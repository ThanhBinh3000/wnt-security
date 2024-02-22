package vn.com.gsoft.security.controller;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import vn.com.gsoft.security.model.dto.ChooseDepartment;
import vn.com.gsoft.security.model.dto.JwtRequest;
import vn.com.gsoft.security.model.dto.JwtResponse;
import vn.com.gsoft.security.model.dto.LoginQr;
import vn.com.gsoft.security.model.system.MessageDTO;
import vn.com.gsoft.security.model.system.Profile;
import vn.com.gsoft.security.service.KafkaProducer;
import vn.com.gsoft.security.service.UserService;
import vn.com.gsoft.security.util.system.JwtTokenUtil;

import java.util.UUID;


@RestController
@Slf4j
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;
    @Autowired
    private KafkaProducer kafkaProducer;

    @Value("${wnt.kafka.internal.producer.topic.login}")
    private String producerTopic;

    @PostMapping(value = "/login")
    public ResponseEntity<JwtResponse> authenticate(
            @RequestBody @Valid JwtRequest jwtRequest) {

        try {
            // Xác thực từ username và password.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );

            // Nếu không xảy ra exception tức là thông tin hợp lệ
            // Set thông tin authentication vào Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Trả về jwt cho người dùng.
            String token = jwtTokenUtil.generateToken(jwtRequest.getUsername());
            String refreshToken = jwtTokenUtil.generateRefreshToken(jwtRequest.getUsername());
            return ResponseEntity.ok(new JwtResponse(token, refreshToken));
        } catch (Exception ex) {
            log.error("Authentication error", ex);
            throw new BadCredentialsException(messageSource.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    new Object[0],
                    LocaleContextHolder.getLocale()));
        }
    }

    @PutMapping(value = "/choose-department")
    public void chooseDepartment(
            @RequestBody @Valid ChooseDepartment chooseDepartment, Authentication authentication, HttpServletRequest request) {
        String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            String jwtToken = requestTokenHeader.substring(7);
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                requestAttributes.setAttribute("chooseDepartment", chooseDepartment, RequestAttributes.SCOPE_REQUEST);
            }
            userService.chooseDepartment(jwtToken);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Profile> getUserDetails(Authentication authentication) {
        final Profile userDetails = (Profile) authentication.getPrincipal();
        return ResponseEntity.ok(userDetails);
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(HttpServletRequest request) {
        String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwtToken;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                Claims claims = jwtTokenUtil.getAllClaimsFromToken(jwtToken);
                String type = (String) claims.get("type");
                String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                if ("refreshtoken".equals(type)) {
                    String refreshToken = jwtTokenUtil.generateRefreshToken(username);
                    return ResponseEntity.ok(new JwtResponse(jwtToken, refreshToken));
                }
            } catch (IllegalArgumentException e) {
                log.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                log.error("JWT Token has expired");
            }
        }
        throw new BadCredentialsException(messageSource.getMessage(
                "AbstractUserDetailsAuthenticationProvider.badCredentials",
                new Object[0],
                LocaleContextHolder.getLocale()));
    }

    @PostMapping(value = "/login-qr")
    public ResponseEntity<String> authenticateQr(@RequestBody @Valid LoginQr loginQr) {

        try {
            Profile profile = (Profile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // Trả về jwt cho người dùng.
            String token = jwtTokenUtil.generateToken(profile.getUsername());
            String refreshToken = jwtTokenUtil.generateRefreshToken(profile.getUsername());
            // send socket.io trong loginQr
            Gson gson = new Gson();
            MessageDTO message = new MessageDTO();
            message.setId(UUID.randomUUID().toString());
            message.setUuid(loginQr.getUuid());
            message.setData(gson.toJson(new JwtResponse(token, refreshToken)));
            String messageStr = gson.toJson(message);
            kafkaProducer.sendInternal(producerTopic, messageStr);
            return ResponseEntity.ok("Thành công!");
        } catch (Exception ex) {
            log.error("Authentication error", ex);
            throw new BadCredentialsException(messageSource.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    new Object[0],
                    LocaleContextHolder.getLocale()));
        }
    }
}
