package vn.com.gsoft.security.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Role")
public class Role extends BaseEntity{
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "RoleName")
    private String roleName;
    @Column(name = "IsDeleted")
    private Boolean isDeleted;
    @Column(name = "MaNhaThuoc")
    private String maNhaThuoc;
    @Column(name = "Description")
    private String description;
    @Column(name = "Type")
    private Integer type;  // 0 mặc định, 1 của nhà thuốc
    @Column(name = "IsDefault")
    private Boolean isDefault;  // true là mặc định
}

