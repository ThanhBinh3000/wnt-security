package vn.com.gsoft.security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NhanVienNhaThuocs")
public class NhanVienNhaThuocs extends BaseEntity{
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "Role")
    private String role;
    @Column(name = "NhaThuoc_MaNhaThuoc")
    private String nhaThuocMaNhaThuoc;
    @Column(name = "User_UserId")
    private Long userUserId;
    @Column(name = "ArchivedId")
    private Long archivedId;
    @Column(name = "StoreId")
    private Long storeId;
}

