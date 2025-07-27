package com.example.javaTask.service;

import com.example.javaTask.dto.AccountDto;
import com.example.javaTask.model.Account;
import com.example.javaTask.model.AccountMessageStats;
import com.example.javaTask.model.AccountRole;
import com.example.javaTask.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.javaTask.utils.InformMessages.*;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }


    public Account registerAccount(AccountDto accountDto) throws Exception {
        if (accountRepository.findByName(accountDto.getName()).isPresent()) {
            throw new Exception(ACCOUNT_ALREADY_EXISTS_WITH_NAME + accountDto.getName());
        }

        Account account = new Account();
        account.setName(accountDto.getName());

        try {
            AccountRole role = AccountRole.valueOf(accountDto.getRole().trim().toUpperCase());
            account.setRole(role);

        } catch (IllegalArgumentException e) {
            throw new Exception(INVALID_ROLE + accountDto.getRole());
        }

        int rows = accountRepository.insertAccountNative(account.getName(), account.getRole().toString());
        if (rows != 1) throw new Exception(INSERT_FAILED);

        return accountRepository.findByName(account.getName())
                .orElseThrow(() -> new Exception(ACCOUNT_NOT_FOUND_AFTER_INSERT));
    }


    @Transactional
    public void deleteAccount(Long id) throws Exception {
        accountRepository.findById(id)
                .orElseThrow(() -> new Exception(ACCOUNT_NOT_FOUND_WITH_ID + id));
        accountRepository.deleteAccount(id);
    }

    public List<AccountMessageStats> getAccountMessageStats() {
        List<Object[]> rawResults = accountRepository.findAccountMessageStatsRaw();

        return rawResults.stream()
                .map(row -> new AccountMessageStats(
                        (String) row[0],
                        ((Number) row[1]).longValue(),
                        (Timestamp) row[2],
                        (Timestamp) row[3],
                        ((Number) row[4]).intValue(),
                        (String) row[5]
                ))
                .collect(Collectors.toList());
    }
}
