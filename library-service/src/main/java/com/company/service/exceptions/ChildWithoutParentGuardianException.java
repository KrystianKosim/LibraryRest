package com.company.service.exceptions;

public class ChildWithoutParentGuardianException extends Exception{
    public ChildWithoutParentGuardianException(String message){
        super(message);
    }
}
