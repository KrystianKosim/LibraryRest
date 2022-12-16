package com.company.service.exceptions;

import lombok.Getter;

@Getter
public class BookIsCurrentlyBorrowedException extends Exception {

    public BookIsCurrentlyBorrowedException(String message) {
        super(message);
    }
}
