package com.crm.controller;

import com.crm.dto.UserSettingsDTO;
import com.crm.dto.PasswordUpdateDTO;
import com.crm.service.UserSettingsService;
import com.crm.service.UserService;
import com.crm.repository.UserRepository;
import com.crm.entity.User;
import com.crm.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for Settings management operations.
 * Provides endpoints for managing user preferences and system settings.
 */
@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Settings Management", description = "APIs for managing user preferences and system settings")
public class SettingsController {

    private final UserSettingsService userSettingsService;
    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * Get user settings for the authenticated user.
     *
     * @param userDetails the authenticated user details
     * @return user settings
     */
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    @Operation(summary = "Get user settings", description = "Retrieves settings for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Settings retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserSettingsDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<UserSettingsDTO> getUserSettings(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("Fetching user settings for user: {}", userDetails.getUsername());
        
        // Extract actual user ID from userDetails
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        
        UserSettingsDTO settings = userSettingsService.getUserSettings(user.getId());
        return ResponseEntity.ok(settings);
    }

    /**
     * Update user settings for the authenticated user.
     *
     * @param settingsDTO the settings to update
     * @param userDetails the authenticated user details
     * @return updated user settings
     */
    @PutMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    @Operation(summary = "Update user settings", description = "Updates settings for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Settings updated successfully",
                    content = @Content(schema = @Schema(implementation = UserSettingsDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<UserSettingsDTO> updateUserSettings(
            @Valid @RequestBody UserSettingsDTO settingsDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("Updating user settings for user: {}", userDetails.getUsername());
        
        // Extract actual user ID from userDetails
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        
        UserSettingsDTO updatedSettings = userSettingsService.updateUserSettings(user.getId(), settingsDTO);
        return ResponseEntity.ok(updatedSettings);
    }

    /**
     * Get available themes.
     *
     * @return list of available themes
     */
    @GetMapping("/themes")
    @Operation(summary = "Get available themes", description = "Retrieves list of available UI themes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Themes retrieved successfully")
    })
    public ResponseEntity<List<Map<String, String>>> getAvailableThemes() {
        log.info("Fetching available themes");
        
        List<Map<String, String>> themes = List.of(
                Map.of("id", "light", "name", "Light", "description", "Light theme for daytime use"),
                Map.of("id", "dark", "name", "Dark", "description", "Dark theme for low-light environments"),
                Map.of("id", "auto", "name", "Auto", "description", "Automatically switch based on system preference")
        );
        
        return ResponseEntity.ok(themes);
    }

    /**
     * Get available languages.
     *
     * @return list of available languages
     */
    @GetMapping("/languages")
    @Operation(summary = "Get available languages", description = "Retrieves list of available languages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Languages retrieved successfully")
    })
    public ResponseEntity<List<Map<String, String>>> getAvailableLanguages() {
        log.info("Fetching available languages");
        
        List<Map<String, String>> languages = List.of(
                Map.of("code", "en", "name", "English", "nativeName", "English"),
                Map.of("code", "es", "name", "Spanish", "nativeName", "Español"),
                Map.of("code", "fr", "name", "French", "nativeName", "Français"),
                Map.of("code", "de", "name", "German", "nativeName", "Deutsch")
        );
        
        return ResponseEntity.ok(languages);
    }

    /**
     * Get available timezones.
     *
     * @return list of available timezones
     */
    @GetMapping("/timezones")
    @Operation(summary = "Get available timezones", description = "Retrieves list of available timezones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Timezones retrieved successfully")
    })
    public ResponseEntity<List<Map<String, String>>> getAvailableTimezones() {
        log.info("Fetching available timezones");
        
        List<Map<String, String>> timezones = List.of(
                Map.of("id", "UTC", "name", "UTC", "offset", "+00:00"),
                Map.of("id", "EST", "name", "Eastern Time", "offset", "-05:00"),
                Map.of("id", "PST", "name", "Pacific Time", "offset", "-08:00"),
                Map.of("id", "GMT", "name", "Greenwich Mean Time", "offset", "+00:00")
        );
        
        return ResponseEntity.ok(timezones);
    }

    /**
     * Get system settings (admin only).
     *
     * @return system settings
     */
    @GetMapping("/system")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get system settings", description = "Retrieves system-wide settings (admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "System settings retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Map<String, Object>> getSystemSettings() {
        log.info("Fetching system settings");
        
        Map<String, Object> systemSettings = Map.of(
                "maintenanceMode", false,
                "dataBackup", "daily",
                "apiRateLimit", 1000,
                "systemVersion", "1.0.0"
        );
        
        return ResponseEntity.ok(systemSettings);
    }

    /**
     * Update system settings (admin only).
     *
     * @param systemSettings the system settings to update
     * @return updated system settings
     */
    @PutMapping("/system")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update system settings", description = "Updates system-wide settings (admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "System settings updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Map<String, Object>> updateSystemSettings(
            @RequestBody Map<String, Object> systemSettings) {
        
        log.info("Updating system settings: {}", systemSettings);
        
        // TODO: Implement actual system settings update logic
        // For now, just return the received settings
        
        return ResponseEntity.ok(systemSettings);
    }

    /**
     * Update user password.
     *
     * @param passwordUpdateDTO the password update data
     * @param userDetails the authenticated user details
     * @return success message
     */
    @PutMapping("/password")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'SALES_REP')")
    @Operation(summary = "Update user password", description = "Updates the authenticated user's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or current password incorrect"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Map<String, String>> updatePassword(
            @Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        log.info("Updating password for user: {}", userDetails.getUsername());
        
        // Extract actual user ID from userDetails
        String username = userDetails.getUsername();
        userService.updatePassword(username, passwordUpdateDTO);
        
        return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
    }
}
