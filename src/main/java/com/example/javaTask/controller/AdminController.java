package com.example.javaTask.controller;

import com.example.javaTask.dto.AccountDto;
import com.example.javaTask.model.Account;
import com.example.javaTask.model.AccountMessageStats;
import com.example.javaTask.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.javaTask.utils.InformMessages.ACCOUNT_DELETED_SUCCESSFULLY;

@RequestMapping("/admin")
@RestController
public class AdminController {

    private final AccountService accountService;

    public AdminController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<Account> getAccounts() {
        return accountService.getAccounts();
    }

    @PostMapping(value = "/register", consumes = {"application/json"})
    public ResponseEntity<?> registerAccount(@RequestBody AccountDto accountDto) {
        try {
            Account newAccount = accountService.registerAccount(accountDto);
            return ResponseEntity.ok(newAccount);
        } catch (Exception e) {
            return ResponseEntity
                    .status(409)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/account/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        try {
            accountService.deleteAccount(id);
            return ResponseEntity.ok(ACCOUNT_DELETED_SUCCESSFULLY);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/stats")
    public List<AccountMessageStats> getAccountStats() {
        return accountService.getAccountMessageStats();
    }

}
