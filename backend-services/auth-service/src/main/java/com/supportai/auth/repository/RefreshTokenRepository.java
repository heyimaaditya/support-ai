package com.supportai.auth.repository;

import com.supportai.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.supportai.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.Modifying;
import java.time.Instant;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    
    @Modifying
    void deleteByUser(User user);

    @Modifying
    void deleteByExpiryDateBefore(Instant now);
}