package com.company.controller;

import com.company.dtos.AuthorDto;
import com.company.dtos.BookDto;
import com.company.repository.models.entity.AuthorEntity;
import com.company.service.AuthorService;
import com.company.service.exceptions.AuthorHaveBooksException;
import com.company.service.exceptions.AuthorNotFoundException;
import com.company.service.exceptions.AuthorWithGivenNameAndSurnameExistsException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@EnableWebMvc
@RestController
@RequestMapping("/author/")
public class AuthorController {
    private final AuthorService authorService;
    private final ModelMapper modelMapper;

    /**
     * Method to get all authors
     *
     * @return
     */
    @GetMapping("all/")
    public ResponseEntity findAllAuthors() {
        List<AuthorDto> authors = authorService.findAllAuthors()
                .stream()
                .map(author -> modelMapper.map(author, AuthorDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity(authors, HttpStatus.OK);
    }

    /**
     * Method to find authors with given parameters, and map results to dto's
     *
     * @param id
     * @param name
     * @param surname
     * @return
     */
    @GetMapping("findAuthors/")
    public ResponseEntity findAuthors(@RequestParam(required = false) Integer id,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) String surname) {
        List<AuthorDto> authors = authorService.findAuthors(id, name, surname)
                .stream()
                .map(author -> modelMapper.map(author, AuthorDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity(authors, HttpStatus.OK);
    }

    /**
     * Method to find single author with given id, and map result to dto's
     *
     * @param id
     * @return
     */
    @GetMapping("findAuthor/{id}")
    public ResponseEntity findAuthorById(@PathVariable Integer id) throws AuthorNotFoundException {
        AuthorEntity author = authorService.findAuthorById(id);
        return new ResponseEntity(modelMapper.map(author, AuthorDto.class), HttpStatus.OK);
    }

    /**
     * Method to find all books with author given as parameter, and map result to dto's
     *
     * @param id
     * @return
     */
    @GetMapping("findBooks/{id}")
    public ResponseEntity findAllBooksWithGivenAuthor(@PathVariable Integer id) throws AuthorNotFoundException {
        List<BookDto> books = authorService.findAllBooksOfGivenAuthor(id)
                .stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity(books, HttpStatus.OK);
    }


    /**
     * Method to edit author with id given as parameter, and map result to dto's
     *
     * @param authorWithNewData, author body with parameters which should be edited
     * @return
     */
    @PatchMapping("edit/")
    public ResponseEntity editAuthor(@RequestBody AuthorDto authorWithNewData) throws AuthorNotFoundException, AuthorWithGivenNameAndSurnameExistsException {
        AuthorEntity result = authorService.editAuthor(modelMapper.map(authorWithNewData, AuthorEntity.class));
        return new ResponseEntity(modelMapper.map(result, AuthorDto.class), HttpStatus.OK);
    }

    /**
     * Method to delete single author with given id
     *
     * @param id
     * @return
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity deleteAuthorById(@PathVariable Integer id) throws AuthorNotFoundException, AuthorHaveBooksException {
        authorService.deleteAuthor(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Method to add new author, and map result to dto's
     *
     * @param author, author which should be added
     * @return
     */
    @PutMapping("addAuthor/")
    public ResponseEntity addAuthor(@RequestBody AuthorDto author) throws AuthorWithGivenNameAndSurnameExistsException {
        AuthorEntity result = authorService.addAuthor(modelMapper.map(author, AuthorEntity.class));
        return new ResponseEntity(modelMapper.map(result, AuthorEntity.class), HttpStatus.CREATED);
    }

}
