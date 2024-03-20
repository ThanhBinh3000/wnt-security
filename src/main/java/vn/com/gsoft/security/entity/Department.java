package vn.com.gsoft.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "NhaThuocs")
public class Department extends BaseEntity {
    @Id
    private Long id;
    @Column(name = "Code")
    private String code;
    @Column(name = "UserId")
    private Long userId;
}
