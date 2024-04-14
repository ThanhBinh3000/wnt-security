package vn.com.gsoft.security.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.BadCredentialsException;
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
import vn.com.gsoft.security.model.dto.ChooseNhaThuoc;
import vn.com.gsoft.security.model.dto.NhaThuocsReq;
import vn.com.gsoft.security.model.dto.NhaThuocsRes;
import vn.com.gsoft.security.model.system.CodeGrantedAuthority;
import vn.com.gsoft.security.model.system.Profile;
import vn.com.gsoft.security.repository.NhaThuocsRepository;
import vn.com.gsoft.security.repository.PrivilegeRepository;
import vn.com.gsoft.security.repository.RoleRepository;
import vn.com.gsoft.security.repository.UserProfileRepository;
import vn.com.gsoft.security.service.RedisListService;
import vn.com.gsoft.security.service.UserService;
import vn.com.gsoft.security.util.system.DataUtils;
import vn.com.gsoft.security.util.system.JwtTokenUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    @Autowired
    private RedisListService redisListService;

    @Override
    @Cacheable(value = CachingConstant.USER_TOKEN, key = "#token+ '-' +#username")
    public Optional<Profile> findUserByToken(String token, String username) {
        log.warn("Cache findUserByToken missing: {}", username);
        redisListService.addValueToListEnd(username, token);
        return findUserByUsername(username);
    }

    @Override
    public Optional<Profile> findUserByUsername(String username) {
        Optional<UserProfile> user = userProfileRepository.findByUserName(username);
        if (!user.isPresent()) {
            throw new BadCredentialsException("Không tìm thấy username!");
        }
        Set<CodeGrantedAuthority> privileges = new HashSet<>();
        List<NhaThuocs> nhaThuocs = nhaThuocsRepository.findByUserId(user.get().getId());
        NhaThuocs nhaThuoc = null;
        List<Role> roles = null;
        if (nhaThuocs.size() == 1) {
            nhaThuoc = nhaThuocs.get(0);
            NhaThuocsReq req = new NhaThuocsReq();
            req.setMaNhaThuoc(nhaThuoc.getMaNhaThuoc());
            req.setUserIdQueryData(user.get().getId());
            NhaThuocsRes nhaThuocsRes = DataUtils.convertOne(nhaThuocsRepository.getUserRoleNhaThuoc(req), NhaThuocsRes.class);
            nhaThuoc.setRole(nhaThuocsRes.getRole());
            roles = roleRepository.findByUserIdAndMaNhaThuoc(user.get().getId(), nhaThuoc.getMaNhaThuoc());
            List<Long> roleIds = roles.stream()
                    .map(Role::getId) // Extract the ID from each role
                    .collect(Collectors.toList());
            List<Privilege> privilegeObjs = privilegeRepository.findByRoleIdInAndMaNhaThuocAndEntityId(roleIds, nhaThuoc.getMaNhaThuoc(), user.get().getEntityId());
            for (Privilege p : privilegeObjs) {
                privileges.add(new CodeGrantedAuthority(p.getCode()));
            }
        }
        return Optional.of(new Profile(
                user.get().getId(),
                user.get().getTenDayDu(),
                nhaThuoc,
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

    @Override
    public Optional<Profile> findByUserNameWhenChoose(String username) {
        Optional<UserProfile> user = userProfileRepository.findByUserName(username);
        if (!user.isPresent()) {
            throw new BadCredentialsException("Không tìm thấy username!");
        }
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ChooseNhaThuoc chooseNhaThuoc = (ChooseNhaThuoc) requestAttributes.getAttribute("chooseNhaThuoc", RequestAttributes.SCOPE_REQUEST);
        Set<CodeGrantedAuthority> privileges = new HashSet<>();
        List<NhaThuocs> nhaThuocs = nhaThuocsRepository.findByMaNhaThuoc(user.get().getMaNhaThuoc());
        Optional<NhaThuocs> nhaThuoc = nhaThuocsRepository.findById(chooseNhaThuoc.getId());
        NhaThuocsReq req = new NhaThuocsReq();
        req.setMaNhaThuoc(nhaThuoc.get().getMaNhaThuoc());
        req.setUserIdQueryData(user.get().getId());
        NhaThuocsRes nhaThuocsRes = DataUtils.convertOne(nhaThuocsRepository.getUserRoleNhaThuoc(req), NhaThuocsRes.class);
        nhaThuoc.get().setRole(nhaThuocsRes.getRole());
        List<Role> roles = roleRepository.findByUserIdAndMaNhaThuoc(user.get().getId(), nhaThuoc.get().getMaNhaThuoc());
        List<Long> roleIds = roles.stream()
                .map(Role::getId) // Extract the ID from each role
                .collect(Collectors.toList());
        List<Privilege> privilegeObjs = privilegeRepository.findByRoleIdInAndMaNhaThuocAndEntityId(roleIds, nhaThuoc.get().getMaNhaThuoc(), user.get().getEntityId());
        for (Privilege p : privilegeObjs) {
            privileges.add(new CodeGrantedAuthority(p.getCode()));
        }
        return Optional.of(new Profile(
                user.get().getId(),
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

    @Override
    public Optional<Profile> getUserNameWhenChoose(String username) {
        return Optional.empty();
    }

    @Override
    @CachePut(value = CachingConstant.USER_TOKEN, key = "#token+ '-' +#username")
    public Optional<Profile> chooseNhaThuoc(String token, String username) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            ChooseNhaThuoc chooseNhaThuoc = (ChooseNhaThuoc) requestAttributes.getAttribute("chooseNhaThuoc", RequestAttributes.SCOPE_REQUEST);
            if (chooseNhaThuoc != null) {
                redisListService.addValueToListEnd(username, token);
                return findByUserNameWhenChoose(username);
            }
        }
        return Optional.ofNullable(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findUserByUsername(username).get();
    }
}
