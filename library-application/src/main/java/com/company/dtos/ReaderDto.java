package com.company.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Date;

@Data
public class ReaderDto {

    @ApiModelProperty(notes = "Reader ID", example = "1")
    private Integer id;

    @ApiModelProperty(notes = "Reader name", example = "John")
    private String name;

    @ApiModelProperty(notes = "Reader surname", example = "Smith")
    private String surname;

    @ApiModelProperty(notes = "Reader birthdate", example = "2018-01-01")
    private Date birthDate;

    @ApiModelProperty(notes = "Number of books which reader currently have", example = "1")
    private Integer numberOfCurrentlyBorrowedBooks = 0;

    @ApiModelProperty(notes = "Number of every books which reader borrowed", example = "1")
    private Integer numberOfEveryBorrowedBooks = 0;

    @Override
    public String toString() {
        return "\t" + "id: " + id + "\n" +
                "\t" + "name: " + name + "\n" +
                "\t" + "surname: " + surname + "\n" +
                "\t" + "birthDate: " + birthDate + "\n" +
                "\t" + "numberOfCurrentBorrowedBooks: " + numberOfCurrentlyBorrowedBooks + "\n" +
                "\t" + "numberOfEveryBorrowedBooks: " + numberOfEveryBorrowedBooks + "\n";
    }
}
