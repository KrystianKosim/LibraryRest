package com.company.repository.models.repository;


import com.company.repository.models.entity.ChildEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildRepository extends JpaRepository<ChildEntity, Integer> {
}
