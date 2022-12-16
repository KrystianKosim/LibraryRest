package services;

import com.company.repository.models.entity.AuthorEntity;
import com.company.repository.models.entity.BookEntity;
import com.company.repository.models.entity.LoanEntity;
import com.company.repository.models.repository.BookRepository;
import com.company.repository.models.repository.LoansRepository;
import com.company.service.AuthorService;
import com.company.service.BookService;
import com.company.service.exceptions.AuthorNotFoundException;
import com.company.service.exceptions.BookIsCurrentlyBorrowedException;
import com.company.service.exceptions.BookNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static services.TestUtils.createAuthors;
import static services.TestUtils.createBooks;


@ExtendWith(MockitoExtension.class)
@TestExecutionListeners(MockitoTestExecutionListener.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration
@AutoConfigureMockMvc
public class BookServiceTest {

    @Mock
    private AuthorService mockAuthorService;
    @Mock
    private BookRepository mockBookRepository;
    @Mock
    private LoansRepository mockLoansRepository;
    @InjectMocks
    private BookService bookService;

    private List<BookEntity> books = new LinkedList<>();
    private List<AuthorEntity> authors = new LinkedList<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        bookService = new BookService(mockAuthorService, mockBookRepository, mockLoansRepository);
        authors = createAuthors();
        books = createBooks();
    }

    @Test
    @DisplayName("Should add book")
    void shouldAddBook() throws AuthorNotFoundException {
        //given
        BookEntity book = books.get(0);
        AuthorEntity authorEntity = authors.get(0);
        when(mockAuthorService.findAuthorById(authorEntity.getId())).thenReturn(authorEntity);
        //when
        BookEntity result = bookService.addBook(book);
        //then
        Assertions.assertEquals(book, result);
    }

    @Test
    @DisplayName("Should throw exception because author is not present while add")
    void shouldThrowExceptionBecauseAuthorIsNotPresentWhileAdd() throws AuthorNotFoundException {
        //given
        BookEntity book = books.get(0);
        book.setAuthor(null);
        //when
        Exception result = Assertions.assertThrows(AuthorNotFoundException.class,
                () -> bookService.addBook(book));
        //then
        Assertions.assertEquals(BookService.NOT_FOUND_AUTHOR, result.getMessage());
    }

    @Test
    @DisplayName("Should return edited book")
    void shouldReturnEditedBook() throws AuthorNotFoundException, BookNotFoundException {
        //given
        BookEntity bookEntity = books.get(0);
        Integer quantity = 99;
        BookEntity bookWithNewParams = new BookEntity();
        bookWithNewParams.setId(bookEntity.getId());
        bookWithNewParams.setQuantity(quantity);

        when(mockBookRepository.findById(bookEntity.getId())).thenReturn(Optional.of(bookEntity));
        //when
        BookEntity result = bookService.editBook(bookWithNewParams);
        //then
        Assertions.assertTrue(quantity.equals(result.getQuantity()));
    }

    @Test
    @DisplayName("Should throw exception because id is not present while edit")
    void shouldThrowExceptionBecauseIdIsNotPresentWhileEdit() {
        //given
        BookEntity bookEntity = books.get(0);
        Integer quantity = 99;
        BookEntity bookWithNewParams = new BookEntity();
        bookWithNewParams.setQuantity(quantity);

        when(mockBookRepository.findById(bookEntity.getId())).thenReturn(Optional.of(bookEntity));
        //when
        Exception result = Assertions.assertThrows(BookNotFoundException.class,
                () -> bookService.editBook(bookWithNewParams));
        //then
        Assertions.assertEquals(BookService.NOT_FOUND_BOOK_WITH_GIVEN_ID, result.getMessage());
    }

    @Test
    @DisplayName("Should return empty book because all values are empty")
    void shouldReturnTheSameBookBecauseAllValuesAreEmpty() throws AuthorNotFoundException, BookNotFoundException {
        //given
        BookEntity book = books.get(0);

        BookEntity bookWithNewParameters = new BookEntity();
        bookWithNewParameters.setId(book.getId());

        when(mockBookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        //when
        BookEntity result = bookService.editBook(bookWithNewParameters);
        //then
        Assertions.assertEquals(book, result);
    }

    @Test
    @DisplayName("Should return list of books with given quantity")
    void shouldReturnListOfBooksWithGivenQuantity() {
        //given
        List<BookEntity> bookEntities = books;
        BookEntity book = bookEntities.get(0);
        Integer quantity = book.getQuantity();

        when(mockBookRepository.findAll()).thenReturn(bookEntities);
        //when
        List<BookEntity> results = bookService.findBookByParameters(null, null, null, quantity, null);
        //then
        Assertions.assertEquals(List.of(book), results);
    }

    @Test
    @DisplayName("Should return all books given as parameter because all filter values are empty")
    void shouldReturnAllBooksGivenAsParameterBecauseAllFilterValuesAreEmpty() {
        //given
        List<BookEntity> bookEntities = books;

        when(mockBookRepository.findAll()).thenReturn(bookEntities);
        //when
        List<BookEntity> results = bookService.findBookByParameters(null, null, null, null, null);
        //then
        Assertions.assertEquals(bookEntities, results);
    }

    @Test
    @DisplayName("Should throw exception because id is incorrect")
    void shouldThrowExceptionBecauseIdIsIncorrect() {
        //given
        Integer id = 99;

        when(mockBookRepository.findById(id)).thenReturn(Optional.empty());
        //when
        Exception result = Assertions.assertThrows(BookNotFoundException.class,
                () -> bookService.deleteBook(id));
        //then
        Assertions.assertEquals(BookService.NOT_FOUND_BOOK_WITH_GIVEN_ID, result.getMessage());
    }

    @Test
    @DisplayName("Should throw exception while delete because book is currently borrowed")
    void shouldThrowExceptionWhileDeleteBecauseBookIsCurrentlyBorrowed() {
        //given
        BookEntity bookEntity = books.get(0);
        Integer id = bookEntity.getId();

        when(mockBookRepository.findById(id)).thenReturn(Optional.of(bookEntity));
        when(mockLoansRepository.findAllByBookAndReturnedDate(bookEntity, null)).thenReturn(List.of(new LoanEntity()));
        //when
        Exception result = Assertions.assertThrows(BookIsCurrentlyBorrowedException.class,
                () -> bookService.deleteBook(id));
        //then
        Assertions.assertEquals(BookService.BOOK_IS_CURRENTLY_BORROWED, result.getMessage());
    }
}
