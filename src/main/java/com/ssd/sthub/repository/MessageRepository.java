package com.ssd.sthub.repository;

import com.ssd.sthub.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findAllBySenderId(Long senderId, PageRequest pageRequest);
    List<Message> findAllBySenderIdAndReceiverId(Long senderId, Long receiverId);
    Message save(Message msg);
}
