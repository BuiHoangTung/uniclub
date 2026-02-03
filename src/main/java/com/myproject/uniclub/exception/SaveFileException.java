package com.myproject.uniclub.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SaveFileException extends RuntimeException {
    private String message;
}
