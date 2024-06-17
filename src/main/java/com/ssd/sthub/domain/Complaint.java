package com.ssd.sthub.domain;

import com.ssd.sthub.dto.complaint.ComplaintDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "complaint")
public class Complaint extends BaseTime {

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

    // 공동구매 조인
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "groupBuyingId")
    private GroupBuying groupBuying;

    // 중고거래 조인
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "secondhandId")
    private Secondhand secondhand;

    public Complaint(ComplaintDTO.Request request, Optional<Secondhand> secondhandOptional, Optional<GroupBuying> groupBuyingOptional) {
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

        if (secondhandOptional.isPresent()) {
            this.secondhand = secondhandOptional.get();
        } else if (groupBuyingOptional.isPresent()) {
            this.groupBuying = groupBuyingOptional.get();
        }
    }
}