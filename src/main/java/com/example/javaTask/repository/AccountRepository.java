package com.example.javaTask.repository;

import com.example.javaTask.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT * FROM account WHERE name = :name", nativeQuery = true)
    Optional<Account> findByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(
            value = "INSERT INTO account (name, role) VALUES (:name, :role)",
            nativeQuery = true
    )
    int insertAccountNative(@Param("name") String name, @Param("role") String role);

    @Modifying
    @Transactional
    @Query(value = "UPDATE account SET name = 'Anonymous' WHERE id = :accountId", nativeQuery = true)
    void deleteAccount(@Param("accountId") Long accountId);

    @Query(value = """
            SELECT 
                a.name,
                COUNT(m.account_id) AS message_count,
                MIN(m.timestamp) AS first_message_timestamp,
                MAX(m.timestamp) AS last_message_timestamp,
                CAST(AVG(LENGTH(m.content)) AS INT) AS avg_message_length,
                (
                    SELECT m2.content
                    FROM MESSAGE m2
                    WHERE m2.account_id = a.id
                    ORDER BY m2.timestamp DESC
                    LIMIT 1
                ) AS last_message_text
            FROM ACCOUNT a
            LEFT JOIN MESSAGE m ON m.account_id = a.id
            GROUP BY a.name
            """, nativeQuery = true)
    List<Object[]> findAccountMessageStatsRaw();
}
