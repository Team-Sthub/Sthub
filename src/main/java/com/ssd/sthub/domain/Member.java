package com.ssd.sthub.domain;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.ssd.sthub.domain.enumerate.Bank;
import com.ssd.sthub.dto.member.MemberDTO;
import com.ssd.sthub.dto.member.RegisterDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Entity
@AllArgsConstructor
@Table(name = "member")
public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Long id;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "bank", nullable = false)
    @Enumerated(EnumType.STRING)
    private Bank bank;

    @Column(name = "account", nullable = false)
    private String account;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "email", nullable = false)
    private String email;

    @Setter
    @Column(name = "profile", nullable = true)
    private String profile;

    @Column(name = "mannerGrade", nullable = false)
    @ColumnDefault("3.0")
    private Double mannerGrade;

    @Column(name = "status", nullable = false)
    @ColumnDefault("'active'")
    private String status;

    @Column(name = "complaintCount", nullable = false)
    @ColumnDefault("0")
    private int complaintCount;

    public Member(RegisterDTO registerDTO) {
        this.nickname = registerDTO.getNickname();
        this.password = registerDTO.getPassword();
        this.phone = registerDTO.getPhone();
        this.bank = registerDTO.getBank();
        this.account = registerDTO.getAccount();
        this.address = registerDTO.getAddress();
        this.email = registerDTO.getEmail();
        this.profile = registerDTO.getProfile();
        this.latitude = registerDTO.getLatitude();
        this.longitude = registerDTO.getLongitude();
    }

    public void updateInfo(MemberDTO memberDTO) {
        this.nickname = memberDTO.getNickname();
        this.password = memberDTO.getPassword();
        this.phone = memberDTO.getPhone();
        this.bank = memberDTO.getBank();
        this.account = memberDTO.getAccount();
        this.address = memberDTO.getAddress();
        this.longitude = memberDTO.getLongitude();
        this.latitude = memberDTO.getLatitude();
        this.email = memberDTO.getEmail();
        this.profile = memberDTO.getProfile();
    }
}
