package com.example.javaTask.service;

import com.example.javaTask.model.Account;
import com.example.javaTask.model.Message;
import com.example.javaTask.repository.AccountRepository;
import com.example.javaTask.repository.MessagesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.javaTask.utils.InformMessages.ACCOUNT_NOT_FOUND;
import static com.example.javaTask.utils.InformMessages.FAILED_TO_INSERT_MESSAGE;

@Service
public class MessagesService {

    private final MessagesRepository messagesRepository;
    private final AccountRepository accountRepository;

    public MessagesService(MessagesRepository messagesRepository, AccountRepository accountRepository) {
        this.messagesRepository = messagesRepository;
        this.accountRepository = accountRepository;
    }

    public List<Message> getAllMessagesOrdered() {
        return messagesRepository.findAllMessagesOrdered();
    }

    public void saveMessage(String accountName, String message) {
        Account account = accountRepository.findByName(accountName)
                .orElseThrow(() -> new RuntimeException(ACCOUNT_NOT_FOUND));

        int rows = messagesRepository.insertMessageNative(account.getId(), message);

        if (rows != 1) {
            throw new RuntimeException(FAILED_TO_INSERT_MESSAGE);
        }
    }

}
