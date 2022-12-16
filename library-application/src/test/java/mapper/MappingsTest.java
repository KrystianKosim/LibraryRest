package mapper;

import com.company.dtos.*;
import com.company.mapper.mappings.*;
import com.company.repository.models.entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.sql.Date;
import java.time.LocalDate;

public class MappingsTest {


    private ModelMapper modelMapper;
    private static AuthorDto authorDto;
    private static AuthorEntity authorEntity;
    private static BookDto bookDto;
    private static BookEntity bookEntity;
    private static ReaderEntity readerEntity;
    private static ReaderDto readerDto;
    private static ParentDto parentDto;
    private static ParentEntity parentEntity;
    private static ChildDto childDto;
    private static ChildEntity childEntity;
    private static LoanDto loanDto;
    private static LoanEntity loanEntity;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        modelMapper.addConverter(new AuthorDtoToAuthorEntity());
        modelMapper.addConverter(new AuthorEntityToAuthorDto());
        modelMapper.addConverter(new BookDtoToBookEntity());
        modelMapper.addConverter(new BookEntityToBookDto());
        modelMapper.addConverter(new ChildDtoToChildEntity());
        modelMapper.addConverter(new ChildEntityToChildDto());
        modelMapper.addConverter(new LoanDtoToLoanEntity());
        modelMapper.addConverter(new LoanEntityToLoanDto());
        modelMapper.addConverter(new ParentDtoToParentEntity());
        modelMapper.addConverter(new ParentEntityToParentDto());
        modelMapper.addConverter(new ReaderDtoToReaderEntity());
        modelMapper.addConverter(new ReaderEntityToReaderDto());
    }

    @BeforeEach
    void initValues() {
        initAuthorDto();
        initAuthorEntity();
        initBookDto();
        initBookEntity();
        initReaderDto();
        initReaderEntity();
        initParentDto();
        initParentEntity();
        initChildDto();
        initChildEntity();
        initLoanDto();
        initLoanEntity();
    }

    @Test
    @DisplayName("Should map author dto to author entity")
    void authorDtoToAuthorEntity() {
        //given
        //when
        AuthorEntity authorEntity = modelMapper.map(authorDto, AuthorEntity.class);
        //then
        equalsAuthors(authorDto, authorEntity);
    }

    @Test
    @DisplayName("Should map author entity to author dto")
    void authorEntityToAuthorDto() {
        //given
        //when
        AuthorDto authorDto = modelMapper.map(authorEntity, AuthorDto.class);
        //then
        equalsAuthors(authorDto, authorEntity);
    }

    @Test
    @DisplayName("Should map book dto to book entity")
    void bookDtoToBookEntity() {
        //given
        //when
        BookEntity bookEntity = modelMapper.map(bookDto, BookEntity.class);
        //then
        equalsBooks(bookDto, bookEntity);
    }

    @Test
    @DisplayName("Should map book entity to book dto")
    void bookEntityToBookDto() {
        //given
        //when
        BookDto bookDto = modelMapper.map(bookEntity, BookDto.class);
        //then
        equalsBooks(bookDto, bookEntity);
    }

    @Test
    @DisplayName("Should map reader dto to reader entity")
    void readerDtoToReaderEntity() {
        //given
        //when
        ReaderEntity readerEntity = modelMapper.map(readerDto, ReaderEntity.class);
        //then
        equalsReaders(readerDto,readerEntity);
    }

    @Test
    @DisplayName("Should map reader entity to reader dto")
    void readerEntityToReaderDto(){
        //given
        //when
        ReaderDto readerDto = modelMapper.map(readerEntity,ReaderDto.class);
        //then
        equalsReaders(readerDto,readerEntity);
    }

    @Test
    @DisplayName("Should map parent dto to parent entity")
    void parentDtoToParentEntity(){
        //given
        //when
        ParentEntity parentEntity = modelMapper.map(parentDto,ParentEntity.class);
        //then
        equalsParents(parentDto,parentEntity);
    }

    @Test
    @DisplayName("should map parent entity to parent dto")
    void shouldMapParentEntityToParentDto(){
        //given
        //when
        ParentDto parentDto = modelMapper.map(parentEntity,ParentDto.class);
        //then
        equalsParents(parentDto,parentEntity);
    }

    @Test
    @DisplayName("Should map child entity to child dto")
    void childEntityToChildDto(){
        //given
        //when
        ChildDto childDto = modelMapper.map(childEntity,ChildDto.class);
        //then
        equalsChild(childDto,childEntity);
    }

    @Test
    @DisplayName("Should map child dto to child entity")
    void childDtoToChildEntity(){
        //given
        //when
        ChildEntity childEntity = modelMapper.map(childDto,ChildEntity.class);
        //then
        equalsChild(childDto,childEntity);
    }

    @Test
    @DisplayName("Should map loan dto to loan entity")
    void loanDtoToLoanEntity(){
        //given
        //when
        LoanEntity loanEntity = modelMapper.map(loanDto,LoanEntity.class);
        //then
        equalsLoan(loanDto,loanEntity);
    }

    @Test
    @DisplayName("Should map loan entity to loan dto")
    void loanEntityToLoanDto(){
        //given
        //when
        LoanDto loanDto = modelMapper.map(loanEntity,LoanDto.class);
        //then
        equalsLoan(loanDto,loanEntity);
    }


    private void equalsLoan(LoanDto loanDto, LoanEntity loanEntity) {
        equalsBooks(loanDto.getBook(),loanEntity.getBook());
        equalsReaders(loanDto.getReader(),loanEntity.getReader());
        Assertions.assertEquals(loanDto.getBorrowDate(),loanEntity.getBorrowDate());
        Assertions.assertEquals(loanDto.getReturnedDate(),loanDto.getReturnedDate());
    }
    private void equalsChild(ChildDto childDto, ChildEntity childEntity) {
        equalsReaders(childDto,childEntity);
        equalsParents(childDto.getParent(),childEntity.getParent());
    }

    private void equalsParents(ParentDto parentDto, ParentEntity parentEntity) {
        equalsReaders(parentDto,parentEntity);
        Assertions.assertEquals(parentDto.getAddress(),parentEntity.getAddress());
        Assertions.assertEquals(parentDto.getPhoneNumber(),parentEntity.getPhoneNumber());
    }

    private void equalsReaders(ReaderDto readerDto, ReaderEntity readerEntity) {
        Assertions.assertEquals(readerDto.getId(),readerEntity.getId());
        Assertions.assertEquals(readerDto.getName(),readerEntity.getName());
        Assertions.assertEquals(readerDto.getSurname(),readerEntity.getSurname());
        Assertions.assertEquals(readerDto.getBirthDate(),readerEntity.getBirthDate());
        Assertions.assertEquals(readerDto.getNumberOfEveryBorrowedBooks(),readerEntity.getNumberOfEveryBorrowedBooks());
        Assertions.assertEquals(readerDto.getNumberOfCurrentlyBorrowedBooks(),readerEntity.getNumberOfCurrentlyBorrowedBooks());
    }

    private void equalsBooks(BookDto bookDto, BookEntity bookEntity) {
        Assertions.assertEquals(bookDto.getTitle(), bookEntity.getTitle());
        Assertions.assertEquals(bookDto.getId(), bookEntity.getId());
        Assertions.assertEquals(bookDto.getQuantity(), bookEntity.getQuantity());
        Assertions.assertEquals(bookDto.getQuantityAvailable(), bookEntity.getQuantityAvailable());
        equalsAuthors(bookDto.getAuthor(), bookEntity.getAuthor());
    }

    private void equalsAuthors(AuthorDto authorDto, AuthorEntity authorEntity) {
        Assertions.assertEquals(authorEntity.getId(), authorDto.getId());
        Assertions.assertEquals(authorEntity.getName(), authorDto.getName());
        Assertions.assertEquals(authorEntity.getSurname(), authorDto.getSurname());
    }

    private void initLoanEntity(){
        loanEntity = new LoanEntity();
        loanEntity.setBook(bookEntity);
        loanEntity.setReader(readerEntity);
        loanEntity.setBorrowDate(Date.valueOf(LocalDate.now().minusDays(10)));
        loanEntity.setReturnedDate(Date.valueOf(LocalDate.now().minusYears(1)));
    }

    private void initLoanDto(){
        loanDto = new LoanDto();
        loanDto.setBook(bookDto);
        loanDto.setReader(readerDto);
        loanDto.setBorrowDate(Date.valueOf(LocalDate.now().minusDays(10)));
        loanDto.setReturnedDate(Date.valueOf(LocalDate.now().minusYears(1)));
    }

    private void initChildEntity(){
        childEntity = new ChildEntity();
        childEntity.setId(4);
        childEntity.setName("NameChild");
        childEntity.setSurname("SurnameChild");
        childEntity.setBirthDate(Date.valueOf(LocalDate.now().minusYears(5)));
        childEntity.setNumberOfEveryBorrowedBooks(22);
        childEntity.setNumberOfCurrentlyBorrowedBooks(11);
        childEntity.setParent(parentEntity);
    }

    private void initChildDto(){
        childDto = new ChildDto();
        childDto.setId(4);
        childDto.setName("NameChild");
        childDto.setSurname("SurnameChild");
        childDto.setBirthDate(Date.valueOf(LocalDate.now().minusYears(5)));
        childDto.setNumberOfEveryBorrowedBooks(22);
        childDto.setNumberOfCurrentlyBorrowedBooks(11);
        childDto.setParent(parentDto);
    }

    private void initParentEntity(){
        parentEntity= new ParentEntity();
        parentEntity.setId(3);
        parentEntity.setName("Name");
        parentEntity.setSurname("Surname");
        parentEntity.setBirthDate(Date.valueOf(LocalDate.now().minusYears(20)));
        parentEntity.setNumberOfEveryBorrowedBooks(2);
        parentEntity.setNumberOfCurrentlyBorrowedBooks(1);
        parentEntity.setAddress("Address");
        parentEntity.setPhoneNumber("500500500");
    }

    private void initParentDto(){
        parentDto = new ParentDto();
        parentDto.setId(3);
        parentDto.setName("Name");
        parentDto.setSurname("Surname");
        parentDto.setBirthDate(Date.valueOf(LocalDate.now().minusYears(20)));
        parentDto.setNumberOfEveryBorrowedBooks(2);
        parentDto.setNumberOfCurrentlyBorrowedBooks(1);
        parentDto.setAddress("Address");
        parentDto.setPhoneNumber("500500500");
    }

    private void initReaderEntity(){
        readerEntity = new ReaderEntity();
        readerEntity.setId(2);
        readerEntity.setName("Name");
        readerEntity.setSurname("Surname");
        readerEntity.setBirthDate(Date.valueOf(LocalDate.now().minusYears(20)));
        readerEntity.setNumberOfEveryBorrowedBooks(2);
        readerEntity.setNumberOfCurrentlyBorrowedBooks(1);
    }

    private void initReaderDto(){
        readerDto = new ReaderDto();
        readerDto.setId(2);
        readerDto.setName("Name");
        readerDto.setSurname("Surname");
        readerDto.setBirthDate(Date.valueOf(LocalDate.now().minusYears(20)));
        readerDto.setNumberOfEveryBorrowedBooks(2);
        readerDto.setNumberOfCurrentlyBorrowedBooks(1);
    }

    private void initAuthorDto(){
        authorDto = new AuthorDto();
        authorDto.setId(1);
        authorDto.setName("Name");
        authorDto.setSurname("Surname");
    }

    private void initAuthorEntity(){
        authorEntity = new AuthorEntity();
        authorEntity.setId(1);
        authorEntity.setName("Name");
        authorEntity.setSurname("Surname");
    }

    private void initBookDto(){
        bookDto = new BookDto();
        bookDto.setId(1);
        bookDto.setTitle("Title");
        bookDto.setAuthor(authorDto);
        bookDto.setQuantity(1);
        bookDto.setQuantityAvailable(1);
    }

    private void initBookEntity(){
        bookEntity = new BookEntity();
        bookEntity.setId(1);
        bookEntity.setTitle("Title");
        bookEntity.setAuthor(authorEntity);
        bookEntity.setQuantity(1);
        bookEntity.setQuantityAvailable(1);
    }

}
