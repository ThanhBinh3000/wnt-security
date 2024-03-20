package vn.com.gsoft.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "NhaThuocs")
public class NhaThuocs extends BaseEntity {

    @Id
    private Long id;
    private String maNhaThuoc;
    private String tenNhaThuoc;
    private String diaChi;
    private String soKinhDoanh;
    private String dienThoai;
    private String nguoiDaiDien;
    private String email;
    private String mobile;
    private String duocSy;
    private Boolean hoatDong;
    private Boolean active;
    private Boolean isReportDataGenerating;
    private Long tinhThanhId;
    private String maNhaThuocCha;
    private Boolean isConnectivity;
    private String connectivityCode;
    private String connectivityUserName;
    private String connectivityPassword;
    private String generalPharmacyId;
    private Boolean isGeneralPharmacy;
    private BigDecimal paidAmount;
    private String description;
    private Long drugStoreTypeId;
    private Boolean isPaid;
    private Long chainLinkId;
    private Long regionId;
    private Long cityId;
    private Long wardId;
    private LocalDateTime lastTransDate;
    private String supportPhones;
    private String deliveryPolicy;
    private String contentThankYou;
    private Long recordStatusId;
    private Long idTypeBasic;
    private String ghiChu;
    private Boolean isNationalDBConnected;
    private String imagePreviewUrl;
    private String imageThumbUrl;
    private Integer totalNumberInvoices;
    private String connEInvoiceUserName;
    private String connEInvoicePassword;
    private String symbolCodeInvocie;
    private String formNumberInvoice;
    private Integer typeInvoice;
    private Integer paymentStatus;
    private BigDecimal paidMoney;
    private Boolean isNationalSampleConnected;
    private String connectivityCodeMeidcal;
    private String connectivityPasswordMedical;
    private LocalDateTime expiredDate;
    private String businessDescription;
    private LocalDateTime paidDate;
    private Boolean isUploading;
    private String connEInvoiceSerialCert;
    private String linkConnectEInvoice;
    private String nameServiceEInvoice;
    private String passServiceEInvoice;
    private String signedString;
    private Integer typeSendEinvocie;
    @Column(name="Slug_CustomerWebsite")
    private String slugCustomerWebsite;
    @Column(name="GoogleLocation_CustomerWebsite")
    private String googleLocationCustomerWebsite;
    @Column(name="ThemeId_CustomerWebsite")
    private Long themeIdCustomerWebsite;
    private String tokenZalo;
    private String zaloKey;
    private String appId;
    private String refreshTokenZalo;
    private String imageOrderThumbUrl;
    private String imageOrderPreviewUrl;
    private Integer typeMessage;
    @Column(name="Banner_CustomerWebsite")
    private String bannerCustomerWebsite;
    @Column(name="MainSlogan_CustomerWebsite")
    private String mainSloganCustomerWebsite;
    @Column(name="SubSlogan_CustomerWebsite")
    private String subSloganCustomerWebsite;
    private String qRDeviceName;
    private String qRDeviceToken;
    private String simData;
    private Boolean mapped;
    @Column(name="ZNS_StatusSendCreateAccount")
    private Boolean zNSStatusSendCreateAccount;
    @Column(name="ZNS_TrackingIdCreateAccount")
    private String zNSTrackingIdCreateAccount;
    @Column(name="ZNS_StatusSendPayment")
    private Boolean zNSStatusSendPayment;
    @Column(name="ZNS_TrackingIdPayment")
    private String zNSTrackingIdPayment;
    private Long businessId;
    @Column(name="CodeErrorConfirmPayment_ZNS")
    private Integer codeErrorConfirmPaymentZNS;
    @Column(name="CodeErrorCreateAccount_ZNS")
    private Integer codeErrorCreateAccountZNS;
    private String footerPrint;
    private Integer classify;
    private Integer evaluate;
    private Long supporterId;
    private Boolean upgradeToPlus;
}
