package com.ssd.sthub.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssd.sthub.domain.*;
import com.ssd.sthub.dto.complaint.ComplaintRepoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Repository
public class ComplaintRepositoryImpl extends QuerydslRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    public ComplaintRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Complaint.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Integer> findComplaintRepoDTOByMemberId(Long memberId) {
        QComplaint complaint = QComplaint.complaint;

        // 특정 회원의 신고 내역 조회
        List<Complaint> complaints = jpaQueryFactory
                .selectFrom(complaint)
                .leftJoin(complaint.groupBuying).on(complaint.groupBuying.member.id.eq(memberId))
                .leftJoin(complaint.secondhand).on(complaint.secondhand.member.id.eq(memberId))
                .fetch();
        log.info("======================1");

        // 모든 신고에서 true 인덱스의 종류 수집
        Set<Integer> trueIndexes = new HashSet<>();
        complaints.forEach(complaintEntity -> {
            List<Integer> tags = complaintEntity.getTags();
            log.info("======================2");
            log.info(tags.toString());
            Set<Integer> indexes = IntStream.range(0, tags.size())
                    .filter(i -> Objects.equals(tags.get(i), 1)) // 1인 경우만 필터링
                    .boxed()
                    .collect(Collectors.toSet());
            trueIndexes.addAll(indexes);
        });

        List<Integer> indexList = new ArrayList<>(trueIndexes);
        return indexList;
    }
}
