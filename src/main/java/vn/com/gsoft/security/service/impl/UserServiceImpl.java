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
import vn.com.gsoft.security.constant.UserStatus;
import vn.com.gsoft.security.entity.Department;
import vn.com.gsoft.security.entity.Role;
import vn.com.gsoft.security.entity.User;
import vn.com.gsoft.security.model.dto.ChooseDepartment;
import vn.com.gsoft.security.model.system.Profile;
import vn.com.gsoft.security.repository.DepartmentRepository;
import vn.com.gsoft.security.repository.RoleRepository;
import vn.com.gsoft.security.repository.UserRepository;
import vn.com.gsoft.security.service.UserService;
import vn.com.gsoft.security.util.system.JwtTokenUtil;

import java.util.*;

@Service
@Slf4j
public class UserServiceImpl extends BaseServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Cacheable(value = CachingConstant.USER)
    public Optional<Profile> findUserByToken(String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token);
        log.warn("Cache findUserByToken missing: {}", username);
        return findUserByUsername(username);
    }

    @Override
    public Optional<Profile> findUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new BadCredentialsException("Không tìm thấy username!");
        }
        Set<SimpleGrantedAuthority> privileges = new HashSet<>();
        List<Department> departments = departmentRepository.findByUserId(user.get().getId());
        return Optional.of(new Profile(
                user.get().getId(),
                user.get().getFullName(),
                null,
                null,
                departments,
                user.get().getUsername(),
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
    public Optional<Profile> chooseDepartment(String token) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            ChooseDepartment chooseDepartment = (ChooseDepartment) requestAttributes.getAttribute("chooseDepartment", RequestAttributes.SCOPE_REQUEST);
            if (chooseDepartment != null) {
                String username = jwtTokenUtil.getUsernameFromToken(token);
                Optional<User> user = userRepository.findByUsername(username);
                if (!user.isPresent()) {
                    throw new BadCredentialsException("Không tìm thấy username!");
                }
                Set<SimpleGrantedAuthority> privileges = new HashSet<>();
                List<Department> departments = departmentRepository.findByUserId(user.get().getId());
                Optional<Department> department = departmentRepository.findById(chooseDepartment.getId());
                List<Role> roles = roleRepository.findByUserIdAndDepartmentId(user.get().getId(), department.get().getId());

                return Optional.of(new Profile(
                        user.get().getId(),
                        user.get().getFullName(),
                        department.get(),
                        roles,
                        departments,
                        user.get().getUsername(),
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
