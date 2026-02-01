package com.myproject.uniclub.request;

public record AuthenRequest(
        String email,
        String password
) {
}
