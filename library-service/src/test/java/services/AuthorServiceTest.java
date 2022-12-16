package services;

import com.company.repository.models.entity.AuthorEntity;
import com.company.repository.models.entity.BookEntity;
import com.company.repository.models.repository.AuthorRepository;
import com.company.repository.models.repository.BookRepository;
import com.company.service.AuthorService;
import com.company.service.exceptions.AuthorHaveBooksException;
import com.company.service.exceptions.AuthorNotFoundException;
import com.company.service.exceptions.AuthorWithGivenNameAndSurnameExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static services.TestUtils.createAuthors;
import static services.TestUtils.createBooks;


@ExtendWith(MockitoExtension.class)
@TestExecutionListeners(MockitoTestExecutionListener.class)
@ContextConfiguration
@AutoConfigureMockMvc
public class AuthorServiceTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    private AuthorRepository mockAuthorRepository;
    @Mock
    private BookRepository mockBookRepository;

    @InjectMocks
    private AuthorService authorService;

    private List<AuthorEntity> authorEntityList;
    private List<BookEntity> bookEntityList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authorService = new AuthorService(mockAuthorRepository, mockBookRepository);
        authorEntityList = createAuthors();
        bookEntityList = createBooks();
    }

    @Test
    @DisplayName("Should add author")
    void shouldAddAuthor() throws AuthorWithGivenNameAndSurnameExistsException {
        //given
        AuthorEntity author = authorEntityList.get(0);
        //when
        when(mockAuthorRepository.findAll()).thenReturn(List.of(authorEntityList.get(1)));
        AuthorEntity result = authorService.addAuthor(author);

        //then
        Assertions.assertEquals(author, result);
    }

    @Test
    @DisplayName("Should throw exception because author with given data already exists")
    void shouldThrowExceptionBecauseAuthorWithGivenDataAlreadyExists() throws AuthorWithGivenNameAndSurnameExistsException {
        //given
        AuthorEntity author = authorEntityList.get(0);
        //when
        when(mockAuthorRepository.findAll()).thenReturn(authorEntityList);
        Exception ex = Assertions.assertThrows(AuthorWithGivenNameAndSurnameExistsException.class,
                () -> {
                    authorService.addAuthor(author);
                });
        //then
        Assertions.assertEquals(AuthorService.AUTHOR_WITH_GIVEN_DATA_ALREADY_EXISTS, ex.getMessage());
    }


    @Test
    @DisplayName("Should return authors with given parameters")
    void shouldReturnAuthorsWithGivenParameters() {
        List<AuthorEntity> authors = authorEntityList;
        AuthorEntity author = authors.get(0);

        Integer id = null;
        String name = author.getName();
        String surname = author.getSurname();

        List<AuthorEntity> expectedResult = List.of(authors.get(0));

        when(mockAuthorRepository.findAll()).thenReturn(authors);

        List<AuthorEntity> result = authorService.findAuthors(id, name, surname);

        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    @DisplayName("Should return empty list because couldn't find any author with given parameters")
    void shouldReturnEmptyListBecauseCouldntFindAnyAuthorWithGivenParameters() {
        List<AuthorEntity> authors = authorEntityList;
        AuthorEntity author = authors.get(0);

        when(mockAuthorRepository.findAll()).thenReturn(authors);

        Integer id = 99;
        String name = author.getName();
        String surname = author.getSurname();

        List<AuthorEntity> result = authorService.findAuthors(id, name, surname);

        Assertions.assertEquals(List.of(), result);
    }

    @Test
    @DisplayName("Should return all authors because all of given parameters are incorrect")
    void shouldReturnAllAuthorsBecauseAllOfGivenParametersAreIncorrect() {
        Integer id = null;
        String name = null;
        String surname = null;

        List<AuthorEntity> authors = authorEntityList;

        when(mockAuthorRepository.findAll()).thenReturn(authors);

        List<AuthorEntity> result = authorService.findAuthors(id, name, surname);

        Assertions.assertEquals(authors, result);
    }

    @Test
    @DisplayName("Should edit author name")
    void shouldReturnAuthorName() throws AuthorNotFoundException, AuthorWithGivenNameAndSurnameExistsException {
        //given
        AuthorEntity author = authorEntityList.get(0);

        String name = "New Name for author";
        AuthorEntity authorWithNewValues = new AuthorEntity();
        authorWithNewValues.setId(author.getId());
        authorWithNewValues.setName(name);
        when(mockAuthorRepository.findById(authorWithNewValues.getId())).thenReturn(Optional.of(author));
        when(mockAuthorRepository.findAll()).thenReturn(List.of(authorEntityList.get(1)));
        //when
        AuthorEntity result = authorService.editAuthor(authorWithNewValues);
        //then
        Assertions.assertEquals(author, result);
    }

    @Test
    @DisplayName("Should edit author name and surname")
    void shouldEditAuthorNameAndSurname() throws AuthorNotFoundException, AuthorWithGivenNameAndSurnameExistsException {
        //given
        AuthorEntity author = authorEntityList.get(0);

        String name = "New Name for author";
        String surname = "new surname";
        AuthorEntity authorWithNewValues = new AuthorEntity();
        authorWithNewValues.setId(author.getId());
        authorWithNewValues.setName(name);
        authorWithNewValues.setSurname(surname);
        when(mockAuthorRepository.findById(authorWithNewValues.getId())).thenReturn(Optional.of(author));
        when(mockAuthorRepository.findAll()).thenReturn(List.of(authorEntityList.get(1)));
        //when
        AuthorEntity result = authorService.editAuthor(authorWithNewValues);
        //then
        Assertions.assertEquals(author, result);
    }

    @Test
    @DisplayName("Should throw exception because author id is not present while edit")
    void shouldThrowExceptionBecauseAuthorIdIsNotPresent() {
        //given
        String name = "New Name for author";
        AuthorEntity authorWithNewValues = new AuthorEntity();
        authorWithNewValues.setName(name);
        //when
        Exception result = Assertions.assertThrows(AuthorNotFoundException.class,
                () -> authorService.editAuthor(authorWithNewValues));
        //then
        Assertions.assertEquals(AuthorService.NOT_FOUND_AUTHOR, result.getMessage());
    }

    @Test
    @DisplayName("Should throw exception because given id is incorrect while edit")
    void shouldThrowExceptionBecauseGivenIdIsIncorrectWhileEdit() throws AuthorNotFoundException, AuthorWithGivenNameAndSurnameExistsException {
        //given
        AuthorEntity author = authorEntityList.get(0);

        String name = "New Name for author";
        String surname = "new surname";
        AuthorEntity authorWithNewValues = new AuthorEntity();
        authorWithNewValues.setId(author.getId());
        authorWithNewValues.setName(name);
        authorWithNewValues.setSurname(surname);
        when(mockAuthorRepository.findById(authorWithNewValues.getId())).thenReturn(Optional.empty());
        //when
        Exception result = Assertions.assertThrows(AuthorNotFoundException.class,
                () -> authorService.editAuthor(authorWithNewValues));
        //then
        Assertions.assertEquals(AuthorService.NOT_FOUND_AUTHOR, result.getMessage());
    }

    @Test
    @DisplayName("Should throw exception because author with given name and surname exists while edit")
    void shouldThrowExceptionBecauseAuthorWithGivenNameAndSurnameExistsWhileEdit() throws AuthorNotFoundException, AuthorWithGivenNameAndSurnameExistsException {
        //given
        AuthorEntity authorToEdit = authorEntityList.get(0);
        AuthorEntity author = authorEntityList.get(1);
        String name = author.getName();
        String surname = author.getSurname();
        AuthorEntity authorWithNewValues = new AuthorEntity();
        authorWithNewValues.setId(authorToEdit.getId());
        authorWithNewValues.setName(name);
        authorWithNewValues.setSurname(surname);
        when(mockAuthorRepository.findById(authorWithNewValues.getId())).thenReturn(Optional.of(authorToEdit));
        when(mockAuthorRepository.findAll()).thenReturn(authorEntityList);
        //when
        Exception result = Assertions.assertThrows(AuthorWithGivenNameAndSurnameExistsException.class,
                () -> authorService.editAuthor(authorWithNewValues));
        //then
        Assertions.assertEquals(AuthorService.AUTHOR_WITH_GIVEN_DATA_ALREADY_EXISTS, result.getMessage());
    }

    @Test
    @DisplayName("Should throw exception because couldn't find author with given id while delete")
    void shouldThrowExceptionBecauseCouldntFindAuthorWithGivenIdWhileDelete() {
        //given
        Integer id = 99;
        when(mockAuthorRepository.findById(id)).thenReturn(Optional.empty());
        //when
        Exception result = Assertions.assertThrows(AuthorNotFoundException.class,
                () -> authorService.deleteAuthor(id));
        //then
        Assertions.assertEquals(AuthorService.NOT_FOUND_AUTHOR, result.getMessage());
    }

    @Test
    @DisplayName("Should throw exception while delete because author have books")
    void shouldThrowExceptionWhileDeleteBecauseAuthorHaveBooks() {
        //given
        AuthorEntity author = authorEntityList.get(0);
        Integer id = author.getId();

        when(mockAuthorRepository.findById(id)).thenReturn(Optional.of(author));
        when(mockBookRepository.findAllByAuthor(author)).thenReturn(List.of(new BookEntity()));
        //when
        Exception result = Assertions.assertThrows(AuthorHaveBooksException.class,
                () -> authorService.deleteAuthor(id));
        //then
        Assertions.assertEquals(AuthorService.AUTHOR_HAVE_BOOKS, result.getMessage());
    }

    @Test
    @DisplayName("Should return list with books which author have")
    void shouldReturnListOfBooksWhichAuthorHave() throws AuthorNotFoundException {
        //given
        AuthorEntity author = authorEntityList.get(0);
        List<BookEntity> bookEntities = List.of(bookEntityList.get(0));

        when(mockAuthorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(mockBookRepository.findAllByAuthor(author)).thenReturn(bookEntities);
        //when
        List<BookEntity> result = authorService.findAllBooksOfGivenAuthor(author.getId());
        //then
        Assertions.assertEquals(bookEntities, result);
    }
}
