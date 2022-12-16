package com.company.repository.models.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ConfigurationRepository {

    private final EntityManager entityManager;
    private static final int ID_OF_RECORD_WITH_CONFIGURATION_DATA = 1;

    @Transactional
    public void editNumberOfBorrowedBooks(int maxNumberOfBorrowedBooks) {
        String sqlStatement = "UPDATE configuration SET maxnumberofborrowedbooks=:value WHERE id=:id";
        entityManager.createNativeQuery(sqlStatement)
                .setParameter("value", maxNumberOfBorrowedBooks)
                .setParameter("id", ID_OF_RECORD_WITH_CONFIGURATION_DATA)
                .executeUpdate();
    }

    @Transactional
    public void editMinAgeToBorrowABook(int minAgeToBorrowABook) {
        String sqlStatement = "UPDATE configuration SET minagetoborrowabook=:value WHERE id=:id";
        entityManager.createNativeQuery(sqlStatement)
                .setParameter("value", minAgeToBorrowABook)
                .setParameter("id", ID_OF_RECORD_WITH_CONFIGURATION_DATA)
                .executeUpdate();
    }

    @Transactional
    public void editNumberOfDaysToBorrowABook(int maxNumberOfDays) {
        String sqlStatement = "UPDATE configuration SET maxnumberofdaystoborrowabook=:value WHERE id=:id";
        entityManager.createNativeQuery(sqlStatement)
                .setParameter("value", maxNumberOfDays)
                .setParameter("id", ID_OF_RECORD_WITH_CONFIGURATION_DATA)
                .executeUpdate();
    }

    @Transactional
    public void addConfigurationDataToTable(int maxNumberOfBorrowedBooks, int maxNumberOfDaysToBorrowABook, int minAgeToBorrowABook) {
        String sqlStatement = "INSERT INTO configuration VALUES (:id,:maxNumberOfBorrowedBooks,:maxNumberOfDaysToBorrowABook,:minAgeToBorrowABook)";
        entityManager.createNativeQuery(sqlStatement)
                .setParameter("id", ID_OF_RECORD_WITH_CONFIGURATION_DATA)
                .setParameter("maxNumberOfBorrowedBooks", maxNumberOfBorrowedBooks)
                .setParameter("maxNumberOfDaysToBorrowABook", maxNumberOfDaysToBorrowABook)
                .setParameter("minAgeToBorrowABook", minAgeToBorrowABook)
                .executeUpdate();
    }

    public int getMaxNumberOfBorrowedBooks() {
        String sqlStatement = "SELECT maxNumberOfBorrowedBooks FROM configuration WHERE id=:id";
        Query query = entityManager.createNativeQuery(sqlStatement)
                .setParameter("id", ID_OF_RECORD_WITH_CONFIGURATION_DATA);
        List results = query.getResultList();
        int maxNumberOfBorrowedBooks = (int) results.get(0);
        return maxNumberOfBorrowedBooks;
    }

    public int getMaxNumberOfDaysToBorrowABook() {
        String sqlStatement = "SELECT maxNumberOfDaysToBorrowABook FROM configuration WHERE id=:id";
        Query query = entityManager.createNativeQuery(sqlStatement)
                .setParameter("id", ID_OF_RECORD_WITH_CONFIGURATION_DATA);
        List results = query.getResultList();
        int maxNumberOfDaysToBorrowABook = (int) results.get(0);
        return maxNumberOfDaysToBorrowABook;
    }

    public int getMinAgeToBorrowABook() {
        String sqlStatement = "SELECT minAgeToBorrowABook FROM configuration WHERE id=:id";
        Query query = entityManager.createNativeQuery(sqlStatement)
                .setParameter("id", ID_OF_RECORD_WITH_CONFIGURATION_DATA);
        List results = query.getResultList();
        int minAgeToBorrowABook = (int) results.get(0);
        return minAgeToBorrowABook;
    }

    public boolean isConfigurationDataInTable() {
        String sqlStatement = "SELECT * FROM configuration WHERE id=:id";
        Query query = entityManager.createNativeQuery(sqlStatement).setParameter("id", ID_OF_RECORD_WITH_CONFIGURATION_DATA);
        List results = query.getResultList();
        return results.size() == 1;
    }
}
