package com.company.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Date;

@Data
public class LoanDto {

    @ApiModelProperty(notes = "Borrowed book")
    private BookDto book;

    @ApiModelProperty(notes = "Reader who borrowed book")
    private ReaderDto reader;

    @ApiModelProperty(notes = "Date of borrow")
    private Date borrowDate;
    @ApiModelProperty(notes = "Date of return a book")
    private Date returnedDate;
}
