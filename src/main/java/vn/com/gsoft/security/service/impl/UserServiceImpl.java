package vn.com.gsoft.security.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import vn.com.gsoft.security.constant.CachingConstant;
import vn.com.gsoft.security.entity.NhaThuocs;
import vn.com.gsoft.security.entity.Privilege;
import vn.com.gsoft.security.entity.Role;
import vn.com.gsoft.security.entity.UserProfile;
import vn.com.gsoft.security.model.dto.ChooseNhaThuocs;
import vn.com.gsoft.security.model.system.Profile;
import vn.com.gsoft.security.repository.NhaThuocsRepository;
import vn.com.gsoft.security.repository.PrivilegeRepository;
import vn.com.gsoft.security.repository.RoleRepository;
import vn.com.gsoft.security.repository.UserProfileRepository;
import vn.com.gsoft.security.service.UserService;
import vn.com.gsoft.security.util.system.JwtTokenUtil;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private NhaThuocsRepository nhaThuocsRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Override
    public Optional<Profile> findUserByToken(String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return findUserByUsername(username);
    }

    @Override
    @Cacheable(value = CachingConstant.USER)
    public Optional<Profile> findUserByUsername(String username) {
        log.warn("Cache findUserByUsername missing: {}", username);
        Optional<UserProfile> user = userProfileRepository.findByUserName(username);
        if (!user.isPresent()) {
            throw new BadCredentialsException("Không tìm thấy username!");
        }
        Set<SimpleGrantedAuthority> privileges = new HashSet<>();
        List<NhaThuocs> nhaThuocs = nhaThuocsRepository.findByMaNhaThuoc(user.get().getMaNhaThuoc());
        return Optional.of(new Profile(
                user.get().getUserId(),
                user.get().getTenDayDu(),
                null,
                null,
                nhaThuocs,
                user.get().getUserName(),
                user.get().getPassword(),
                user.get().getHoatDong() && (user.get().getEnableNT() != null ? user.get().getEnableNT() : true),
                true,
                true,
                true,
                privileges
        ));
    }

    @Override
    @CachePut(value = CachingConstant.USER)
    public Optional<Profile> chooseNhaThuocs(String token) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            ChooseNhaThuocs chooseNhaThuocs = (ChooseNhaThuocs) requestAttributes.getAttribute("chooseNhaThuocs", RequestAttributes.SCOPE_REQUEST);
            if (chooseNhaThuocs != null) {
                String username = jwtTokenUtil.getUsernameFromToken(token);
                Optional<UserProfile> user = userProfileRepository.findByUserName(username);
                if (!user.isPresent()) {
                    throw new BadCredentialsException("Không tìm thấy username!");
                }
                Set<SimpleGrantedAuthority> privileges = new HashSet<>();
                List<NhaThuocs> nhaThuocs = nhaThuocsRepository.findByMaNhaThuoc(user.get().getMaNhaThuoc());
                Optional<NhaThuocs> nhaThuoc = nhaThuocsRepository.findById(chooseNhaThuocs.getId());
                List<Role> roles = roleRepository.findByUserIdAndMaNhaThuoc(user.get().getUserId(), nhaThuoc.get().getMaNhaThuoc());
                List<Long> roleIds = roles.stream()
                        .map(Role::getRoleId) // Extract the ID from each role
                        .collect(Collectors.toList());
                List<Privilege> privilegeObjs = privilegeRepository.findByRoleIdInAndMaNhaThuocAndEntityId(roleIds, nhaThuoc.get().getMaNhaThuoc(), user.get().getEntityId());
                for(Privilege p : privilegeObjs){
                    privileges.add(new SimpleGrantedAuthority(p.getCode()));
                }
                return Optional.of(new Profile(
                        user.get().getUserId(),
                        user.get().getTenDayDu(),
                        nhaThuoc.get(),
                        roles,
                        nhaThuocs,
                        user.get().getUserName(),
                        user.get().getPassword(),
                        user.get().getHoatDong() && (user.get().getEnableNT() != null ? user.get().getEnableNT() : true),
                        true,
                        true,
                        true,
                        privileges
                ));
            }
        }
        return Optional.ofNullable(null);
    }

    @Override
    @Cacheable(value = CachingConstant.USER)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByUsername(username).get();
    }
}
