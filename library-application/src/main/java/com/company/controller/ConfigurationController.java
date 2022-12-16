package com.company.controller;

import com.company.service.ConfigurationService;
import com.company.service.exceptions.ConfigurationValueIncorrectException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/configuration/")
public class ConfigurationController {
    private final ConfigurationService configurationService;

    /**
     * Method to edit value of number of maximum days which user can have borrowed book
     *
     * @param maxDaysToBorrowBook
     * @return
     */
    @PatchMapping("maxDays/")
    public ResponseEntity editMaxDaysToBorrowBook(@RequestParam Integer maxDaysToBorrowBook) throws ConfigurationValueIncorrectException {
        configurationService.editNumberOfDaysToBorrowABook(maxDaysToBorrowBook);
        return new ResponseEntity(maxDaysToBorrowBook, HttpStatus.OK);
    }

    /**
     * Method to edit maximum number of books which user can have in the same time
     *
     * @param maxNumberOfBorrowedBooks
     * @return
     */
    @PatchMapping("maxBooks/")
    public ResponseEntity editMaxNumberOfBorrowedBooks(@RequestParam Integer maxNumberOfBorrowedBooks) throws ConfigurationValueIncorrectException {
        configurationService.editNumberOfBorrowedBooks(maxNumberOfBorrowedBooks);
        return new ResponseEntity(maxNumberOfBorrowedBooks, HttpStatus.OK);
    }

    /**
     * Method to edit min age of user to borrow a book
     *
     * @param minAgeToBorrowABook
     * @return
     */
    @PatchMapping("minAge/")
    public ResponseEntity editMinAgeToBorrowABook(@RequestParam Integer minAgeToBorrowABook) throws ConfigurationValueIncorrectException {
        configurationService.editMinAgeToBorrowABook(minAgeToBorrowABook);
        return new ResponseEntity(minAgeToBorrowABook, HttpStatus.OK);
    }

    /**
     * Method to get value of min age to borrow a book
     * @return
     */
    @GetMapping("minAge/")
    public ResponseEntity getMinAgeToBorrowABook() {
        Integer minAge = configurationService.getMinAgeToBorrowABook();
        return new ResponseEntity(minAge, HttpStatus.OK);
    }

    /**
     * Method to get value of max number of books which reader can have in the same time
     * @return
     */
    @GetMapping("maxBooks/")
    public ResponseEntity getMaxNumberOfBorrowedBooks() {
        Integer maxNumberOfBorrowedBooks = configurationService.getMaxNumberOfBorrowedBooks();
        return new ResponseEntity(maxNumberOfBorrowedBooks, HttpStatus.OK);
    }

    /**
     * Method to get value of maximum days which reader can have a book
     * @return
     */
    @GetMapping("maxDays/")
    public ResponseEntity getMaxDaysToBorrowBook() {
        Integer maxDaysToBorrowBook = configurationService.getMaxNumberOfDaysToBorrowABook();
        return new ResponseEntity(maxDaysToBorrowBook, HttpStatus.OK);
    }
}
