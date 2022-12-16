package com.company.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChildDto extends ReaderDto {

    @ApiModelProperty(notes = "Child parent")
    private ParentDto parent;

    @Override
    public String toString() {
        return super.toString()
                +  "\t" + "parent id: " + parent + "\n";
    }
}
