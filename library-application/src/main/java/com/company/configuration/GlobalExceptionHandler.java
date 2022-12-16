package com.company.configuration;

import com.company.service.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorNotFoundException.class)
    public final ResponseEntity handleException(AuthorNotFoundException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public final ResponseEntity handleException(BookNotFoundException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookIsCurrentlyBorrowedException.class)
    public final ResponseEntity handleException(BookIsCurrentlyBorrowedException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorWithGivenNameAndSurnameExistsException.class)
    public final ResponseEntity handleException(AuthorWithGivenNameAndSurnameExistsException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ConfigurationValueIncorrectException.class)
    public final ResponseEntity handleException(ConfigurationValueIncorrectException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ReaderNotFoundException.class)
    public final ResponseEntity handleException(ReaderNotFoundException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReaderHasCurrentlyBookOnLoanException.class)
    public final ResponseEntity handleException(ReaderHasCurrentlyBookOnLoanException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ChildWithoutParentGuardianException.class)
    public final ResponseEntity handleException(ChildWithoutParentGuardianException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ParentNotFoundException.class)
    public final ResponseEntity handleException(ParentNotFoundException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ReaderIsNotParentException.class)
    public final ResponseEntity handleException(ReaderIsNotParentException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ReaderIsNotChildException.class)
    public final ResponseEntity handleException(ReaderIsNotChildException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ReaderTooYoungException.class)
    public final ResponseEntity handleException(ReaderTooYoungException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ReaderHaveTooMuchBooksException.class)
    public final ResponseEntity handleException(ReaderHaveTooMuchBooksException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ReaderHaveBooksTooLongException.class)
    public final ResponseEntity handleException(ReaderHaveBooksTooLongException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public final ResponseEntity handleException(BookNotAvailableException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ReaderCurrentlyHaveThisBookException.class)
    public final ResponseEntity handleException(ReaderCurrentlyHaveThisBookException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(AuthorHaveBooksException.class)
    public final ResponseEntity handleException(AuthorHaveBooksException ex){
        return new ResponseEntity(ex.getMessage(),HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(LoanNotFoundException.class)
    public final ResponseEntity handleException(LoanNotFoundException ex){
        return new ResponseEntity(ex.getMessage(),HttpStatus.NOT_FOUND);
    }
}
