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
public class Role {
    @Id
    @Column(name = "RoleId")
    private Long roleId;
    @Column(name = "RoleName")
    private String roleName;
    @Column(name = "IsDeleted")
    private Boolean isDeleted;
    @Column(name = "MaNhaThuoc")
    private String maNhaThuoc;
    @Column(name = "Description")
    private String description;
    @Column(name = "Type")
    private Integer type;
}

