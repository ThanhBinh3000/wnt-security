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
@Table(name = "UserRole")
@EntityListeners(AuditingEntityListener.class)
public class UserRole extends BaseEntity{
    @Id
    private Long id;
    @Column(name = "RoleId")
    private Long roleId;
    @Column(name = "UserId")
    private Long userId;
}
