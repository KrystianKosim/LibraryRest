package com.company.repository.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Reader")
@Inheritance(strategy = InheritanceType.JOINED)
public class ReaderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String surname;

    private Date birthDate;

    @Formula("(select count(*) from loans l where l.reader_id = id and l.returned_date is null)")
    private Integer numberOfCurrentlyBorrowedBooks;
    @Formula("(select count(*) from loans l where l.reader_id = id)")
    private Integer numberOfEveryBorrowedBooks;

    @Override
    public String toString() {
        return "*****************Reader***************** " + "\n" +
                "\t" + "id: " + id + "\n" +
                "\t" + "name: " + name + "\n" +
                "\t" + "surname: " + surname + "\n" +
                "\t" + "birthDate: " + birthDate + "\n" +
                "\t" + "numberOfCurrentBorrowedBooks: " + numberOfCurrentlyBorrowedBooks + "\n" +
                "\t" + "numberOfEveryBorrowedBooks: " + numberOfEveryBorrowedBooks + "\n";
    }
}
