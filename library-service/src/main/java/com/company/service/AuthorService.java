package com.company.service;

import com.company.repository.models.entity.AuthorEntity;
import com.company.repository.models.entity.BookEntity;
import com.company.repository.models.repository.AuthorRepository;
import com.company.repository.models.repository.BookRepository;
import com.company.service.exceptions.AuthorHaveBooksException;
import com.company.service.exceptions.AuthorNotFoundException;
import com.company.service.exceptions.AuthorWithGivenNameAndSurnameExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorService {
    public static final String NOT_FOUND_AUTHOR = "Not found author!";
    public static final String AUTHOR_WITH_GIVEN_DATA_ALREADY_EXISTS = "Author with given data already exists";
    public static final String AUTHOR_HAVE_BOOKS = "Author have books";

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    /**
     * Method to add new author to database
     *
     * @param author
     * @return
     * @throws AuthorWithGivenNameAndSurnameExistsException, if author with given name and surname exists in database
     */
    public AuthorEntity addAuthor(AuthorEntity author) throws AuthorWithGivenNameAndSurnameExistsException {
        List<AuthorEntity> authorsWithGivenData = findAuthors(null, author.getName(), author.getSurname());
        if (!authorsWithGivenData.isEmpty()) {
            throw new AuthorWithGivenNameAndSurnameExistsException(AUTHOR_WITH_GIVEN_DATA_ALREADY_EXISTS);
        }
        author.setId(null);
        authorRepository.save(author);
        return author;
    }


    /**
     * Method to get all authors from database
     *
     * @return
     */
    public List<AuthorEntity> findAllAuthors() {
        return authorRepository.findAll();
    }

    /**
     * Method to find multiple authors with given parameters
     *
     * @param id
     * @param name
     * @param surname
     * @return
     */
    public List<AuthorEntity> findAuthors(Integer id, String name, String surname) {
        List<AuthorEntity> authors = findAllAuthors();
        return authors.stream()
                .filter(author -> Optional.ofNullable(id).isPresent() ? author.getId().equals(id) : true)
                .filter(author -> Optional.ofNullable(name).isPresent() ? author.getName().contains(name) : true)
                .filter(author -> Optional.ofNullable(surname).isPresent() ? author.getSurname().contains(surname) : true)
                .collect(Collectors.toList());
    }

    /**
     * Method to edit given author
     *
     * @param authorWithNewData
     * @return
     * @throws AuthorNotFoundException
     * @throws AuthorWithGivenNameAndSurnameExistsException
     */
    public AuthorEntity editAuthor(AuthorEntity authorWithNewData) throws AuthorNotFoundException, AuthorWithGivenNameAndSurnameExistsException {
        Optional.ofNullable(authorWithNewData.getId()).orElseThrow(
                () -> new AuthorNotFoundException(NOT_FOUND_AUTHOR));
        AuthorEntity author = findAuthorById(authorWithNewData.getId());

        List<AuthorEntity> authorsWithGivenData = findAuthors(null, authorWithNewData.getName(), authorWithNewData.getSurname());
        if (!authorsWithGivenData.isEmpty()) {
            throw new AuthorWithGivenNameAndSurnameExistsException(AUTHOR_WITH_GIVEN_DATA_ALREADY_EXISTS);
        }

        Optional.ofNullable(authorWithNewData.getName()).ifPresent(author::setName);
        Optional.ofNullable(authorWithNewData.getSurname()).ifPresent(author::setSurname);

        authorRepository.save(author);
        return author;
    }

    /**
     * Method to delete author with given id from databse
     *
     * @param id
     * @return
     */
    public void deleteAuthor(Integer id) throws AuthorNotFoundException, AuthorHaveBooksException {
        AuthorEntity author = findAuthorById(id);
        List<BookEntity> books = bookRepository.findAllByAuthor(author);
        if (!books.isEmpty()) {
            throw new AuthorHaveBooksException(AUTHOR_HAVE_BOOKS);
        }
        authorRepository.deleteById(id);
    }

    /**
     * Method to check is  author wrote books
     *
     * @param id, of author whose you want to check
     * @return list of book which author wrote or null if author didn't wrote any book
     */
    public List<BookEntity> findAllBooksOfGivenAuthor(Integer id) throws AuthorNotFoundException {
        AuthorEntity author = findAuthorById(id);
        List<BookEntity> books = bookRepository.findAllByAuthor(author);
        return books;
    }

    /**
     * Method to find author with given id
     *
     * @param id
     * @return
     */
    public AuthorEntity findAuthorById(Integer id) throws AuthorNotFoundException {
        Optional<AuthorEntity> author = authorRepository.findById(id);
        if (author.isEmpty()) {
            throw new AuthorNotFoundException(NOT_FOUND_AUTHOR);
        }
        return author.get();
    }
}
