package vn.com.gsoft.security.model.system;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import vn.com.gsoft.security.entity.NhaThuocs;
import vn.com.gsoft.security.entity.Role;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class Profile extends User {
    private Long id;

    private String fullName;


    private NhaThuocs nhaThuoc;

    private List<Role> roles;

    private List<NhaThuocs> nhaThuocs;

    public Profile(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public Profile(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public Profile(Long id, String fullName, NhaThuocs nhaThuoc, List<Role> roles, List<NhaThuocs> nhaThuocs, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.fullName = fullName;
        this.nhaThuoc = nhaThuoc;
        this.roles = roles;
        this.nhaThuocs = nhaThuocs;
    }
}
