package com.testcompany.usermanagement.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDto {
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Schema(description = "The user's full name", example = "John Doe")
    private String name;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "Password must be at least 8 characters long and contain at least one letter and one number")
    @Schema(
            description = "Password must be at least 8 characters, contain a letter and a number",
            example = "Password1"
    )
    private String password;
}
