package vn.com.gsoft.security.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.security.entity.ApplicationSetting;

import java.util.List;

@Repository
public interface ApplicationSettingRepository extends CrudRepository<ApplicationSetting, Long> {

    List<ApplicationSetting> findByDrugStoreId(String maNhaThuoc);
}
