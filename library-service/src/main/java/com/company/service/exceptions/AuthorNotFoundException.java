package com.company.service.exceptions;

import lombok.Getter;

@Getter
public class AuthorNotFoundException extends Exception{

    public AuthorNotFoundException(String message){
        super(message);
    }
}
