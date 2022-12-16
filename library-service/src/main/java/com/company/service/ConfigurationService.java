package com.company.service;

import com.company.repository.models.repository.ConfigurationRepository;
import com.company.service.exceptions.ConfigurationValueIncorrectException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    public static final String INCORRECT_VALUE = "Incorrect value";

    /**
     * Method to edit value of maximum days which reader can have a book
     * @param maxNumberOfDaysToBorrowABook
     * @throws ConfigurationValueIncorrectException, if given value is incorrect
     */
    public void editNumberOfDaysToBorrowABook(Integer maxNumberOfDaysToBorrowABook) throws ConfigurationValueIncorrectException {
        checkConfigurationValue(maxNumberOfDaysToBorrowABook);
        configurationRepository.editNumberOfDaysToBorrowABook(maxNumberOfDaysToBorrowABook);
    }

    /**
     * Method to edit value of maximum number of books which reader can borrow in the same time
     * @param maxNumberOfBorrowedBooks
     * @throws ConfigurationValueIncorrectException, if given value is incorrect
     */
    public void editNumberOfBorrowedBooks(Integer maxNumberOfBorrowedBooks) throws ConfigurationValueIncorrectException {
        checkConfigurationValue(maxNumberOfBorrowedBooks);
        configurationRepository.editNumberOfBorrowedBooks(maxNumberOfBorrowedBooks);
    }

    /**
     * Method to edit value of min age that reader must have to borrow a book
     * @param minAgeToBorrowABook
     * @throws ConfigurationValueIncorrectException
     */
    public void editMinAgeToBorrowABook(Integer minAgeToBorrowABook) throws ConfigurationValueIncorrectException {
        checkConfigurationValue(minAgeToBorrowABook);
        configurationRepository.editMinAgeToBorrowABook(minAgeToBorrowABook);
    }

    /**
     * Method to get value of maximum number of books which reader can borrow in the same time from database
     * @return
     */
    public int getMaxNumberOfBorrowedBooks() {
        return configurationRepository.getMaxNumberOfBorrowedBooks();
    }

    /**
     * Method to get value of maximum days which reader can have a book from database
     * @return
     */
    public int getMaxNumberOfDaysToBorrowABook() {
        return configurationRepository.getMaxNumberOfDaysToBorrowABook();
    }

    /**
     * Method to get value of min age that reader must have to borrow a book from database
     * @return
     */
    public Integer getMinAgeToBorrowABook() {
        return configurationRepository.getMinAgeToBorrowABook();
    }

    private void checkConfigurationValue(Integer value) throws ConfigurationValueIncorrectException {
        if(value < 0){
         throw new ConfigurationValueIncorrectException(INCORRECT_VALUE);
        }
    }
}
