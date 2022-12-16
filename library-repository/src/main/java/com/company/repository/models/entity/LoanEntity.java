package com.company.repository.models.entity;

import com.company.repository.models.keys.LoanId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "loans")
@IdClass(LoanId.class)
public class LoanEntity{
        @Id
        @JoinColumn(name = "book_id")
        @OneToOne
        private BookEntity book;

        @Id
        @JoinColumn(name = "reader_id")
        @OneToOne
        private ReaderEntity reader;

        @Id
        @Column(name = "borrow_date")
        private Date borrowDate;

        @Id
        @Column(name = "returned_date")
        private Date returnedDate;
}
