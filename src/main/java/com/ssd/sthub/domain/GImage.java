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
@Table(name = "gImage")
public class GImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gImageId")
    private Long id;

    @Column(name = "path", nullable = false, columnDefinition = "CLOB")
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupBuyingId")
    private GroupBuying groupBuying;

    @Builder
    public GImage(String path, GroupBuying groupBuying) {
        this.path = path;
        this.groupBuying = groupBuying;
    }

    @Override
    public String toString() {
        return "GImage{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", groupBuying=" + groupBuying +
                '}';
    }
}
