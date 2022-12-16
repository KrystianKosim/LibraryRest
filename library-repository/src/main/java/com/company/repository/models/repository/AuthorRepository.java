package com.company.repository.models.repository;

import com.company.repository.models.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {

    Optional<AuthorEntity> findAuthorEntitiesByNameAndSurname(String name, String surname);
}
