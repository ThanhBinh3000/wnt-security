package vn.com.gsoft.security.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.security.entity.NhaThuocs;

import java.util.List;

@Repository
public interface NhaThuocsRepository extends CrudRepository<NhaThuocs, Long> {
    List<NhaThuocs> findByMaNhaThuoc(String maNhaThuoc);
}
