package com.company.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

    @ApiModelProperty(notes = "Author ID", example = "1")
    private Integer id;
    @ApiModelProperty(notes = "Author name", example = "John")
    private String name;
    @ApiModelProperty(notes = "Author surname", example = "Smith")
    private String surname;
}
