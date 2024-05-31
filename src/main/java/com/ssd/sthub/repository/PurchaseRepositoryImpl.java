package com.ssd.sthub.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssd.sthub.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class PurchaseRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public PurchaseRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Purchase.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Secondhand> findTop4Secondhands(Long memberId) {
        QPurchase purchase = QPurchase.purchase;
        QSecondhand secondhand = QSecondhand.secondhand;

        return jpaQueryFactory.select(purchase.secondhand)
                .from(purchase)
                .join(purchase.secondhand, secondhand)
                .where(purchase.member.id.eq(memberId))
                .orderBy(purchase.createdAt.desc())
                .limit(4)
                .fetch();
    }
}
