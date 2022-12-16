package services;

import com.company.repository.models.entity.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class TestUtils {

    static List<AuthorEntity> createAuthors() {
        AuthorEntity author1 = new AuthorEntity();
        author1.setName("Name1");
        author1.setSurname("surname1");
        author1.setId(1);
        AuthorEntity author2 = new AuthorEntity();
        author2.setName("Name2");
        author2.setSurname("surname2");
        author2.setId(2);
        return List.of(author1, author2);
    }

    static List<BookEntity> createBooks() {
        List<AuthorEntity> authors = createAuthors();
        BookEntity book1 = new BookEntity();
        book1.setAuthor(authors.get(0));
        book1.setId(1);
        book1.setTitle("title1");
        book1.setQuantity(4);
        BookEntity book2 = new BookEntity();
        book2.setAuthor(authors.get(1));
        book2.setId(2);
        book2.setTitle("title2");
        book2.setQuantity(2);
        return List.of(book1, book2);
    }

    static List<ReaderEntity> createReaders() {
        ReaderEntity reader1 = new ReaderEntity();
        reader1.setId(1);
        reader1.setName("Jan");
        reader1.setSurname("Nowak");
        reader1.setBirthDate(Date.valueOf(LocalDate.now().minusYears(20)));

        ReaderEntity reader2 = new ReaderEntity();
        reader2.setId(2);
        reader2.setName("Adam");
        reader2.setSurname("Kowalczyk");
        reader2.setBirthDate(Date.valueOf(LocalDate.now().minusYears(5)));
        return List.of(reader1, reader2);
    }

    static List<LoanEntity> createLoans() {
        List<ReaderEntity> readers = createReaders();
        List<BookEntity> books = createBooks();
        LoanEntity loanEntity1 = new LoanEntity();
        loanEntity1.setBook(books.get(0));
        loanEntity1.setReader(readers.get(0));
        loanEntity1.setBorrowDate(Date.valueOf(LocalDate.now()));
        LoanEntity loansEntity2 = new LoanEntity();
        loansEntity2.setBook(books.get(0));
        loansEntity2.setReader(readers.get(0));
        loansEntity2.setBorrowDate(Date.valueOf(LocalDate.now().minusDays(10)));
        loansEntity2.setReturnedDate(Date.valueOf(LocalDate.now()));
        return List.of(loanEntity1, loansEntity2);
    }

    static List<ReaderEntity> createParents() {
        ParentEntity parent1 = new ParentEntity();
        parent1.setId(3);
        parent1.setName("name1");
        parent1.setSurname("surname1");
        parent1.setAddress("address1");
        parent1.setPhoneNumber("phone1");
        parent1.setNumberOfCurrentlyBorrowedBooks(20);
        parent1.setBirthDate(new Date(1990, 10, 10));

        ParentEntity parent2 = new ParentEntity();
        parent2.setId(4);
        parent2.setName("name2");
        parent2.setSurname("surname2");
        parent2.setAddress("address2");
        parent2.setPhoneNumber("phone2");
        parent1.setBirthDate(new Date(1992, 1, 20));
        return List.of(parent1, parent2);
    }

    static List<ReaderEntity> createChilds() {
        List<ReaderEntity> parents = createParents();
        ChildEntity child1 = new ChildEntity();
        child1.setId(5);
        child1.setName("child1");
        child1.setSurname("childSurname1");
        child1.setParent((ParentEntity) parents.get(0));
        child1.setBirthDate(new Date(2005, 10, 10));

        ChildEntity child2 = new ChildEntity();
        child2.setId(6);
        child2.setName("child2");
        child2.setSurname("childSurname2");
        child2.setParent((ParentEntity) parents.get(1));
        child1.setBirthDate(new Date(2015, 10, 10));
        return List.of(child1, child2);
    }
}
