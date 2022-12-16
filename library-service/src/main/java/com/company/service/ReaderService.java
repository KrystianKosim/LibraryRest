package com.company.service;

import com.company.repository.models.entity.ChildEntity;
import com.company.repository.models.entity.LoanEntity;
import com.company.repository.models.entity.ParentEntity;
import com.company.repository.models.entity.ReaderEntity;
import com.company.repository.models.repository.*;
import com.company.service.exceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ReaderService {

    public static final String READER_IS_NOT_PARENT = "Reader is not parent";
    private final ReaderRepository readerRepository;
    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;
    private final LoansRepository loansRepository;
    private final ConfigurationRepository configurationRepository;
    public static final String NOT_FOUND_READER_WITH_GIVEN_ID = "Not found reader with given id";
    public static final String READER_HAVE_CURRENTLY_BORROWED_BOOKS = "Reader have currently borrowed books ";
    public static final String YOU_HAVE_TO_GIVE_PARENT_LEGAL_GUARDIAN_ID = "You have to give parent/legal guardian id";
    public static final String INCORRECT_PARENT_LEGAL_GUARDIAN_ID = "Incorrect parent/legal guardian id";
    public static final String READER_WITH_GIVEN_ID_IS_NOT_CHILD = "Reader with given id is not child";
    public static final String READER_HAVE_TO_RETURN_BOOKS = "Reader have to return books: ";
    public static final String READER_HAVE_TOO_MUCH_BOOKS = "Reader have too much books";
    public static final String READER_IS_TOO_YOUNG = "Reader is too young";


    /**
     * Method to get all books which given reader have too long
     *
     * @param id
     * @return
     * @throws ReaderNotFoundException, if given reader doesn't exist
     */
    public List<LoanEntity> booksIdWhichReaderHaveTooMuchTime(Integer id) throws ReaderNotFoundException {
        ReaderEntity reader = findReaderById(id);
        Integer maxNumberOfDaysToBorrowABook = configurationRepository.getMaxNumberOfDaysToBorrowABook();
        return booksIdWhichReaderHaveTooMuchTime(reader, maxNumberOfDaysToBorrowABook);
    }

    /**
     * Method to find single reader by given id
     *
     * @param readerId
     * @return
     * @throws ReaderNotFoundException, if reader with given id doesn't exist
     */
    public ReaderEntity findReaderById(Integer readerId) throws ReaderNotFoundException {
        Optional<ReaderEntity> foundedReader = readerRepository.findById(readerId);
        if (foundedReader.isEmpty()) {
            throw new ReaderNotFoundException(NOT_FOUND_READER_WITH_GIVEN_ID);
        }
        return foundedReader.get();
    }

    /**
     * Method to find readers with given parameters
     *
     * @param id
     * @param name
     * @param surname
     * @param birthDate
     * @param numberOfCurrentlyBorrowedBooks
     * @param numberOfEveryBorrowedBooks
     * @return, reader with given parameters, or all readers if all parameters are null
     */
    public List<ReaderEntity> findReaderByParameters(Integer id, String name, String surname, LocalDate birthDate, Integer numberOfCurrentlyBorrowedBooks, Integer numberOfEveryBorrowedBooks) {
        List<ReaderEntity> readers = findAllReaders();
        return readers.stream()
                .filter(reader -> Optional.ofNullable(id).isPresent() ? reader.getId().equals(id) : true)
                .filter(reader -> Optional.ofNullable(name).isPresent() ? reader.getName().equals(name) : true)
                .filter(reader -> Optional.ofNullable(surname).isPresent() ? reader.getSurname().equals(surname) : true)
                .filter(reader -> Optional.ofNullable(birthDate).isPresent() ? reader.getBirthDate().equals(birthDate) : true)
                .filter(reader -> Optional.ofNullable(numberOfCurrentlyBorrowedBooks).isPresent() ? reader.getNumberOfCurrentlyBorrowedBooks().equals(numberOfCurrentlyBorrowedBooks) : true)
                .filter(reader -> Optional.ofNullable(numberOfEveryBorrowedBooks).isPresent() ? reader.getNumberOfEveryBorrowedBooks().equals(numberOfEveryBorrowedBooks) : true)
                .collect(Collectors.toList());
    }

    /**
     * Method to find parent readers with given parameters
     *
     * @param id
     * @param name
     * @param surname
     * @param birthDate
     * @param numberOfCurrentlyBorrowedBooks
     * @param numberOfEveryBorrowedBooks
     * @param address
     * @param phoneNumber
     * @return, parents with given parameters, or all parents if all parameters are null
     */
    public List<ReaderEntity> findParentByParameters(Integer id, String name, String surname, LocalDate birthDate, Integer numberOfCurrentlyBorrowedBooks, Integer numberOfEveryBorrowedBooks, String address, String phoneNumber) {
        List<ReaderEntity> readers = findReaderByParameters(id, name, surname, birthDate, numberOfCurrentlyBorrowedBooks, numberOfEveryBorrowedBooks);
        return readers.stream().filter(reader -> reader instanceof ParentEntity)
                .filter(reader -> Optional.ofNullable(address).isPresent() ? ((ParentEntity) reader).getAddress().equals(address) : true)
                .filter(reader -> Optional.ofNullable(phoneNumber).isPresent() ? ((ParentEntity) reader).getPhoneNumber().equals(phoneNumber) : true)
                .collect(Collectors.toList());
    }

    /**
     * Method to find child readers with given parameters
     *
     * @param id
     * @param name
     * @param surname
     * @param birthDate
     * @param numberOfCurrentlyBorrowedBooks
     * @param numberOfEveryBorrowedBooks
     * @param parentId
     * @return, return child readers with given parameters, or all child readers if all parameters are null
     */
    public List<ReaderEntity> findChildByParameters(Integer id, String name, String surname, LocalDate birthDate, Integer numberOfCurrentlyBorrowedBooks, Integer numberOfEveryBorrowedBooks, Integer parentId) {
        List<ReaderEntity> readers = findReaderByParameters(id, name, surname, birthDate, numberOfCurrentlyBorrowedBooks, numberOfEveryBorrowedBooks);
        return readers.stream().filter(reader -> reader instanceof ChildEntity)
                .filter(child -> Optional.ofNullable(parentId).isPresent() ? (Optional.ofNullable(((ChildEntity) child).getParent()).isPresent() ? ((ChildEntity) child).getParent().getId().equals(parentId) : true) : true)
                .collect(Collectors.toList());
    }

    /**
     * Method to get all readers from database
     *
     * @return
     */
    public List<ReaderEntity> findAllReaders() {
        return readerRepository.findAll();
    }

    /**
     * Method to delete single reader with given id from loans table, and from reader table
     *
     * @param id
     * @throws ReaderNotFoundException,               if reader with given id doesn't exist
     * @throws ReaderHasCurrentlyBookOnLoanException, if reader have currently borrowed book
     */
    @Transactional
    public void deleteReader(Integer id) throws ReaderNotFoundException, ReaderHasCurrentlyBookOnLoanException {
        ReaderEntity reader = findReaderById(id);
        List<LoanEntity> foundedLoans = findCurrentlyBorrowedBooksForReader(reader);
        if (!foundedLoans.isEmpty()) {
            throw new ReaderHasCurrentlyBookOnLoanException(READER_HAVE_CURRENTLY_BORROWED_BOOKS);
        }
        loansRepository.deleteAllByReader(reader);
        readerRepository.deleteById(id);
    }

    /**
     * Method to add parent to repository
     *
     * @param parent
     * @return
     */
    public ParentEntity addParentReader(ParentEntity parent) {
        parent.setId(null);
        parentRepository.save(parent);
        return parent;
    }

    /**
     * Method to check if given child have parent/legal guardian, and add him to repository
     *
     * @param child
     * @return
     * @throws ChildWithoutParentGuardianException, if given child to add doesn't have parent
     * @throws ParentNotFoundException,             if given child have incorrect parent
     */
    public ChildEntity addChildReader(ChildEntity child) throws ChildWithoutParentGuardianException, ParentNotFoundException {
        boolean isPresentParent = Optional.ofNullable(child.getParent()).isEmpty();
        if (isPresentParent) {
            throw new ChildWithoutParentGuardianException(YOU_HAVE_TO_GIVE_PARENT_LEGAL_GUARDIAN_ID);
        }
        isCorrectParentId(child.getParent().getId());
        child.setId(null);
        childRepository.save(child);
        return child;
    }


    /**
     * Method to edit reader with given id
     *
     * @param readerWithNewData, body with id which reader should be edited and with parameters which should be edited
     * @return
     * @throws ReaderNotFoundException,if reader with given id doesn't exist
     */
    public ReaderEntity editReader(ReaderEntity readerWithNewData) throws ReaderNotFoundException {
        Optional.ofNullable(readerWithNewData.getId()).orElseThrow(
                () -> new ReaderNotFoundException(NOT_FOUND_READER_WITH_GIVEN_ID)
        );
        ReaderEntity reader = findReaderById(readerWithNewData.getId());
        editReader(readerWithNewData, reader);

        readerRepository.save(reader);
        return reader;
    }


    /**
     * Method to edit parent reader with given id
     *
     * @param readerWithNewData, body with id which reader should be edited and with parameters which should be edited
     * @return
     * @throws ReaderNotFoundException,    if reader with given id doesn't exist
     * @throws ReaderIsNotParentException, if founded reader with given id is not parent
     */
    public ParentEntity editParent(ParentEntity readerWithNewData) throws ReaderNotFoundException, ReaderIsNotParentException {
        Optional.ofNullable(readerWithNewData.getId()).orElseThrow(
                () -> new ReaderNotFoundException(NOT_FOUND_READER_WITH_GIVEN_ID)
        );
        ReaderEntity reader = findReaderById(readerWithNewData.getId());
        boolean isParent = isParent(reader);
        if (!isParent) {
            throw new ReaderIsNotParentException(READER_IS_NOT_PARENT);
        }
        ParentEntity readerToEdit = (ParentEntity) reader;

        editReader(readerWithNewData, readerToEdit);

        Optional.ofNullable(readerWithNewData.getPhoneNumber()).ifPresent(readerToEdit::setPhoneNumber);
        Optional.ofNullable(readerWithNewData.getAddress()).ifPresent(readerToEdit::setAddress);

        parentRepository.save(readerToEdit);

        return readerToEdit;
    }

    /**
     * Method to edit child reader with given id
     *
     * @param readerWithNewData, body with id which reader should be edited and with parameters which should be edited
     * @return
     * @throws ReaderNotFoundException,   if reader with given id doesn't exist
     * @throws ParentNotFoundException,   if new parent doesn't exist
     * @throws ReaderIsNotChildException, if founded reader is not a child
     */
    public ChildEntity editChild(ChildEntity readerWithNewData) throws ReaderNotFoundException, ParentNotFoundException, ReaderIsNotChildException {
        Optional.ofNullable(readerWithNewData.getId()).orElseThrow(
                () -> new ReaderNotFoundException(NOT_FOUND_READER_WITH_GIVEN_ID)
        );
        ReaderEntity reader = findReaderById(readerWithNewData.getId());
        boolean isChild = isChild(reader);
        if (!isChild) {
            throw new ReaderIsNotChildException(READER_WITH_GIVEN_ID_IS_NOT_CHILD);
        }
        if (Optional.ofNullable(readerWithNewData.getParent()).isPresent()) {
            parentRepository.findById(readerWithNewData.getParent().getId())
                    .orElseThrow(() -> new ParentNotFoundException(INCORRECT_PARENT_LEGAL_GUARDIAN_ID));
        }

        ChildEntity readerToEdit = (ChildEntity) reader;

        editReader(readerWithNewData, readerToEdit);

        Optional.ofNullable(readerWithNewData.getParent()).ifPresent(readerToEdit::setParent);

        childRepository.save(readerToEdit);

        return readerToEdit;
    }

    /**
     * Method to check values like; reader age, reader currently borrowed books, time how long reader have books, and check if reader can borrow a book
     *
     * @param id
     * @throws ReaderNotFoundException,         if given reader doesn't exist
     * @throws ReaderTooYoungException,         if given reader is too young to borrow a book
     * @throws ReaderHaveTooMuchBooksException, if given reader have too many books to borrow another book
     * @throws ReaderHaveBooksTooLongException, if given reader have any book too long
     */
    public void isReaderCanBorrowABook(Integer id) throws ReaderNotFoundException, ReaderTooYoungException, ReaderHaveTooMuchBooksException, ReaderHaveBooksTooLongException {
        ReaderEntity reader = findReaderById(id);
        isReaderHaveCorrectAge(reader);
        isReaderHaveGotTooMuchBooks(reader);
        isReaderHaveGotBooksTooLong(reader);
    }


    /**
     * Method to add single reader
     *
     * @param reader
     * @return
     */
    public ReaderEntity addReader(ReaderEntity reader) {
        reader.setId(null);
        readerRepository.save(reader);
        return reader;
    }

    /**
     * Method to change type from child, to parent with new values
     *
     * @param id,          of child who should be edit to parent
     * @param address,     address of created parent
     * @param phoneNumber, phone number to created parent
     * @return
     * @throws ReaderNotFoundException,   if reader with given id doesn't exist
     * @throws ReaderIsNotChildException, if reader with given id is not a child
     */
    public ReaderEntity editChildToParent(Integer id, String address, String phoneNumber) throws ReaderNotFoundException, ReaderIsNotChildException {
        ReaderEntity reader = findReaderById(id);
        boolean isChild = isChild(reader);
        if (!isChild) {
            throw new ReaderIsNotChildException(READER_WITH_GIVEN_ID_IS_NOT_CHILD);
        }
        ParentEntity parent = new ParentEntity();

        editReader(reader, parent);

        parent.setId(reader.getId());

        parent.setAddress(address);
        parent.setPhoneNumber(phoneNumber);

        childRepository.deleteById(reader.getId());
        parentRepository.save(parent);

        return parent;
    }

    private boolean isCorrectParentId(Integer parentId) throws ParentNotFoundException {
        Optional<ParentEntity> parent = parentRepository.findById(parentId);
        if (parent.isEmpty()) {
            throw new ParentNotFoundException(INCORRECT_PARENT_LEGAL_GUARDIAN_ID);
        }
        return true;
    }

    private boolean isChild(ReaderEntity reader) {
        return reader instanceof ChildEntity;
    }

    private boolean isParent(ReaderEntity reader) {
        return reader instanceof ParentEntity;
    }

    private List<LoanEntity> findCurrentlyBorrowedBooksForReader(ReaderEntity reader) {
        return loansRepository.findAllByReaderAndReturnedDate(reader, null);
    }

    private List<LoanEntity> booksIdWhichReaderHaveTooMuchTime(ReaderEntity reader, Integer maxNumberOfDaysToBorrowABook) {
        List<LoanEntity> result = findCurrentlyBorrowedBooksForReader(reader);
        return result.stream().filter(loan -> {
                    LocalDate borrowDate = new Date(loan.getBorrowDate().getTime()).toLocalDate();
                    LocalDate deadLine = borrowDate.plusDays(maxNumberOfDaysToBorrowABook);
                    LocalDate currentDate = LocalDate.now();
                    return !currentDate.isBefore(deadLine);
                })
                .collect(Collectors.toList());
    }

    private ReaderEntity editReader(ReaderEntity readerWithNewData, ReaderEntity readerToEdit) {
        Optional.ofNullable(readerWithNewData.getName()).ifPresent(readerToEdit::setName);
        Optional.ofNullable(readerWithNewData.getSurname()).ifPresent(readerToEdit::setSurname);
        Optional.ofNullable(readerWithNewData.getBirthDate()).ifPresent(readerToEdit::setBirthDate);
        return readerToEdit;
    }

    private void isReaderHaveGotBooksTooLong(ReaderEntity reader) throws ReaderHaveBooksTooLongException {
        Integer maxNumberOfDaysToBorrowABook = configurationRepository.getMaxNumberOfDaysToBorrowABook();
        List<LoanEntity> loans = booksIdWhichReaderHaveTooMuchTime(reader, maxNumberOfDaysToBorrowABook);
        if (!loans.isEmpty()) {
            throw new ReaderHaveBooksTooLongException(READER_HAVE_TO_RETURN_BOOKS);
        }
    }

    private void isReaderHaveCorrectAge(ReaderEntity reader) throws ReaderTooYoungException {
        Integer minAge = configurationRepository.getMinAgeToBorrowABook();
        Integer readerAge = LocalDate.now().getYear() - reader.getBirthDate().getYear();
        if (readerAge < minAge) {
            throw new ReaderTooYoungException(READER_IS_TOO_YOUNG);
        }
    }

    private void isReaderHaveGotTooMuchBooks(ReaderEntity reader) throws ReaderHaveTooMuchBooksException {
        Integer maxNumberOfBorrowedBooks = configurationRepository.getMaxNumberOfBorrowedBooks();
        if (reader.getNumberOfCurrentlyBorrowedBooks() >= maxNumberOfBorrowedBooks) {
            throw new ReaderHaveTooMuchBooksException(READER_HAVE_TOO_MUCH_BOOKS);
        }
    }

}
