package vn.com.gsoft.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.security.entity.NhaThuocs;
import vn.com.gsoft.security.constant.RecordStatusContains;

import java.util.List;

@Repository
public interface NhaThuocsRepository extends CrudRepository<NhaThuocs, Long> {
    List<NhaThuocs> findByMaNhaThuoc(String maNhaThuoc);

    @Query(value = "select p from NhaThuocs p " +
            "join NhanVienNhaThuocs nv on nv.nhaThuocMaNhaThuoc = p.maNhaThuoc " +
            "where nv.recordStatusId =  " + RecordStatusContains.ACTIVE +
            " and nv.userUserId =?1  "
    )
    List<NhaThuocs> findByUserId(Long userId);
}
