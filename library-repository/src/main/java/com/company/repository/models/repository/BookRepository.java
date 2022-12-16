package com.company.repository.models.repository;

import com.company.repository.models.entity.AuthorEntity;
import com.company.repository.models.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Integer> {
    List<BookEntity> findAllByAuthor(AuthorEntity author);
}
