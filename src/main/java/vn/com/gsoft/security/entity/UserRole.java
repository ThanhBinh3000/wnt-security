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
@Table(name = "UserRole")
public class UserRole extends BaseEntity{
    @Id
    @Column(name = "Id")
    private Integer id;
    @Column(name = "UserId")
    private Integer userId;
    @Column(name = "RoleId")
    private Integer roleId;
}

