package vn.com.gsoft.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.security.entity.Role;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    @Query(value = "SELECT r from Role r " +
            "join UserRole ur on ur.roleId = r.roleId  " +
            "where ur.userId = ?1 and (r.type = 0 or r.maNhaThuoc = ?2)")
    List<Role> findByUserIdAndMaNhaThuoc(Long userId, String maNhaThuoc);
}
