package vn.com.gsoft.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.security.entity.Privilege;

import java.util.List;

@Repository
public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {
    @Query(value = "select p from Privilege p " +
            "join PrivilegeEntity pe on pe.privilegeId = p.id " +
            "join RolePrivilege rp on rp.privilegeId = p.id " +
            "join Role r on r.id = rp.roleId " +
            "where p.enable = true and r.isDeleted = false and r.id in ?1 and (r.type = 0 or r.maNhaThuoc = ?2) and pe.entityId = ?3")
    List<Privilege> findByRoleIdInAndMaNhaThuocAndEntityId(List<Long> roleIds, String maNhaThuoc, Long entityId);
}
