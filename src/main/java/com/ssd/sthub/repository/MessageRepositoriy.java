package com.ssd.sthub.repository;

import com.ssd.sthub.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepositoriy extends JpaRepository<Message, Long> {
    Page<Message> findAllByMemberId(Long memberId, PageRequest pageRequest);
    List<Message> findAllBySenderIdAndReceiverId(Long memberId, Long otherId);
    Message save(Message msg);
}
