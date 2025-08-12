package com.example.mes_api.service;

import com.example.mes_api.dto.MessageDTO;
import com.example.mes_api.entity.Message;
import com.example.mes_api.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final MessageRepository repo;
    public MessageService(MessageRepository repo) { this.repo = repo; }

    public void send(MessageDTO dto) {
        Message m = new Message();
        m.setFromUserId(dto.getFromUserId());
        m.setToUserId(dto.getToUserId());
        m.setText(dto.getText());
        repo.save(m);
        // TODO: отправлять в WebSocket топик
    }

    public List<MessageDTO> history(Long a, Long b) {
        return repo.findTop100ByToUserIdAndFromUserIdOrderByCreatedAtDesc(a,b)
                .stream().map(m -> new MessageDTO(m.getId(), m.getFromUserId(), m.getToUserId(), m.getText(), m.getCreatedAt()))
                .collect(Collectors.toList());
    }
}