package com.example.mes_api.repository;
import com.example.mes_api.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findTop100ByToUserIdAndFromUserIdOrderByCreatedAtDesc(Long toUserId, Long fromUserId);
}