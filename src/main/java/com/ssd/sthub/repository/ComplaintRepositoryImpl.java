package com.ssd.sthub.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssd.sthub.domain.*;
import com.ssd.sthub.dto.complaint.ComplaintRepoDTO;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class ComplaintRepositoryImpl extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public ComplaintRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Complaint.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<ComplaintRepoDTO> findComplaintRepoDTOByMemberId(Long memberId) {
        QComplaint complaint = QComplaint.complaint;

        List<ComplaintRepoDTO> complaintReports = jpaQueryFactory
                .select(Projections.constructor(ComplaintRepoDTO.class, complaint.groupBuying.member.id))
                .from(complaint)
                .where(complaint.groupBuying.member.id.eq(memberId)
                        .or(complaint.secondhand.member.id.eq(memberId)))
                .fetch();

        for (ComplaintRepoDTO dto : complaintReports) {
//            Set<Integer> trueIndexes = jpaQueryFactory
//                    .select(complaint.tags.indexOf(true))
//                    .from(complaint)
//                    .where(complaint.groupBuying.member.id.eq(memberId)
//                            .or(complaint.secondhand.member.id.eq(memberId)))
//                    .fetch().stream().collect(Collectors.toSet());
//            dto.setTrueIndexes(trueIndexes);

//            ComplaintRepoDTO report = new ComplaintRepoDTO(memberId);
//            List<Boolean> tags = complaint.getTags();
//            Set<Integer> trueIndexes = IntStream.range(0, tags.size())
//                    .filter(i -> tags.get(i))
//                    .boxed()
//                    .collect(Collectors.toSet());
//            report.setTrueIndexes(trueIndexes);
//            complaintReports.add(report);
        }

        return complaintReports;
    }
}
