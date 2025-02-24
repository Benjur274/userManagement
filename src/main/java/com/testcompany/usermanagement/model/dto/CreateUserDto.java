package com.testcompany.usermanagement.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserDto {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters long")
    @Schema(description = "The user's full name", example = "John Doe")
    private String name;

    @NotBlank(message = "Username is mandatory")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters long")
    @Schema(description = "A unique username for login", example = "johndoe123")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "Password must be at least 8 characters long and contain at least one letter and one number")
    @Schema(
            description = "Password must be at least 8 characters, contain a letter and a number",
            example = "Password1"
    )
    private String password;
}
