package com.example.javaTask.service;

import com.example.javaTask.model.Account;
import com.example.javaTask.model.AccountRole;
import com.example.javaTask.model.Message;
import com.example.javaTask.repository.AccountRepository;
import com.example.javaTask.repository.MessagesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.javaTask.utils.InformMessages.ACCOUNT_NOT_FOUND;
import static com.example.javaTask.utils.InformMessages.FAILED_TO_INSERT_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessagesServiceTest {
    @InjectMocks
    private MessagesService messagesService;
    @Mock
    private MessagesRepository messagesRepository;
    @Mock
    private AccountRepository accountRepository;

    @Test
    void getAllMessagesOrdered_shouldReturnMessages() {
        Account account = new Account();
        account.setRole(AccountRole.ADMIN);
        account.setName("admin");
        account.setId(1L);
        List<Message> expectedMessages = new ArrayList<>();
        Message message1 = new Message(1L, account, "message text", LocalDateTime.now());
        Message message2 = new Message(2L, account, "another message", LocalDateTime.now());
        expectedMessages.add(message1);
        expectedMessages.add(message2);

        when(messagesRepository.findAllMessagesOrdered()).thenReturn(expectedMessages);

        List<Message> result = messagesService.getAllMessagesOrdered();

        assertEquals(expectedMessages, result);
        assertEquals(2, result.size());
        verify(messagesRepository).findAllMessagesOrdered();
    }

    @Test
    void saveMessage_shouldSaveSuccessfully() {
        String accountName = "testUser";
        String messageContent = "Test Message";
        Account account = new Account(1L, accountName, AccountRole.ADMIN);

        when(accountRepository.findByName(accountName)).thenReturn(Optional.of(account));
        when(messagesRepository.insertMessageNative(account.getId(), messageContent)).thenReturn(1);

        assertDoesNotThrow(() -> messagesService.saveMessage(accountName, messageContent));

        verify(accountRepository).findByName(accountName);
        verify(messagesRepository).insertMessageNative(account.getId(), messageContent);
    }

    @Test
    void saveMessage_shouldThrowExceptionWhenAccountNotFound() {
        String accountName = "nonExistentUser";
        when(accountRepository.findByName(accountName)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> messagesService.saveMessage(accountName, "Message"));

        assertEquals(ACCOUNT_NOT_FOUND, exception.getMessage());
        verify(accountRepository).findByName(accountName);
        verify(messagesRepository, never()).insertMessageNative(anyLong(), anyString());
    }

    @Test
    void saveMessage_shouldThrowExceptionWhenInsertFails() {
        String accountName = "testUser";
        String messageContent = "Message";
        Account account = new Account(1L, accountName, AccountRole.ADMIN);

        when(accountRepository.findByName(accountName)).thenReturn(Optional.of(account));
        when(messagesRepository.insertMessageNative(account.getId(), messageContent)).thenReturn(0);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> messagesService.saveMessage(accountName, messageContent));

        assertEquals(FAILED_TO_INSERT_MESSAGE, exception.getMessage());
        verify(messagesRepository, times(1)).insertMessageNative(account.getId(), messageContent);
    }
}