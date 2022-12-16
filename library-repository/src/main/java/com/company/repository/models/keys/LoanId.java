package com.company.repository.models.keys;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LoanId implements Serializable {

    private Integer book;
    private Integer reader;
    private Date borrowDate;

}
