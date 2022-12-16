package com.company.service.exceptions;

public class ReaderHasCurrentlyBookOnLoanException extends Exception{
    public ReaderHasCurrentlyBookOnLoanException(String message){
        super(message);
    }
}
