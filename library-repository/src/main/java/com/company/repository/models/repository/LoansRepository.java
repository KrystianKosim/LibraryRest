package com.company.repository.models.repository;

import com.company.repository.models.entity.BookEntity;
import com.company.repository.models.entity.ReaderEntity;
import com.company.repository.models.keys.LoanId;
import com.company.repository.models.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LoansRepository extends JpaRepository<LoanEntity, LoanId> {

    List<LoanEntity> findAllByReader(ReaderEntity reader);
    List<LoanEntity> findAllByReaderAndReturnedDate(ReaderEntity reader,Date returnedDate);
    List<LoanEntity> findAllByBook(BookEntity book);
    void deleteAllByBook(BookEntity book);
    void deleteAllByReader(ReaderEntity reader);

    List<LoanEntity> findAllByBookAndReturnedDate(BookEntity book,Date returnedDate);
    List<LoanEntity> findAllByReaderAndBookAndReturnedDate(ReaderEntity reader, BookEntity book, Date returnedDate);
}