package com.company.controller;

import com.company.dtos.LoanDto;
import com.company.repository.models.entity.LoanEntity;
import com.company.service.LoanService;
import com.company.service.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/loans/")
@RequiredArgsConstructor
public class LoansController {

    private final LoanService loanService;
    private final ModelMapper modelMapper;

    /**
     * Method to get all loans from library
     *
     * @return
     */
    @GetMapping("all/")
    public ResponseEntity findAll() {
        List<LoanDto> loans = loanService.findAll()
                .stream()
                .map(loan -> modelMapper.map(loan, LoanDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity(loans, HttpStatus.OK);
    }

    /**
     * Method to display loans history of loans for given reader
     *
     * @param readerId
     */
    @GetMapping("reader/{id}")
    public ResponseEntity getLoansForGivenReader(@PathVariable Integer readerId) throws ReaderNotFoundException {
        List<LoanDto> loans = loanService.findLoansByReader(readerId)
                .stream()
                .map(loan -> modelMapper.map(loan, LoanDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity(loans, HttpStatus.OK);
    }

    /**
     * Method to get all loans for book with given id
     *
     * @param bookId
     * @return
     */
    @GetMapping("book/{id}")
    public ResponseEntity getLoansForGivenBook(@PathVariable Integer bookId) throws BookNotFoundException {
        List<LoanDto> loans = loanService.findLoansByBookId(bookId)
                .stream()
                .map(loan -> modelMapper.map(loan, LoanDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity(loans, HttpStatus.OK);
    }

    /**
     * Method to borrow a book
     *
     * @param readerId, reader who want to borrow a book
     * @param bookId,   book which should be borrowed
     * @return
     */
    @PatchMapping("/borrowBook/")
    public ResponseEntity borrowBook(@RequestParam Integer readerId,
                                     @RequestParam Integer bookId) throws ReaderNotFoundException, ReaderTooYoungException, ReaderHaveBooksTooLongException, BookNotFoundException, ReaderHaveTooMuchBooksException, BookNotAvailableException, ReaderCurrentlyHaveThisBookException {
        LoanEntity loan = loanService.borrowBook(bookId, readerId);
        return new ResponseEntity(modelMapper.map(loan, LoanDto.class), HttpStatus.OK);
    }

    /**
     * Method to return a book
     *
     * @param readerId, the id of reader who borrowed the book
     * @param bookId,   the id of book which is on loan
     */
    @PatchMapping("returnBook/")
    public ResponseEntity returnBook(Integer readerId, Integer bookId) throws ReaderNotFoundException, BookNotFoundException, LoanNotFoundException {
        LoanEntity loan = loanService.returnBook(readerId, bookId);
        return new ResponseEntity(modelMapper.map(loan, LoanDto.class), HttpStatus.OK);
    }
}
