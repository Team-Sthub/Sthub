package com.ssd.sthub.repository;

import com.ssd.sthub.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepositoriy extends JpaRepository<Message, Long> {

}
