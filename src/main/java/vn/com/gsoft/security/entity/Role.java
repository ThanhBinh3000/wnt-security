package vn.com.gsoft.security.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Role")
@EntityListeners(AuditingEntityListener.class)
public class Role extends BaseEntity {
    @Id
    @Column(name = "RoleId")
    private Long id;

    @Column(name = "MaNhaThuoc")
    private Long maNhaThuoc;

    @Column(name = "RoleName")
    private String name;

    @Column(name = "IsDeleted")
    private Boolean isDeleted;

    @Column(name = "Description")
    private String description;

    @Column(name = "Type")
    private Integer type;
}
