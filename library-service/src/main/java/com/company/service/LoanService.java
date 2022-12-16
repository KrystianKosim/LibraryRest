package com.company.service;

import com.company.repository.models.entity.BookEntity;
import com.company.repository.models.entity.LoanEntity;
import com.company.repository.models.entity.ReaderEntity;
import com.company.repository.models.repository.LoansRepository;
import com.company.service.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


@RequiredArgsConstructor
@Service
public class LoanService {
    public static final String NOT_FOUND_LOAN_FOR_GIVEN_BOOK_AND_AUTHOR = "Not found loan for given book and author";
    private final LoansRepository loansRepository;
    private final ReaderService readerService;
    private final BookService bookService;

    public static final String BOOK_IS_CURRENTLY_BORROWED = "Book is currently borrowed ";
    public static final String BOOK_IS_NOT_AVAILABLE = "Book is not available";


    /**
     * Method to set return date of loan and return book to library
     *
     * @param readerId, reader who want to return book
     * @param bookId,   book which reader want to return
     * @return
     * @throws ReaderNotFoundException, if given reader id is incorrect
     * @throws BookNotFoundException,   if given book id is incorrect
     * @throws LoanNotFoundException,   if reader didn't borrow book
     */
    public LoanEntity returnBook(Integer readerId, Integer bookId) throws ReaderNotFoundException, BookNotFoundException, LoanNotFoundException {
        ReaderEntity reader = readerService.findReaderById(readerId);
        BookEntity book = bookService.findBookById(bookId);
        List<LoanEntity> existedLoan = loansRepository.findAllByReaderAndBookAndReturnedDate(reader, book, null);
        if (existedLoan.size() != 1) {
            throw new LoanNotFoundException(NOT_FOUND_LOAN_FOR_GIVEN_BOOK_AND_AUTHOR);
        }
        LoanEntity loan = existedLoan.get(0);
        LocalDate returnDate = LocalDate.now();
        loan.setReturnedDate(Date.valueOf(returnDate));
        loansRepository.save(loan);
        return loan;
    }

    /**
     * Method to get all loans from database
     *
     * @return
     */
    public List<LoanEntity> findAll() {
        return loansRepository.findAll();
    }

    /**
     * Method to find all loans for given reader
     *
     * @param readerId, for whom the loan is to be returned
     * @return
     * @throws ReaderNotFoundException, if given reader doesn't exist
     */
    public List<LoanEntity> findLoansByReader(Integer readerId) throws ReaderNotFoundException {
        ReaderEntity reader = readerService.findReaderById(readerId);
        return loansRepository.findAllByReader(reader);
    }

    /**
     * Method to find all loans for given book
     *
     * @param bookId, for which loans are to be returned
     * @return
     * @throws BookNotFoundException, if given book doesn't exist
     */
    public List<LoanEntity> findLoansByBookId(Integer bookId) throws BookNotFoundException {
        BookEntity book = bookService.findBookById(bookId);
        return loansRepository.findAllByBook(book);
    }

    /**
     * Method to borrow a single book
     *
     * @param bookId,   book which should be borrowed
     * @param readerId, who want to borrow a book
     * @return
     * @throws ReaderNotFoundException,              if given reader doens't exist
     * @throws BookNotFoundException,                if given book doesn't exist
     * @throws ReaderTooYoungException,              if reader can't borrow a book because is too young
     * @throws ReaderHaveBooksTooLongException,      if reader can't borrow a book because have any book too long
     * @throws ReaderHaveTooMuchBooksException,      if reader can't borrow a book because have currently borrowed too many books
     * @throws BookNotAvailableException,            if book doesn't available in library
     * @throws ReaderCurrentlyHaveThisBookException, if reader have currently borrowed given book
     */
    public LoanEntity borrowBook(Integer bookId, Integer readerId) throws ReaderNotFoundException, BookNotFoundException,
            ReaderTooYoungException, ReaderHaveBooksTooLongException, ReaderHaveTooMuchBooksException, BookNotAvailableException, ReaderCurrentlyHaveThisBookException {
        ReaderEntity reader = readerService.findReaderById(readerId);
        BookEntity book = bookService.findBookById(bookId);

        readerService.isReaderCanBorrowABook(readerId);
        boolean isReaderHasBook = isReaderHasABook(reader, book);
        if (isReaderHasBook) {
            throw new ReaderCurrentlyHaveThisBookException(BOOK_IS_CURRENTLY_BORROWED);
        }
        if (book.getQuantityAvailable() <= 0) {
            throw new BookNotAvailableException(BOOK_IS_NOT_AVAILABLE);
        }

        return addNewLoanIntoTable(book, reader);
    }

    private boolean isReaderHasABook(ReaderEntity reader, BookEntity book) {
        return !loansRepository.findAllByReaderAndBookAndReturnedDate(reader, book, null).isEmpty();
    }

    private LoanEntity addNewLoanIntoTable(BookEntity book, ReaderEntity reader) {
        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setBook(book);
        loanEntity.setReader(reader);
        loanEntity.setBorrowDate(Date.valueOf(LocalDate.now()));
        loansRepository.save(loanEntity);
        return loanEntity;
    }

}
