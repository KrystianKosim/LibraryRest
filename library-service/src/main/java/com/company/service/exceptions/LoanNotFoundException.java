package com.company.service.exceptions;

public class LoanNotFoundException extends Exception{
    public LoanNotFoundException(String message){
        super(message);
    }
}
