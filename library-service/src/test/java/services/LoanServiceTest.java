package services;

import com.company.repository.models.entity.BookEntity;
import com.company.repository.models.entity.LoanEntity;
import com.company.repository.models.entity.ReaderEntity;
import com.company.repository.models.repository.LoansRepository;
import com.company.service.BookService;
import com.company.service.LoanService;
import com.company.service.ReaderService;
import com.company.service.exceptions.*;
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

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static services.TestUtils.*;

@ExtendWith(MockitoExtension.class)
@TestExecutionListeners(MockitoTestExecutionListener.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration
@AutoConfigureMockMvc
public class LoanServiceTest {

    @Mock
    private LoansRepository mockLoansRepository;
    @Mock
    private ReaderService mockReaderService;
    @Mock
    private BookService mockBookService;
    @InjectMocks
    private LoanService loanService;

    private List<ReaderEntity> readers;
    private List<BookEntity> books;
    private List<LoanEntity> loans;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        loanService = new LoanService(mockLoansRepository, mockReaderService, mockBookService);
        readers = createReaders();
        books = createBooks();
        loans = createLoans();
    }

    @Test
    @DisplayName("Should throw exception because not found loan to return book")
    void shouldThrowExceptionBecauseNotFoundLoanToReturnBook() throws ReaderNotFoundException, BookNotFoundException {
        //given
        ReaderEntity reader = readers.get(0);
        Integer readerId = reader.getId();
        BookEntity book = books.get(0);
        Integer bookId = book.getId();

        when(mockReaderService.findReaderById(readerId)).thenReturn(reader);
        when(mockBookService.findBookById(bookId)).thenReturn(book);
        when(mockLoansRepository.findAllByReaderAndBookAndReturnedDate(reader, book, null)).thenReturn(List.of());
        //when
        Exception result = Assertions.assertThrows(LoanNotFoundException.class,
                () -> loanService.returnBook(readerId, bookId));
        //then
        Assertions.assertEquals(LoanService.NOT_FOUND_LOAN_FOR_GIVEN_BOOK_AND_AUTHOR, result.getMessage());
    }

    @Test
    @DisplayName("Should return book")
    void shouldReturnBook() throws ReaderNotFoundException, BookNotFoundException, LoanNotFoundException {
        //given
        ReaderEntity reader = readers.get(0);
        Integer readerId = reader.getId();
        BookEntity book = books.get(0);
        Integer bookId = book.getId();
        LoanEntity loan = new LoanEntity();
        loan.setReader(reader);
        loan.setBook(book);
        loan.setBorrowDate(Date.valueOf(LocalDate.now().minusDays(10)));

        when(mockReaderService.findReaderById(readerId)).thenReturn(reader);
        when(mockBookService.findBookById(bookId)).thenReturn(book);
        when(mockLoansRepository.findAllByReaderAndBookAndReturnedDate(reader, book, null)).thenReturn(List.of(loan));
        //when
        LoanEntity result = loanService.returnBook(readerId, bookId);
        //then
        Assertions.assertEquals(loan.getBook(), result.getBook());
        Assertions.assertEquals(loan.getReader(), result.getReader());
        Assertions.assertEquals(loan.getBorrowDate(), result.getBorrowDate());
        Assertions.assertNotNull(result.getReturnedDate());
    }

    @Test
    @DisplayName("Should throw exception because reader currently have book")
    void shouldThrowExceptionBecauseReaderCurrentlyHaveBook() throws ReaderNotFoundException, BookNotFoundException {
        //given
        ReaderEntity reader = readers.get(0);
        Integer readerId = reader.getId();
        BookEntity book = books.get(0);
        Integer bookId = book.getId();
        LoanEntity loan = new LoanEntity();
        loan.setReader(reader);
        loan.setBook(book);
        loan.setBorrowDate(Date.valueOf(LocalDate.now().minusDays(10)));

        when(mockReaderService.findReaderById(readerId)).thenReturn(reader);
        when(mockBookService.findBookById(bookId)).thenReturn(book);
        when(mockLoansRepository.findAllByReaderAndBookAndReturnedDate(reader, book, null)).thenReturn(List.of(loan));
        //when
        Exception result = Assertions.assertThrows(ReaderCurrentlyHaveThisBookException.class,
                () -> loanService.borrowBook(bookId, readerId));
        //then
        Assertions.assertEquals(LoanService.BOOK_IS_CURRENTLY_BORROWED, result.getMessage());
    }

    @Test
    @DisplayName("Should throw exception because book is not available")
    void shouldThrowExceptionBecauseBookIsNotAvailable() throws ReaderNotFoundException, BookNotFoundException {
        //given
        ReaderEntity reader = readers.get(0);
        Integer readerId = reader.getId();
        BookEntity book = books.get(0);
        book.setQuantityAvailable(0);
        Integer bookId = book.getId();
        LoanEntity loan = new LoanEntity();
        loan.setReader(reader);
        loan.setBook(book);
        loan.setBorrowDate(Date.valueOf(LocalDate.now().minusDays(10)));

        when(mockReaderService.findReaderById(readerId)).thenReturn(reader);
        when(mockBookService.findBookById(bookId)).thenReturn(book);
        when(mockLoansRepository.findAllByReaderAndBookAndReturnedDate(reader, book, null)).thenReturn(List.of());
        //when
        Exception result = Assertions.assertThrows(BookNotAvailableException.class,
                () -> loanService.borrowBook(bookId, readerId));
        //then
        Assertions.assertEquals(LoanService.BOOK_IS_NOT_AVAILABLE, result.getMessage());
    }

    @Test
    @DisplayName("Reader should borrow a book")
    void readerShouldBorrowBook() throws ReaderNotFoundException, BookNotFoundException, ReaderTooYoungException, ReaderHaveBooksTooLongException, ReaderHaveTooMuchBooksException, ReaderCurrentlyHaveThisBookException, BookNotAvailableException {
        //given
        ReaderEntity reader = readers.get(0);
        Integer readerId = reader.getId();
        BookEntity book = books.get(0);
        book.setQuantityAvailable(2);
        Integer bookId = book.getId();
        LoanEntity loan = new LoanEntity();
        loan.setReader(reader);
        loan.setBook(book);
        loan.setBorrowDate(Date.valueOf(LocalDate.now().minusDays(10)));

        when(mockReaderService.findReaderById(readerId)).thenReturn(reader);
        when(mockBookService.findBookById(bookId)).thenReturn(book);
        when(mockLoansRepository.findAllByReaderAndBookAndReturnedDate(reader, book, null)).thenReturn(List.of());
        //when
        LoanEntity loanEntity = loanService.borrowBook(bookId, readerId);
        //then
        Assertions.assertEquals(reader, loanEntity.getReader());
        Assertions.assertEquals(book, loanEntity.getBook());
    }

    @Test
    @DisplayName("Should return loans of reader")
    void shouldReturnLoansOfReader() throws ReaderNotFoundException {
        //given
        ReaderEntity reader = readers.get(0);
        Integer readerId = reader.getId();
        BookEntity book = books.get(0);
        LoanEntity loan = new LoanEntity();
        loan.setReader(reader);
        loan.setBook(book);
        loan.setBorrowDate(Date.valueOf(LocalDate.now().minusDays(10)));
        when(mockReaderService.findReaderById(readerId)).thenReturn(reader);
        when(mockLoansRepository.findAllByReader(reader)).thenReturn(List.of(loan));
        //when
        List<LoanEntity> result = loanService.findLoansByReader(readerId);
        //then
        Assertions.assertEquals(List.of(loan), result);
    }

    @Test
    @DisplayName("Should return loans of given book")
    void shouldReturnLoansOfGivenBook() throws BookNotFoundException {
        //given
        ReaderEntity reader = readers.get(0);
        BookEntity book = books.get(0);
        Integer bookId = book.getId();
        LoanEntity loan = new LoanEntity();
        loan.setReader(reader);
        loan.setBook(book);
        loan.setBorrowDate(Date.valueOf(LocalDate.now().minusDays(10)));
        when(mockBookService.findBookById(bookId)).thenReturn(book);
        when(mockLoansRepository.findAllByBook(book)).thenReturn(List.of(loan));
        //when
        List<LoanEntity> result = loanService.findLoansByBookId(bookId);
        //then
        Assertions.assertEquals(List.of(loan), result);
    }
}
