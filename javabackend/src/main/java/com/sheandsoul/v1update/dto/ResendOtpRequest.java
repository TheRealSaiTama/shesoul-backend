package com.sheandsoul.v1update.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResendOtpRequest(
    @NotBlank @Email String email
) {

}
