package com.ssd.sthub.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssd.sthub.domain.Member;
import com.ssd.sthub.domain.Message;
import com.ssd.sthub.domain.QMember;
import com.ssd.sthub.domain.QMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ssd.sthub.domain.QMessage.message;

@Slf4j
@Repository
public class MessageRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public MessageRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Message.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    // 모든 사용자와의 주고받은 쪽지 내역 전체 조회 (가장 최근 대화 내용만)
    public QueryResults<Message> findMessagesByAllMember(Long memberId, int pageNum, int size) {
        /*
        * SQL문 *

            SELECT
               message_id,
               sender_id,
               receiver_id,
               content,
               created_at
            FROM
                (SELECT
                    message_id,
                    sender_id,
                    receiver_id,
                    content,
                    created_at,
                    ROW_NUMBER() OVER (PARTITION BY
                                        CASE
                                            WHEN sender_id < receiver_id THEN CONCAT(sender_id, receiver_id)
                                            ELSE CONCAT(receiver_id, sender_id)
                                        END
                                        ORDER BY created_at DESC) AS rn
                 FROM MESSAGE) AS subquery
            WHERE rn = 1 and (sender_id = 3 or receiver_id=3);
        */

        List<Message> results = jpaQueryFactory
                .selectFrom(message)
                .where(
                        message.sender.id.eq(memberId).or(message.receiver.id.eq(memberId)),
                        message.id.in(
                                JPAExpressions
                                        .select(message.id.max())
                                        .from(message)
                                        .where(
                                                message.sender.id.eq(memberId).or(message.receiver.id.eq(memberId)),
                                                message.sender.id.eq(memberId).or(message.receiver.id.eq(memberId))
                                        )
                                        .groupBy(
                                                new CaseBuilder()
                                                        .when(message.sender.id.lt(message.receiver.id)).then(message.sender.id)
                                                        .otherwise(message.receiver.id),
                                                new CaseBuilder()
                                                        .when(message.sender.id.lt(message.receiver.id)).then(message.receiver.id)
                                                        .otherwise(message.sender.id)
                                        )
                        )
                )
                .orderBy(message.createdAt.desc())
                .offset((long) (pageNum - 1) * size)
                .limit(size)
                .fetch();

        long totalCount = results.size();
        long totalPage = totalCount == 0 ? 0 : (totalCount / size) + (totalCount % size == 0 ? 0 : 1);

        return new QueryResults<>(results, (long) size, (long) pageNum * size, totalPage);
    }

    // 특정 사용자와의 쪽지 내역 전체 조회
    @Transactional
    public List<Message> findMessagesByMember(Member member, Member other) {
        QMessage qMessage = QMessage.message;
        QMember qSender = new QMember("sender");
        QMember qReceiver = new QMember("receiver");

        BooleanExpression sentByMemberAndReceivedByOther = qMessage.sender.eq(member).and(qMessage.receiver.eq(other));
        BooleanExpression sentByOtherAndReceivedByMember = qMessage.sender.eq(other).and(qMessage.receiver.eq(member));

        BooleanExpression senderAndReceiver = sentByMemberAndReceivedByOther.or(sentByOtherAndReceivedByMember);

        return jpaQueryFactory
                .selectFrom(qMessage)
                .where(senderAndReceiver)
                .orderBy(qMessage.createdAt.asc())
                .fetch();
    }

    @Transactional
    public List<Message> findNewMessages(Member member, Member other, Long lastMessageId) {
        QMessage qMessage = QMessage.message;
        QMember qSender = new QMember("sender");
        QMember qReceiver = new QMember("receiver");

        BooleanExpression sentByMemberAndReceivedByOther = qMessage.sender.eq(member).and(qMessage.receiver.eq(other));
        BooleanExpression sentByOtherAndReceivedByMember = qMessage.sender.eq(other).and(qMessage.receiver.eq(member));

        BooleanExpression senderAndReceiver = sentByMemberAndReceivedByOther.or(sentByOtherAndReceivedByMember);
        BooleanExpression newMessages = qMessage.id.gt(lastMessageId);

        return jpaQueryFactory
                .selectFrom(qMessage)
                .where(senderAndReceiver.and(newMessages))
                .orderBy(qMessage.createdAt.asc())
                .fetch();
    }
}
