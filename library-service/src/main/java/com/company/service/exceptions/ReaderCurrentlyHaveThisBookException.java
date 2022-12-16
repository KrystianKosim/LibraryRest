package com.company.service.exceptions;

public class ReaderCurrentlyHaveThisBookException extends Exception{
    public ReaderCurrentlyHaveThisBookException(String message){
        super(message);
    }
}
