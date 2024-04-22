package vn.com.gsoft.security.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.security.entity.Settings;

import java.util.List;

@Repository
public interface SettingsRepository extends CrudRepository<Settings, Long> {

    List<Settings> findByMaNhaThuoc(String maNhaThuoc);
}
