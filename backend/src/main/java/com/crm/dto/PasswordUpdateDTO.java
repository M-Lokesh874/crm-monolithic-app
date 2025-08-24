package com.crm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating user passwords.
 * Contains current password, new password, and confirmation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data for updating user password")
public class PasswordUpdateDTO {

    @NotBlank(message = "Current password is required")
    @Schema(description = "Current password", example = "currentPassword123")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 100, message = "New password must be between 8 and 100 characters")
    @Schema(description = "New password", example = "newPassword123")
    private String newPassword;

    @NotBlank(message = "Password confirmation is required")
    @Schema(description = "Password confirmation", example = "newPassword123")
    private String confirmPassword;
}
