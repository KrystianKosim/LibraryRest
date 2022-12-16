package com.company.repository.models.entity;

import lombok.*;

import javax.persistence.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Author")
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String surname;

    @Override
    public String toString() {
        return "*****************Author***************** " + "\n" +
                "\t" + "id: " + id + "\n" +
                "\t" + "name: " + name + "\n" +
                "\t" + "surname: " + surname + "\n";
    }
}
