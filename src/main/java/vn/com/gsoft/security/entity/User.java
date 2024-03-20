package vn.com.gsoft.security.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "UserProfile")
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseEntity {
    @Id
    @Column(name = "UserId")
    private Long id;

    @Column(name = "UserName")
    private String username;
    @Column(name = "TenDayDu")
    private String fullName;
    @Column(name = "Password")
    private String password;
    @Column(name = "Email")
    private String email;
    @Column(name = "SoDienThoai")
    private String phoneNumber;
    @Column(name = "SoCMT")
    private String idNumber;
    @Column(name = "MaNhaThuoc")
    private String maNhaThuoc;
    @Column(name = "StoreId")
    private Long storeId;
    @Column(name = "RegionId")
    private Long regionId;
    @Column(name = "CityId")
    private Long cityId;
    @Column(name = "WardId")
    private Long wardId;
    @Column(name = "Addresses")
    private String addresses;
    @Column(name = "HoatDong")
    private Boolean hoatDong;
    @Column(name = "Enable_NT")
    private Boolean enableNT;
}
