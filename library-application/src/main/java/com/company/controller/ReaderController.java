package com.company.controller;


import com.company.dtos.ChildDto;
import com.company.dtos.LoanDto;
import com.company.dtos.ParentDto;
import com.company.dtos.ReaderDto;
import com.company.repository.models.entity.ChildEntity;
import com.company.repository.models.entity.ParentEntity;
import com.company.repository.models.entity.ReaderEntity;
import com.company.service.ReaderService;
import com.company.service.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reader/")
public class ReaderController {
    public static final String READER_CAN_BORROW_A_BOOK = "Reader can borrow a book";
    private final ReaderService readerService;
    private final ModelMapper modelMapper;


    /**
     * Method to get all readers
     *
     * @return
     */
    @GetMapping("all/")
    public ResponseEntity findAllReaders() {
        List<ReaderDto> readers = readerService.findAllReaders()
                .stream()
                .map(reader -> modelMapper.map(reader, ReaderDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity(readers, HttpStatus.OK);
    }

    /**
     * Method to get reader with given id
     *
     * @param id
     * @return
     */
    @GetMapping("findById/{id}")
    public ResponseEntity findReaderById(@PathVariable Integer id) throws ReaderNotFoundException {
        ReaderEntity reader = readerService.findReaderById(id);
        return new ResponseEntity(modelMapper.map(reader, ReaderDto.class), HttpStatus.OK);
    }

    /**
     * Method to find readers with given parameters
     *
     * @param id
     * @param name
     * @param surname
     * @param birthDate
     * @param numberOfCurrentlyBorrowedBooks
     * @param numberOfEveryBorrowedBooks
     * @return
     */
    @GetMapping("find/")
    public ResponseEntity findReaderByParameters(@RequestParam(required = false) Integer id,
                                                 @RequestParam(required = false) String name,
                                                 @RequestParam(required = false) String surname,
                                                 @RequestParam(required = false) LocalDate birthDate,
                                                 @RequestParam(required = false) Integer numberOfCurrentlyBorrowedBooks,
                                                 @RequestParam(required = false) Integer numberOfEveryBorrowedBooks) {
        List<ReaderDto> readers = readerService.findReaderByParameters(id, name, surname, birthDate, numberOfCurrentlyBorrowedBooks, numberOfEveryBorrowedBooks)
                .stream()
                .map(reader -> modelMapper.map(reader, ReaderDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity(readers, HttpStatus.OK);
    }

    /**
     * Method to find child readers with given parameters
     *
     * @param id
     * @param name
     * @param surname
     * @param birthDate
     * @param numberOfCurrentlyBorrowedBooks
     * @param numberOfEveryBorrowedBooks
     * @param parentId
     * @return
     */

    @GetMapping("child/find/")
    public ResponseEntity findChildByParameters(@RequestParam(required = false) Integer id,
                                                @RequestParam(required = false) String name,
                                                @RequestParam(required = false) String surname,
                                                @RequestParam(required = false) LocalDate birthDate,
                                                @RequestParam(required = false) Integer numberOfCurrentlyBorrowedBooks,
                                                @RequestParam(required = false) Integer numberOfEveryBorrowedBooks,
                                                @RequestParam(required = false) Integer parentId) {
        List<ReaderDto> readers = readerService.findChildByParameters(id, name, surname, birthDate,
                        numberOfCurrentlyBorrowedBooks, numberOfEveryBorrowedBooks, parentId)
                .stream()
                .map(reader -> modelMapper.map(reader, ReaderDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity(readers, HttpStatus.OK);
    }

    /**
     * Method to find parent readers with given parameters
     *
     * @param id
     * @param name
     * @param surname
     * @param birthDate
     * @param numberOfCurrentlyBorrowedBooks
     * @param numberOfEveryBorrowedBooks
     * @param address
     * @param phoneNumber
     * @return
     */
    @GetMapping("parent/find/")
    public ResponseEntity findParentByParameters(@RequestParam(required = false) Integer id,
                                                 @RequestParam(required = false) String name,
                                                 @RequestParam(required = false) String surname,
                                                 @RequestParam(required = false) LocalDate birthDate,
                                                 @RequestParam(required = false) Integer numberOfCurrentlyBorrowedBooks,
                                                 @RequestParam(required = false) Integer numberOfEveryBorrowedBooks,
                                                 @RequestParam(required = false) String address,
                                                 @RequestParam(required = false) String phoneNumber) {
        List<ReaderDto> readers = readerService.findParentByParameters(id, name, surname, birthDate, numberOfCurrentlyBorrowedBooks,
                        numberOfEveryBorrowedBooks, address, phoneNumber)
                .stream()
                .map(reader -> modelMapper.map(reader, ReaderDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity(readers, HttpStatus.OK);
    }

    /**
     * Method to delete reader with given id
     *
     * @param id, of reader which should be deleted
     * @return
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity deleteReader(@PathVariable Integer id) throws ReaderHasCurrentlyBookOnLoanException, ReaderNotFoundException {
        readerService.deleteReader(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * Method to check is reader have any book too long
     *
     * @return , books which reader have or empty list
     */
    @GetMapping("booksTooLong/{id}")
    public ResponseEntity isReaderHaveBooksTooLong(@PathVariable Integer id) throws ReaderNotFoundException {
        List<LoanDto> loans = readerService.booksIdWhichReaderHaveTooMuchTime(id)
                .stream()
                .map(loan -> modelMapper.map(loan, LoanDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity(loans, HttpStatus.OK);
    }


    /**
     * The method which check is reader can borrow a book (method checks him age, and current loans)
     *
     * @param id, which you want to check
     * @return true if reader can borrow a book
     */
    @GetMapping("canBorrowBook/{id}")
    public ResponseEntity isReaderCanBorrowBook(@PathVariable Integer id) throws ReaderNotFoundException, ReaderTooYoungException, ReaderHaveBooksTooLongException, ReaderHaveTooMuchBooksException {
        readerService.isReaderCanBorrowABook(id);
        return new ResponseEntity(READER_CAN_BORROW_A_BOOK, HttpStatus.OK);
    }


    /**
     * Method to add new reader
     *
     * @param readerDto, reader which should be added
     * @return
     */
    @PutMapping("add/")
    public ResponseEntity addReader(@RequestBody ReaderDto readerDto) {
        ReaderEntity result = readerService.addReader(modelMapper.map(readerDto, ReaderEntity.class));
        return new ResponseEntity(modelMapper.map(result, ReaderDto.class), HttpStatus.CREATED);
    }

    /**
     * Method to add new parent reader
     *
     * @param parentDto, parent which should be added
     * @return
     */
    @PutMapping("parent/add/")
    public ResponseEntity addParentReader(@RequestBody ParentDto parentDto) {
        ParentEntity result = readerService.addParentReader(modelMapper.map(parentDto, ParentEntity.class));
        return new ResponseEntity(modelMapper.map(result, ParentDto.class), HttpStatus.OK);
    }

    /**
     * Method to add new child reader
     *
     * @param childDto, child which should be added
     * @return
     */
    @PutMapping("child/add/")
    public ResponseEntity addChildReader(@RequestBody ChildDto childDto) throws ChildWithoutParentGuardianException, ParentNotFoundException {
        ChildEntity result = readerService.addChildReader(modelMapper.map(childDto, ChildEntity.class));
        return new ResponseEntity(modelMapper.map(result, ChildDto.class), HttpStatus.CREATED);
    }

    /**
     * Method to edit reader with given id
     *
     * @param readerWithNewData, reader body with parameters which should be edited
     * @return
     */
    @PatchMapping("edit/")
    public ResponseEntity editReader(@RequestBody ReaderDto readerWithNewData) throws ReaderNotFoundException {
        ReaderEntity result = readerService.editReader(modelMapper.map(readerWithNewData, ReaderEntity.class));
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * Method to edit parent reader with given id
     *
     * @param parentWithNewData, parent body with parameters which should be edited
     * @return
     */
    @PatchMapping("parent/edit/")
    public ResponseEntity editParent(@RequestBody ParentDto parentWithNewData) throws ReaderIsNotParentException, ReaderNotFoundException {
        ParentEntity result = readerService.editParent(modelMapper.map(parentWithNewData, ParentEntity.class));
        return new ResponseEntity(modelMapper.map(result, ParentDto.class), HttpStatus.OK);
    }

    /**
     * Method to edit child reader with given id
     *
     * @param childWithNewData, child body with parameters which should be edited
     * @return
     */

    @PatchMapping("child/edit/")
    public ResponseEntity editChild(@RequestBody ChildDto childWithNewData) throws ReaderNotFoundException, ParentNotFoundException, ReaderIsNotChildException {
        ChildEntity result = readerService.editChild(modelMapper.map(childWithNewData, ChildEntity.class));
        return new ResponseEntity(modelMapper.map(result, ChildDto.class), HttpStatus.OK);
    }

    /**
     * Method to edit type of child to parent
     *
     * @param id,         id of child which should be edited
     * @param address
     * @param phoneNumber
     * @return
     */
    @PatchMapping("child/editToParent/{id}")
    public ResponseEntity editChildToParent(@PathVariable Integer id,
                                            @RequestParam String address,
                                            @RequestParam String phoneNumber) throws ReaderNotFoundException, ReaderIsNotChildException {
        ReaderEntity result = readerService.editChildToParent(id, address, phoneNumber);
        return new ResponseEntity(modelMapper.map(result, ReaderDto.class), HttpStatus.OK);
    }

}
