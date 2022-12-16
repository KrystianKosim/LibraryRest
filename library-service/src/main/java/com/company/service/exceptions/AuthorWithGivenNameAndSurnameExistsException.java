package com.company.service.exceptions;

public class AuthorWithGivenNameAndSurnameExistsException extends Exception{

    public AuthorWithGivenNameAndSurnameExistsException(String message){
        super(message);
    }
}
