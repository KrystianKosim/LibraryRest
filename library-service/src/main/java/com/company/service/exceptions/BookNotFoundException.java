package com.company.service.exceptions;

import lombok.Getter;

@Getter
public class BookNotFoundException extends Exception{

    public BookNotFoundException(String message){
        super(message);
    }
}
