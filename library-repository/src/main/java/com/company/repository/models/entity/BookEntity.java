package com.company.repository.models.entity;


import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private AuthorEntity author;

    private Integer quantity;

    @Formula("quantity - (select count(*) from loans l where l.book_id = id)")
    private Integer quantityAvailable;

    @Override
    public String toString() {
        return "*****************Book*****************" + "\n" +
                "\t" + "id: " + id + "\n" +
                "\t" + "title: " + title + "\n" +
                "\t" + "author: " + " id: " + author.getId() + " name: " + author.getName() + " surname: " + author.getSurname() + "\n" +
                "\t" + "quantity: " + quantity + "\n" +
                "\t" + "quantityAvailable: " + quantityAvailable + "\n";
    }
}
