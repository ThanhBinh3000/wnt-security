package vn.com.gsoft.security.service;

import vn.com.gsoft.security.model.system.Profile;

import java.util.Optional;

public interface UserService extends BaseService {
    Optional<Profile> findUserByToken(String token);

    Optional<Profile> findUserByUsername(String token);

    Optional<Profile> chooseNhaThuocs(String jwtToken);
}
