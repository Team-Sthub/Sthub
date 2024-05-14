package com.ssd.sthub.domain;

import com.ssd.sthub.dto.complaint.ComplaintDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "complaint")
public class Complaint {

    // 신고 식별값
    @Id
    @Column(name="complaintId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 조건 (각 항목은 노션에 정리되어 있음)
    @ElementCollection
    @CollectionTable(name = "complaint_tags", joinColumns = @JoinColumn(name = "complaintId"))
    @Column(name = "tag")
    private List<Boolean> tags;

    @CreatedDate
    @Column(name = "createdAt", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    // 공동구매 조인
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "groupBuyingId")
    private GroupBuying groupBuying;

    // 중고거래 조인
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "secondhandId")
    private Secondhand secondhand;

    public Complaint(ComplaintDTO request, Secondhand secondhand) {
        this.tags = List.of(
                request.isTag1(),
                request.isTag2(),
                request.isTag3(),
                request.isTag4(),
                request.isTag5(),
                request.isTag6(),
                request.isTag7(),
                request.isTag8(),
                request.isTag9()
        );
        this.secondhand = secondhand;
    }

    public Complaint(ComplaintDTO request, GroupBuying groupBuying) {
        this.tags = List.of(
                request.isTag1(),
                request.isTag2(),
                request.isTag3(),
                request.isTag4(),
                request.isTag5(),
                request.isTag6(),
                request.isTag7(),
                request.isTag8(),
                request.isTag9()
        );
        this.groupBuying = groupBuying;
    }
}
