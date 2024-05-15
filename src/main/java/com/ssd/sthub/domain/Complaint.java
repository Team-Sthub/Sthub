package com.ssd.sthub.domain;

import com.ssd.sthub.dto.complaint.ComplaintDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    // 조건 (각 항목은 노션에 정리되어 있음) 0,1로 구분 0-> 선택 안된 tag, 1-> 선택된 tag
    @NotEmpty
    @Convert(converter = StringListConverter.class)
    @Column(name = "tag")
    private List<Integer> tags = new ArrayList<Integer>();

    @CreatedDate
    @Column(name = "createdAt", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    // 공동구매 조인
    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "groupBuyingId", nullable = true)
    private GroupBuying groupBuying;

    // 중고거래 조인
    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "secondhandId", nullable = true)
    private Secondhand secondhand;

    public Complaint(ComplaintDTO request, Secondhand secondhand) {
        this.tags = List.of(
                request.getTag1(),
                request.getTag2(),
                request.getTag3(),
                request.getTag4(),
                request.getTag5(),
                request.getTag6(),
                request.getTag7(),
                request.getTag8(),
                request.getTag9()
        );
        this.secondhand = secondhand;
    }

    public Complaint(ComplaintDTO request, GroupBuying groupBuying) {
        this.tags = List.of(
                request.getTag1(),
                request.getTag2(),
                request.getTag3(),
                request.getTag4(),
                request.getTag5(),
                request.getTag6(),
                request.getTag7(),
                request.getTag8(),
                request.getTag9()
        );
        this.groupBuying = groupBuying;
    }
}
