package com.example.javaTask.controller;

import com.example.javaTask.model.Message;
import com.example.javaTask.service.MessagesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.javaTask.utils.InformMessages.ERROR_SAVING_MESSAGE;
import static com.example.javaTask.utils.InformMessages.MESSAGE_SAVED_SUCCESSFULLY;

@RestController
@RequestMapping("/messages")
public class MessagesController {
    private final MessagesService messagesService;

    public MessagesController(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @GetMapping()
    public List<Message> getMessages() {
        return messagesService.getAllMessagesOrdered();
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveMessage(@RequestBody String message, Authentication authentication) {
        String name = authentication.getName();
        try {
            messagesService.saveMessage(name, message);
            return ResponseEntity.status(HttpStatus.CREATED).body(MESSAGE_SAVED_SUCCESSFULLY);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ERROR_SAVING_MESSAGE + ex.getMessage());
        }
    }

}

