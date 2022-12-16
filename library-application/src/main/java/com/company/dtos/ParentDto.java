package com.company.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ParentDto extends ReaderDto {
    @ApiModelProperty(notes = "Parent address", example = "Topolowa 10, 00-300 Warszawa")
    private String address;

    @ApiModelProperty(notes = "Parent phone number", example = "500500500")
    private String phoneNumber;

    @Override
    public String toString() {
        return super.toString() +
                "\t" + "address : " + address + "\n" +
                "\t" + "phone number : " + phoneNumber + "\n";
    }
}
