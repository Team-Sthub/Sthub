package com.ssd.sthub.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "sImage")
public class SImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sImageId")
    private Long id;

    @Column(name = "path", nullable = false, columnDefinition = "CLOB")
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "secondhandId")
    private Secondhand secondhand;

    @Builder
    public SImage(String path, Secondhand secondhand) {
        this.path = path;
        this.secondhand = secondhand;
    }
}
