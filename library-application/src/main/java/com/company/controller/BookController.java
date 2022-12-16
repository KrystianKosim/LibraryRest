package com.company.controller;

import com.company.dtos.BookDto;
import com.company.repository.models.entity.BookEntity;
import com.company.service.BookService;
import com.company.service.exceptions.AuthorNotFoundException;
import com.company.service.exceptions.BookIsCurrentlyBorrowedException;
import com.company.service.exceptions.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@EnableWebMvc
@RequestMapping("/book/")
public class BookController {

    private final BookService bookService;
    private final ModelMapper modelMapper;

    /**
     * Method to get all books, and map result to dto's
     *
     * @return
     */
    @GetMapping("all/")
    public ResponseEntity findAllBooks() {
        List<BookDto> bookEntities = bookService.findAllBooks().stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity(bookEntities, HttpStatus.OK);
    }

    /**
     * Method to get for multiple books with given parameters, and map result to dto's
     *
     * @param id
     * @param title
     * @param authorId
     * @param quantity
     * @param quantityAvailable
     * @return
     */
    @GetMapping("findBooks/")
    public ResponseEntity findBooksByParameters(@RequestParam(required = false) Integer id,
                                                @RequestParam(required = false) String title,
                                                @RequestParam(required = false) Integer authorId,
                                                @RequestParam(required = false) Integer quantity,
                                                @RequestParam(required = false) Integer quantityAvailable) {
        List<BookDto> books = bookService.findBookByParameters(id, title, authorId, quantity, quantityAvailable)
                .stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity(books, HttpStatus.OK);
    }

    /**
     * Method to get single book with given id, and map result to dto's
     *
     * @param id
     * @return
     */
    @GetMapping("findBook/{id}")
    public ResponseEntity findBookById(@PathVariable Integer id) throws BookNotFoundException {
        BookEntity foundedBook = bookService.findBookById(id);
        return new ResponseEntity(modelMapper.map(foundedBook, BookDto.class), HttpStatus.OK);
    }

    /**
     * Method to edit book with given id, and map result to dto's
     *
     * @param book, BookDto body with parameters which should be edited
     * @return
     */
    @PatchMapping("edit/")
    public ResponseEntity editBook(@RequestBody BookDto book) throws AuthorNotFoundException, BookNotFoundException {
        BookEntity result = bookService.editBook(modelMapper.map(book, BookEntity.class));
        return new ResponseEntity(modelMapper.map(result, BookDto.class), HttpStatus.OK);
    }


    /**
     * Method to delete a single book with given id
     *
     * @param id
     * @return
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity deleteBookById(@PathVariable Integer id) throws BookIsCurrentlyBorrowedException, BookNotFoundException {
        bookService.deleteBook(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    /**
     * Method to add new book,and map result to dto's
     *
     * @param bookDto, book with new parameters
     * @return
     */
    @PutMapping("addBook/")
    public ResponseEntity addBook(@RequestBody BookDto bookDto) throws AuthorNotFoundException {
        BookEntity result = bookService.addBook(modelMapper.map(bookDto, BookEntity.class));
        return new ResponseEntity(modelMapper.map(result, BookDto.class), HttpStatus.CREATED);
    }
}
