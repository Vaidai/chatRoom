package com.example.javaTask.repository;

import com.example.javaTask.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Message, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO message (account_id, content, timestamp) VALUES (:accountId, :text, NOW())", nativeQuery = true)
    int insertMessageNative(Long accountId, String text);

    @Query(value = "SELECT * FROM message ORDER BY timestamp DESC", nativeQuery = true)
    List<Message> findAllMessagesOrdered();

}
