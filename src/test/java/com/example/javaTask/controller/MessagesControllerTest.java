package com.example.javaTask.controller;

import com.example.javaTask.model.Account;
import com.example.javaTask.model.AccountRole;
import com.example.javaTask.model.Message;
import com.example.javaTask.service.MessagesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.javaTask.utils.InformMessages.ERROR_SAVING_MESSAGE;
import static com.example.javaTask.utils.InformMessages.MESSAGE_SAVED_SUCCESSFULLY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MessagesControllerTest {
    @InjectMocks
    private MessagesController messagesController;
    @Mock
    private MessagesService messagesService;
    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMessages_shouldReturnOrderedMessages() {
        Account account = new Account();
        account.setRole(AccountRole.ADMIN);
        account.setName("admin");
        account.setId(1L);
        List<Message> expectedMessages = new ArrayList<>();
        Message message1 = new Message(1L, account, "message text", LocalDateTime.now());
        Message message2 = new Message(2L, account, "another message", LocalDateTime.now());
        expectedMessages.add(message1);
        expectedMessages.add(message2);

        when(messagesService.getAllMessagesOrdered()).thenReturn(expectedMessages);

        List<Message> actualMessages = messagesController.getMessages();

        assertEquals(expectedMessages, actualMessages);
        verify(messagesService).getAllMessagesOrdered();
    }

    @Test
    void saveMessage_shouldReturnCreatedStatusOnSuccess() {
        String message = "Test message";
        String username = "john";

        when(authentication.getName()).thenReturn(username);

        ResponseEntity<?> response = messagesController.saveMessage(message, authentication);

        verify(messagesService).saveMessage(username, message);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(MESSAGE_SAVED_SUCCESSFULLY, response.getBody());
    }

    @Test
    void saveMessage_shouldReturnBadRequestOnFailure() {
        String message = "Test message";
        String username = "john";
        String errorMessage = "Account not found";

        when(authentication.getName()).thenReturn(username);
        doThrow(new RuntimeException(errorMessage)).when(messagesService).saveMessage(username, message);

        ResponseEntity<?> response = messagesController.saveMessage(message, authentication);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ERROR_SAVING_MESSAGE + errorMessage, response.getBody());
    }
}