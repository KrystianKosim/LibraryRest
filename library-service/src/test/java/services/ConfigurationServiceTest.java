package services;

import com.company.repository.models.repository.ConfigurationRepository;
import com.company.service.ConfigurationService;
import com.company.service.exceptions.ConfigurationValueIncorrectException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;

@ExtendWith(MockitoExtension.class)
@TestExecutionListeners(MockitoTestExecutionListener.class)
@ContextConfiguration
@AutoConfigureMockMvc
public class ConfigurationServiceTest {

    @Mock
    private ConfigurationRepository configurationRepository;
    @InjectMocks
    private ConfigurationService configurationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        configurationService = new ConfigurationService(configurationRepository);
    }

    @Test
    @DisplayName("Should throw exception because given age is incorrect")
    void shouldThrowExceptionBecauseGivenAgeIsIncorrect() {
        //given
        Integer age = -1;
        //when
        Exception result = Assertions.assertThrows(ConfigurationValueIncorrectException.class,
                () -> configurationService.editMinAgeToBorrowABook(age));
        //then
        Assertions.assertEquals(ConfigurationService.INCORRECT_VALUE, result.getMessage());
    }

    @Test
    @DisplayName("Should throw exception because number of max books is incorrect")
    void shouldThrowExceptionBecauseNumberOfMaxBooksIsIncorrect() {
        //given
        Integer maxBooks = -1;
        //when
        Exception result = Assertions.assertThrows(ConfigurationValueIncorrectException.class,
                () -> configurationService.editNumberOfBorrowedBooks(maxBooks));
        //then
        Assertions.assertEquals(ConfigurationService.INCORRECT_VALUE, result.getMessage());
    }

    @Test
    @DisplayName("Should throw exception because number of days is incorrect")
    void shouldThrowExceptionBecauseNumberOfDaysIsIncorrect() {
        //given
        Integer maxDays = -1;
        //when
        Exception result = Assertions.assertThrows(ConfigurationValueIncorrectException.class,
                () -> configurationService.editNumberOfDaysToBorrowABook(maxDays));
        //then
        Assertions.assertEquals(ConfigurationService.INCORRECT_VALUE, result.getMessage());
    }
}
