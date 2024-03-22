package vn.com.gsoft.security.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "UserProfile")
public class UserProfile implements Serializable {
    @Id
    @Column(name = "UserId")
    private Long userId;
    @Column(name = "UserName")
    private String userName;
    @Column(name = "Password")
    private String password;
    @Column(name = "TenDayDu")
    private String tenDayDu;
    @Column(name = "Email")
    private String email;
    @Column(name = "SoDienThoai")
    private String soDienThoai;
    @Column(name = "MaNhaThuoc")
    private String maNhaThuoc;
    @Column(name = "HoatDong")
    private Boolean hoatDong;
    @Column(name = "SoCMT")
    private String soCMT;
    @Column(name = "Enable_NT")
    private Boolean enableNT;
    @Column(name = "ArchivedId")
    private Integer archivedId;
    @Column(name = "StoreId")
    private Integer storeId;
    @Column(name = "RegionId")
    private Integer regionId;
    @Column(name = "CityId")
    private Integer cityId;
    @Column(name = "WardId")
    private Integer wardId;
    @Column(name = "Addresses")
    private String addresses;
    @Column(name = "TokenDevice")
    private String tokenDevice;
    @Column(name = "TokenBrowser")
    private String tokenBrowser;
    @Column(name = "IsVerificationAccount")
    private Boolean isVerificationAccount;
    @Column(name = "TokenDevice2")
    private String tokenDevice2;
    @Column(name = "MobileDeviceId")
    private String mobileDeviceId;
    @Column(name = "EntityId")
    private Long entityId;
}

