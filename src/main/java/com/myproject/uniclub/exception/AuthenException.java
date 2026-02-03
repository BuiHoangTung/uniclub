package com.myproject.uniclub.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthenException extends RuntimeException{
    private String message;
}
