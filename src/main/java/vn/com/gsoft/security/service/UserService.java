package vn.com.gsoft.security.service;

import org.springframework.cache.annotation.Cacheable;
import vn.com.gsoft.security.constant.CachingConstant;
import vn.com.gsoft.security.model.system.Profile;

import java.util.Optional;

public interface UserService extends BaseService {
    Optional<Profile> findUserByToken(String token, String username);

    Optional<Profile> findUserByUsername(String token);

    Optional<Profile> findByUserNameWhenChoose(String username);

    Optional<Profile> getUserNameWhenChoose(String username);

    Optional<Profile> chooseNhaThuocs(String token,String username);
}
