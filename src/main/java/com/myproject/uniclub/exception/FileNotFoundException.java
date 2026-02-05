package com.myproject.uniclub.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileNotFoundException extends RuntimeException {
    private String message = "Could not read the file!";
}
