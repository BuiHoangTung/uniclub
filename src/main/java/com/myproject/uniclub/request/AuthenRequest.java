package com.myproject.uniclub.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AuthenRequest(
        @NotNull(message = "Email không được phép null")
        @NotEmpty(message = "Email không được phép rỗng")
        @Email(message = "Email không đúng định dạng")
        String email,

        @NotNull(message = "Password không được phép null")
        @NotEmpty(message = "Password không được phép rỗng")
        String password
) {
}
