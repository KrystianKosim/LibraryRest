package com.company.service.exceptions;

public class BookNotAvailableException extends Exception{
    public BookNotAvailableException(String message){
        super(message);
    }
}
