package vn.com.gsoft.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Settings")
public class Settings {
    @Id
    @Column(name = "Id")
    private Long id;
    @Column(name = "Key")
    private String key;
    @Column(name = "Value")
    private String value;
    @Column(name = "Active")
    private Boolean active;
    @Column(name = "MaNhaThuoc")
    private String maNhaThuoc;
    @Column(name = "StoreId")
    private Long storeId;
}

