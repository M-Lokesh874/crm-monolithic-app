package com.crm.repository;

import com.crm.entity.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for UserSettings entity.
 * Provides data access methods for user settings and preferences.
 */
@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {

    /**
     * Find user settings by user ID.
     *
     * @param userId the user ID
     * @return optional containing user settings if found
     */
    Optional<UserSettings> findByUserId(Long userId);

    /**
     * Check if user settings exist for a given user ID.
     *
     * @param userId the user ID
     * @return true if settings exist, false otherwise
     */
    boolean existsByUserId(Long userId);
}
