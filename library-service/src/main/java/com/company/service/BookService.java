package com.company.service;

import com.company.repository.models.entity.AuthorEntity;
import com.company.repository.models.entity.BookEntity;
import com.company.repository.models.entity.LoanEntity;
import com.company.repository.models.repository.BookRepository;
import com.company.repository.models.repository.LoansRepository;
import com.company.service.exceptions.AuthorNotFoundException;
import com.company.service.exceptions.BookIsCurrentlyBorrowedException;
import com.company.service.exceptions.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookService {

    private final AuthorService authorService;
    private final BookRepository bookRepository;
    private final LoansRepository loansRepository;

    public static final String NOT_FOUND_BOOK_WITH_GIVEN_ID = "Not found book with given id!";
    public static final String NOT_FOUND_AUTHOR = "Not found author!";
    public static final String BOOK_IS_CURRENTLY_BORROWED = "Book is currently borrowed ";


    /**
     * Method to add given book to database
     *
     * @param book
     * @return
     * @throws AuthorNotFoundException, if given author of book doesn't exist
     */
    public BookEntity addBook(BookEntity book) throws AuthorNotFoundException {
        Optional.ofNullable(book.getAuthor()).orElseThrow(() -> new AuthorNotFoundException(NOT_FOUND_AUTHOR));
        authorService.findAuthorById(book.getAuthor().getId());
        book.setId(null);
        bookRepository.save(book);
        return book;
    }

    /**
     * Method to find single book by given id
     *
     * @param id
     * @return
     * @throws BookNotFoundException, if book doesn't find
     */
    public BookEntity findBookById(Integer id) throws BookNotFoundException {
        Optional<BookEntity> bookEntity = bookRepository.findById(id);
        if (bookEntity.isEmpty()) {
            throw new BookNotFoundException(NOT_FOUND_BOOK_WITH_GIVEN_ID);
        }
        return bookEntity.get();
    }

    /**
     * Method to check if book is currently borrowed, remove book from book table, and loan table
     *
     * @param bookId
     * @throws BookNotFoundException,            if book with given id doesn't exists
     * @throws BookIsCurrentlyBorrowedException, if any reader currently borrowed a book
     */
    @Transactional
    public void deleteBook(Integer bookId) throws BookNotFoundException, BookIsCurrentlyBorrowedException {
        BookEntity bookEntity = findBookById(bookId);
        isBookCurrentlyBorrowed(bookEntity);
        loansRepository.deleteAllByBook(bookEntity);
        bookRepository.deleteById(bookId);
    }

    /**
     * Method to get all books from database
     *
     * @return
     */
    public List<BookEntity> findAllBooks() {
        return bookRepository.findAll();
    }


    /**
     * Method to edit book
     *
     * @param bookWithNewParameters, body with id of book which should be edited, and values which should be edited
     * @return
     * @throws BookNotFoundException,   if given book id is incorrect
     * @throws AuthorNotFoundException, if given author is incorrect
     */
    public BookEntity editBook(BookEntity bookWithNewParameters) throws BookNotFoundException, AuthorNotFoundException {
        Optional.ofNullable(bookWithNewParameters.getId()).orElseThrow(
                () -> new BookNotFoundException(NOT_FOUND_BOOK_WITH_GIVEN_ID));
        BookEntity bookEntity = findBookById(bookWithNewParameters.getId());
        AuthorEntity author = null;
        if (Optional.ofNullable(bookWithNewParameters.getAuthor()).isPresent()) {
            author = authorService.findAuthorById(bookWithNewParameters.getAuthor().getId());
        }
        AuthorEntity finalAuthor = author;

        Optional.ofNullable(bookWithNewParameters.getTitle()).ifPresent(bookEntity::setTitle);
        Optional.ofNullable(bookWithNewParameters.getAuthor()).ifPresent(authorId -> {
            bookEntity.setAuthor(finalAuthor);
        });
        Optional.ofNullable(bookWithNewParameters.getQuantity()).ifPresent(bookEntity::setQuantity);
        Optional.ofNullable(bookWithNewParameters.getQuantityAvailable()).ifPresent(bookEntity::setQuantityAvailable);

        bookRepository.save(bookEntity);
        return bookEntity;
    }


    /**
     * Method to check which parameters are present, and find books with given parameters
     *
     * @param id
     * @param title
     * @param authorId
     * @param quantity
     * @param quantityAvailable
     * @return
     */
    public List<BookEntity> findBookByParameters(Integer id, String title, Integer authorId, Integer quantity, Integer quantityAvailable) {
        List<BookEntity> books = findAllBooks();
        return books.stream()
                .filter(book -> Optional.ofNullable(id).isPresent() ? book.getId().equals(id) : true)
                .filter(book -> Optional.ofNullable(title).isPresent() ? book.getTitle().equals(title) : true)
                .filter(book -> Optional.ofNullable(authorId).isPresent() ? book.getAuthor().equals(authorId) : true)
                .filter(book -> Optional.ofNullable(quantity).isPresent() ? book.getQuantity().equals(quantity) : true)
                .filter(book -> Optional.ofNullable(quantityAvailable).isPresent() ? book.getQuantityAvailable().equals(quantityAvailable) : true)
                .collect(Collectors.toList());
    }

    private void isBookCurrentlyBorrowed(BookEntity book) throws BookIsCurrentlyBorrowedException {
        List<LoanEntity> loanEntities = loansRepository.findAllByBookAndReturnedDate(book, null);
        if (!loanEntities.isEmpty()) {
            throw new BookIsCurrentlyBorrowedException(BOOK_IS_CURRENTLY_BORROWED);
        }
    }
}
