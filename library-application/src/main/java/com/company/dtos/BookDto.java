package com.company.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    @ApiModelProperty(notes = "Book ID", example = "1")
    private Integer id;
    @ApiModelProperty(notes = "Book title", example = "Harry Potter")
    private String title;
    @ApiModelProperty(notes = "Book author")
    private AuthorDto author;
    @ApiModelProperty(notes = "Quantity of all books in library", example = "1")
    private Integer quantity;
    @ApiModelProperty(notes = "Quantity of current available books", example = "1")
    private Integer quantityAvailable;
}
