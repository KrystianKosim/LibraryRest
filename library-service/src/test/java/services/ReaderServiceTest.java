package services;

import com.company.repository.models.entity.ChildEntity;
import com.company.repository.models.entity.LoanEntity;
import com.company.repository.models.entity.ParentEntity;
import com.company.repository.models.entity.ReaderEntity;
import com.company.repository.models.repository.*;
import com.company.service.ReaderService;
import com.company.service.exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static services.TestUtils.*;

@ExtendWith(MockitoExtension.class)
@TestExecutionListeners(MockitoTestExecutionListener.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ContextConfiguration
@AutoConfigureMockMvc
public class ReaderServiceTest {

    @Mock
    private ReaderRepository mockReaderRepository;
    @Mock
    private ParentRepository mockParentRepository;
    @Mock
    private ChildRepository mockChildRepository;
    @Mock
    private LoansRepository mockLoansRepository;
    @Mock
    private ConfigurationRepository mockConfigurationRepository;
    @InjectMocks
    private ReaderService readerService;

    private List<ReaderEntity> readers;
    private List<ReaderEntity> parents;
    private List<ReaderEntity> childs;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        readerService = new ReaderService(mockReaderRepository, mockParentRepository, mockChildRepository, mockLoansRepository, mockConfigurationRepository);
        readers = createReaders();
        parents = createParents();
        childs = createChilds();
    }

    @Test
    @DisplayName("Should return list of books id which reader have too long")
    void shouldReturnListOfBooksIdWhichReaderHaveTooLong() throws ReaderNotFoundException {
        //given
        ReaderEntity reader = readers.get(0);
        Integer id = reader.getId();
        Integer maxNumberOfDaysToBorrowABook = 2;
        LoanEntity loan = new LoanEntity();
        loan.setBorrowDate(Date.valueOf(LocalDate.now().minusDays(maxNumberOfDaysToBorrowABook + 1)));

        when(mockReaderRepository.findById(id)).thenReturn(Optional.of(reader));
        when(mockConfigurationRepository.getMaxNumberOfDaysToBorrowABook()).thenReturn(maxNumberOfDaysToBorrowABook);
        when(mockLoansRepository.findAllByReaderAndReturnedDate(reader, null)).thenReturn(List.of(loan));
        //when
        List<LoanEntity> result = readerService.booksIdWhichReaderHaveTooMuchTime(id);
        //then
        Assertions.assertEquals(List.of(loan), result);
    }

    @Test
    @DisplayName("Should return empty list because reader haven't got any book to long")
    void shouldReturnEmptyListBecauseReaderHaventGotAnyBookToLong() throws ReaderNotFoundException {
        //given
        ReaderEntity reader = readers.get(0);
        Integer id = reader.getId();
        Integer maxNumberOfDaysToBorrowABook = 2;
        LoanEntity loan = new LoanEntity();
        loan.setBorrowDate(Date.valueOf(LocalDate.now().minusDays(maxNumberOfDaysToBorrowABook + 1)));

        when(mockReaderRepository.findById(id)).thenReturn(Optional.of(reader));
        when(mockConfigurationRepository.getMaxNumberOfDaysToBorrowABook()).thenReturn(maxNumberOfDaysToBorrowABook);
        when(mockLoansRepository.findAllByReaderAndReturnedDate(reader, null)).thenReturn(List.of());
        //when
        List<LoanEntity> result = readerService.booksIdWhichReaderHaveTooMuchTime(id);
        //then
        Assertions.assertEquals(List.of(), result);
    }

    @Test
    @DisplayName("Should return readers with given parameters")
    void shouldReturnReadersWithGivenParameters() {
        //given
        ReaderEntity reader = readers.get(0);
        Integer idToFind = reader.getId();

        when(mockReaderRepository.findAll()).thenReturn(readers);
        //when
        List<ReaderEntity> result = readerService.findReaderByParameters(idToFind, null, null, null, null, null);
        //then
        Assertions.assertEquals(List.of(reader), result);
    }

    @Test
    @DisplayName("Should return empty list because couldn't find reader with given parameters")
    void shouldReturnEmptyListBecauseCouldntFindReaderWithGivenParameters() {
        //given
        ReaderEntity reader = readers.get(0);
        Integer idToFind = 99;

        when(mockReaderRepository.findAll()).thenReturn(readers);
        //when
        List<ReaderEntity> result = readerService.findReaderByParameters(idToFind, null, null, null, null, null);
        //then
        Assertions.assertEquals(List.of(), result);
    }

    @Test
    @DisplayName("Should return all readers because not given filter parameters")
    void shouldReturnAllReadersBecauseNotGivenFilterParameters() {
        //given
        ReaderEntity reader = readers.get(0);
        Integer idToFind = reader.getId();

        when(mockReaderRepository.findAll()).thenReturn(readers);
        //when
        List<ReaderEntity> result = readerService.findReaderByParameters(null, null, null, null, null, null);
        //then
        Assertions.assertEquals(readers, result);
    }

    @Test
    @DisplayName("Should return parent reader with given parameters")
    void shouldReturnParentReaderWithGivenParameters() {
        //given
        ParentEntity parent = (ParentEntity) parents.get(0);
        String phoneToFind = parent.getPhoneNumber();

        when(mockReaderRepository.findAll()).thenReturn(parents);
        //when
        List<ReaderEntity> result = readerService.findParentByParameters(null, null, null, null, null,
                null, null, phoneToFind);
        //then
        Assertions.assertEquals(List.of(parent), result);
    }

    @Test
    @DisplayName("Should return empty list because couldn't find any parent with given parameters")
    void shouldReturnEmptyListBecauseCouldntFindAnyParentWithGivenParameters() {
        //given
        String phoneToFind = "phone3333333";
        String address = "addresssss";

        when(mockReaderRepository.findAll()).thenReturn(parents);
        //when
        List<ReaderEntity> result = readerService.findParentByParameters(null, null, null, null,
                null, null, address, phoneToFind);
        //then
        Assertions.assertEquals(List.of(), result);
    }

    @Test
    @DisplayName("Should return all parents because not given any filter parameters")
    void shouldReturnAllParentsBecauseNotGivenAnyFilterParameters() {
        //given
        when(mockReaderRepository.findAll()).thenReturn(parents);
        //when
        List<ReaderEntity> result = readerService.findParentByParameters(null, null, null, null, null,
                null, null, null);
        //then
        Assertions.assertEquals(parents, result);
    }

    @Test
    @DisplayName("Should return child with given parent id")
    void shouldReturnChildWithGivenParentId() {
        //given
        ChildEntity child = (ChildEntity) childs.get(0);

        Integer parentId = child.getParent().getId();

        when(mockReaderRepository.findAll()).thenReturn(childs);
        //when
        List<ReaderEntity> result = readerService.findChildByParameters(null, null, null, null,
                null, null, parentId);
        //then
        Assertions.assertEquals(List.of(child), result);
    }

    @Test
    @DisplayName("Should return empty list because couldn't find any child with given parameters")
    void shouldReturnEmptyListBecauseCouldntFindAnyChildWithGivenParameters() {
        //given
        Integer parentId = 99;

        when(mockReaderRepository.findAll()).thenReturn(childs);
        //when
        List<ReaderEntity> result = readerService.findChildByParameters(null, null, null, null,
                null, null, parentId);
        //then
        Assertions.assertEquals(List.of(), result);
    }

    @Test
    @DisplayName("Should return all child because not given any filter parameters")
    void shouldReturnAllChildBecauseNotGivenAnyFilterParameters() {
        //given
        when(mockReaderRepository.findAll()).thenReturn(childs);
        //when
        List<ReaderEntity> result = readerService.findChildByParameters(null, null, null, null,
                null, null, null);
        //then
        Assertions.assertEquals(childs, result);
    }

    @Test
    @DisplayName("Should throw exception while delete because reader have currently borrowed books")
    void shouldThrowExceptionWhileDeleteBecauseReaderHaveCurrentlyBorrowedBooks() {
        //given
        ReaderEntity reader = readers.get(0);
        Integer id = reader.getId();
        when(mockReaderRepository.findById(id)).thenReturn(Optional.of(reader));
        when(mockLoansRepository.findAllByReaderAndReturnedDate(reader, null)).thenReturn(List.of(new LoanEntity()));
        //when
        Exception result = Assertions.assertThrows(ReaderHasCurrentlyBookOnLoanException.class,
                () -> readerService.deleteReader(id));
        //then
        Assertions.assertEquals(ReaderService.READER_HAVE_CURRENTLY_BORROWED_BOOKS, result.getMessage());
    }

    @Test
    @DisplayName("Should add parent reader with given parameters")
    void shouldAddParentReaderWithGivenParameters() {
        //given
        ParentEntity parent = (ParentEntity) parents.get(0);
        //when
        ParentEntity result = readerService.addParentReader(parent);
        //then
        Assertions.assertEquals(parent, result);
    }

    @Test
    @DisplayName("Should add child reader")
    void shouldAddChildReader() throws ParentNotFoundException, ChildWithoutParentGuardianException {
        //given
        ChildEntity child = (ChildEntity) childs.get(0);
        ParentEntity parent = (ParentEntity) parents.get(0);

        when(mockParentRepository.findById(parent.getId())).thenReturn(Optional.of(parent));
        //when
        ChildEntity result = readerService.addChildReader(child);
        //then
        Assertions.assertEquals(child, result);
    }

    @Test
    @DisplayName("Should throw exception because reader doesn't have parent")
    void shouldThrowExceptionBecauseReaderDoesntHaveParent() {
        //given
        ChildEntity child = (ChildEntity) childs.get(0);
        child.setParent(null);
        //when
        Exception result = Assertions.assertThrows(ChildWithoutParentGuardianException.class,
                () -> readerService.addChildReader(child));
        //then
        Assertions.assertEquals(ReaderService.YOU_HAVE_TO_GIVE_PARENT_LEGAL_GUARDIAN_ID, result.getMessage());
    }

    @Test
    @DisplayName("Should throw exception because parent id is incorrect")
    void shouldThrowExceptionBecauseParentIdIsIncorrect() {
        //given
        ChildEntity child = (ChildEntity) childs.get(0);
        ParentEntity parent = new ParentEntity();
        parent.setId(99);
        child.setParent(parent);
        when(mockParentRepository.findById(parent.getId())).thenReturn(Optional.empty());
        //when
        Exception result = Assertions.assertThrows(ParentNotFoundException.class,
                () -> readerService.addChildReader(child));
        //then
        Assertions.assertEquals(ReaderService.INCORRECT_PARENT_LEGAL_GUARDIAN_ID, result.getMessage());
    }

    @Test
    @DisplayName("Should edit given parent")
    void shouldEditGivenParent() throws ReaderIsNotParentException, ReaderNotFoundException {
        //given
        ReaderEntity readerToEdit = parents.get(0);

        ParentEntity parentWithNewData = new ParentEntity();
        String address = "new address";
        parentWithNewData.setId(readerToEdit.getId());
        parentWithNewData.setAddress(address);

        when(mockReaderRepository.findById(readerToEdit.getId())).thenReturn(Optional.of(readerToEdit));
        //when
        ReaderEntity result = readerService.editParent(parentWithNewData);
        //then
        Assertions.assertEquals(address, ((ParentEntity) result).getAddress());
    }


    @Test
    @DisplayName("Should edit child to parent")
    void shouldEditChildToParent() throws ReaderNotFoundException, ReaderIsNotChildException {
        //given
        ReaderEntity child = childs.get(0);
        String address = "address";
        String phoneNumber = "phoneNum";
        when(mockReaderRepository.findById(child.getId())).thenReturn(Optional.of(child));
        //when
        ParentEntity result = (ParentEntity) readerService.editChildToParent(child.getId(), address, phoneNumber);
        //then
        Assertions.assertEquals(child.getName(), result.getName());
        Assertions.assertEquals(child.getSurname(), result.getSurname());
        Assertions.assertEquals(child.getBirthDate(), result.getBirthDate());
        Assertions.assertEquals(child.getName(), result.getName());
        Assertions.assertEquals(child.getId(), result.getId());
    }

    @Test
    @DisplayName("Should throw exception because child id is not present to edit")
    void shouldThrowExceptionBecauseChildIdIsNotPresentToEdit() {
        //given
        ChildEntity childWithNewData = new ChildEntity();
        //when
        Exception result = Assertions.assertThrows(ReaderNotFoundException.class,
                () -> readerService.editChild(childWithNewData));
        //then
        Assertions.assertEquals(ReaderService.NOT_FOUND_READER_WITH_GIVEN_ID, result.getMessage());
    }

    @Test
    @DisplayName("Should throw exception because parent id is incorrect while edit child")
    void shouldThrwoExceptionBecauseParentIdIsIncorrectWhileEditChild() {
        //given
        ChildEntity child = (ChildEntity) childs.get(0);
        Integer id = child.getId();
        ParentEntity parent = (ParentEntity) parents.get(0);
        ChildEntity childWithNewData = new ChildEntity();
        childWithNewData.setId(id);
        childWithNewData.setParent(parent);
        when(mockReaderRepository.findById(id)).thenReturn(Optional.of(child));
        when(mockParentRepository.findById(parent.getId())).thenReturn(Optional.empty());
        //when
        Exception result = Assertions.assertThrows(ParentNotFoundException.class,
                () -> readerService.editChild(childWithNewData));
        //then
        Assertions.assertEquals(ReaderService.INCORRECT_PARENT_LEGAL_GUARDIAN_ID, result.getMessage());
    }

    @Test
    @DisplayName("Should throw exception because given reader is not child while edit")
    void shouldThrowExceptionBecauseGivenReaderIsNotChildException() {
        //given
        String address = "address";
        String phoneNum = "phone";
        ReaderEntity reader = readers.get(0);
        when(mockReaderRepository.findById(reader.getId())).thenReturn(Optional.of(reader));
        //when
        Exception result = Assertions.assertThrows(ReaderIsNotChildException.class,
                () -> readerService.editChildToParent(reader.getId(), address, phoneNum));
        //then
        Assertions.assertEquals(ReaderService.READER_WITH_GIVEN_ID_IS_NOT_CHILD, result.getMessage());
    }

    @Test
    @DisplayName("Should edit child values")
    void shouldEditValuesOfChildReader() throws ReaderNotFoundException, ParentNotFoundException, ReaderIsNotChildException {
        //given
        ChildEntity child = (ChildEntity) childs.get(0);
        ChildEntity childWithNewValues = new ChildEntity();
        String name = "new name";
        childWithNewValues.setId(child.getId());
        childWithNewValues.setName(name);
        when(mockReaderRepository.findById(child.getId())).thenReturn(Optional.of(child));
        //when
        ChildEntity result = readerService.editChild(childWithNewValues);
        //then
        Assertions.assertEquals(name, result.getName());
    }


    @Test
    @DisplayName("Should edit reader surname")
    void shouldEditReaderSurname() throws ReaderNotFoundException {
        //given
        ReaderEntity reader = readers.get(0);
        ReaderEntity readerWithNewData = new ReaderEntity();
        String surname = "new surname";
        readerWithNewData.setSurname(surname);
        readerWithNewData.setId(reader.getId());
        when(mockReaderRepository.findById(reader.getId())).thenReturn(Optional.of(reader));
        //when
        ReaderEntity result = readerService.editReader(readerWithNewData);
        //then
        Assertions.assertEquals(surname, result.getSurname());
    }

    @Test
    @DisplayName("Should throw exception because reader id is not present")
    void shouldThrowExceptionBecauseReaderIdIsNotPresent() {
        //given
        ReaderEntity readerWithNewData = new ReaderEntity();
        readerWithNewData.setId(null);
        //when
        Exception result = Assertions.assertThrows(ReaderNotFoundException.class,
                () -> readerService.editReader(readerWithNewData));
        //then
        Assertions.assertEquals(ReaderService.NOT_FOUND_READER_WITH_GIVEN_ID, result.getMessage());
    }

    @Test
    @DisplayName("Should throw exception because parent id is not present")
    void shouldThrowExceptionBecauseParentIdIsNotPresent() {
        //given
        ParentEntity parentWithNewData = new ParentEntity();
        parentWithNewData.setId(null);
        //when
        Exception result = Assertions.assertThrows(ReaderNotFoundException.class,
                () -> readerService.editParent(parentWithNewData));
        //then
        Assertions.assertEquals(ReaderService.NOT_FOUND_READER_WITH_GIVEN_ID, result.getMessage());
    }

    @Test
    @DisplayName("Should return the same reader because not given parameters")
    void shouldReturnTheSameReaderBecauseNotGivenParameters() throws ReaderNotFoundException {
        //given
        ReaderEntity reader = readers.get(0);
        ReaderEntity readerWithNewParameters = new ReaderEntity();
        readerWithNewParameters.setId(reader.getId());
        when(mockReaderRepository.findById(reader.getId())).thenReturn(Optional.of(reader));
        //when
        ReaderEntity result = readerService.editReader(readerWithNewParameters);
        //then
        Assertions.assertEquals(reader, result);
    }

    @Test
    @DisplayName("Should return reader with given id")
    void shouldReturnReaderWithGivenId() throws ReaderNotFoundException {
        //given
        ReaderEntity readerEntity = readers.get(0);
        Integer id = readerEntity.getId();

        when(mockReaderRepository.findById(any(Integer.class))).thenReturn(Optional.of(readerEntity));
        //when
        ReaderEntity result = readerService.findReaderById(id);
        //then
        Assertions.assertEquals(readerEntity, result);
    }

    @Test
    @DisplayName("Should throw exception because couldn't find reader with given id")
    void shouldThrowExceptionBecauseCouldntFindReaderWithGivenId() {
        //given
        Integer id = 99;

        when(mockReaderRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        //when
        Exception result = Assertions.assertThrows(ReaderNotFoundException.class,
                () -> readerService.findReaderById(id));
        //then
        Assertions.assertEquals(ReaderService.NOT_FOUND_READER_WITH_GIVEN_ID, result.getMessage());
    }

    @Test
    @DisplayName("Should throw exception because reader is too young")
    void shouldThrowExceptionBecauseReaderIsTooYoung() {
        //given
        ReaderEntity reader = readers.get(1);
        Integer id = reader.getId();
        Integer currentYear = LocalDate.now().getYear();
        Integer minAge = currentYear - reader.getBirthDate().getYear() + 1;

        when(mockReaderRepository.findById(id)).thenReturn(Optional.of(reader));
        when(mockConfigurationRepository.getMinAgeToBorrowABook()).thenReturn(minAge);
        //when
        Exception result = Assertions.assertThrows(ReaderTooYoungException.class,
                () -> readerService.isReaderCanBorrowABook(id));
        //then
        Assertions.assertEquals(ReaderService.READER_IS_TOO_YOUNG, result.getMessage());
    }

    @Test
    @DisplayName("Should throw exception because reader have too many books")
    void shouldThrowExceptionBecauseReaderHaveTooManyBooks() {
        //given
        ReaderEntity reader = readers.get(1);
        Integer id = reader.getId();
        Integer currentYear = LocalDate.now().getYear();
        Integer minAge = currentYear - reader.getBirthDate().getYear() - 1;
        Integer maxNumberOfBooks = 6;
        reader.setNumberOfCurrentlyBorrowedBooks(maxNumberOfBooks);
        when(mockReaderRepository.findById(id)).thenReturn(Optional.of(reader));
        when(mockConfigurationRepository.getMinAgeToBorrowABook()).thenReturn(minAge);
        when(mockConfigurationRepository.getMaxNumberOfBorrowedBooks()).thenReturn(maxNumberOfBooks);
        //when
        Exception result = Assertions.assertThrows(ReaderHaveTooMuchBooksException.class,
                () -> readerService.isReaderCanBorrowABook(id));
        //then
        Assertions.assertEquals(ReaderService.READER_HAVE_TOO_MUCH_BOOKS, result.getMessage());
    }

    @Test
    @DisplayName("Should throw exception because reader have book too long")
    void shouldThrowExceptionBecauseReaderHaveBookTooLong() {
        //given
        ReaderEntity reader = readers.get(1);
        Integer id = reader.getId();
        Integer currentYear = LocalDate.now().getYear();
        Integer minAge = currentYear - reader.getBirthDate().getYear() - 1;
        Integer maxNumberOfBooks = 6;
        Integer maxNumberOfDaysToBorrowABook = 2;
        reader.setNumberOfCurrentlyBorrowedBooks(maxNumberOfBooks - 1);
        when(mockReaderRepository.findById(id)).thenReturn(Optional.of(reader));
        when(mockConfigurationRepository.getMinAgeToBorrowABook()).thenReturn(minAge);
        when(mockConfigurationRepository.getMaxNumberOfBorrowedBooks()).thenReturn(maxNumberOfBooks);
        when(mockConfigurationRepository.getMaxNumberOfDaysToBorrowABook()).thenReturn(maxNumberOfDaysToBorrowABook);
        LoanEntity loan = new LoanEntity();
        loan.setBorrowDate(Date.valueOf(LocalDate.now().minusDays(maxNumberOfDaysToBorrowABook + 1)));
        when(mockLoansRepository.findAllByReaderAndReturnedDate(reader, null)).thenReturn(List.of(loan));
        //when
        Exception result = Assertions.assertThrows(ReaderHaveBooksTooLongException.class,
                () -> readerService.isReaderCanBorrowABook(id));
        //then
        Assertions.assertEquals(ReaderService.READER_HAVE_TO_RETURN_BOOKS, result.getMessage());
    }


    @Test
    @DisplayName("Should add reader")
    void shouldAddReader() {
        //given
        ReaderEntity readerEntity = readers.get(0);
        //when
        ReaderEntity result = readerService.addReader(readerEntity);
        //then
        Assertions.assertEquals(readerEntity, result);
    }
}
