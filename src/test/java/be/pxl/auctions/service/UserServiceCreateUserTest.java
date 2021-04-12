package be.pxl.auctions.service;

import be.pxl.auctions.dao.UserDao;
import be.pxl.auctions.model.User;
import be.pxl.auctions.rest.resource.UserCreateResource;
import be.pxl.auctions.rest.resource.UserDTO;
import be.pxl.auctions.util.exception.DuplicateEmailException;
import be.pxl.auctions.util.exception.InvalidDateException;
import be.pxl.auctions.util.exception.InvalidEmailException;
import be.pxl.auctions.util.exception.RequiredFieldException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceCreateUserTest {

    private static final long USER_ID = 5L;

    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserService userService;
    private UserCreateResource userCreateResource;
    private User user;

    @BeforeEach
    public void init() {
        userCreateResource = new UserCreateResource();
        userCreateResource.setFirstName("Mark");
        userCreateResource.setLastName("Zuckerberg");
        userCreateResource.setDateOfBirth("03/05/1988");
        userCreateResource.setEmail("mark@facebook.com");

        user = new User();
        user.setId(USER_ID);
        user.setFirstName("Mark");
        user.setLastName("Zuckerberg");
        user.setDateOfBirth(LocalDate.of(1989, 5, 3));
        user.setEmail("mark@facebook.com");
    }

    @Test
    public void throwsRequiredFieldExceptionWhenFirstnameIsEmpty() {
        userCreateResource.setFirstName(null);

        assertThrows(RequiredFieldException.class, () -> userService.createUser(userCreateResource));
    }

    @Test
    void throwsRequiredFieldExceptionWhenLastnameIsEmpty() {
        userCreateResource.setLastName(null);

        assertThrows(RequiredFieldException.class, () -> userService.createUser(userCreateResource));
    }

    @Test
    void throwsRequiredFieldExceptionWhenEmailIsEmpty() {
        userCreateResource.setEmail(null);

        assertThrows(RequiredFieldException.class, () -> userService.createUser(userCreateResource));
    }

    @Test
    void throwsInvalidEmailExceptionWhenEmailIsInvalid() {
        userCreateResource.setEmail("wrong_format");

        assertThrows(InvalidEmailException.class, () -> userService.createUser(userCreateResource));
    }

    @Test
    void throwsRequiredFieldExceptionWhenDateOfBirthIsEmpty() {
        userCreateResource.setDateOfBirth(null);

        assertThrows(RequiredFieldException.class, () -> userService.createUser(userCreateResource));
    }

    @Test
    void throwsDuplicateEmailExceptionWhenEmailAlreadyExists() {
        when(userDao.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(DuplicateEmailException.class, () -> userService.createUser(userCreateResource));
    }

    @Test
    void throwsInvalidDateExceptionWhenDateOfBirthInFuture() {
        String futureDate = LocalDate.now().plusYears(1).toString();
        userCreateResource.setDateOfBirth(futureDate);

        assertThrows(InvalidDateException.class, () -> userService.createUser(userCreateResource));
    }

    @Test
    void returnsUserDtoOfUserCreateResourceIfValidUser() {
        when(userDao.saveUser(any(User.class))).thenReturn(user);

        UserDTO userDto = userService.createUser(userCreateResource);

        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getDateOfBirth(), userDto.getDateOfBirth());
        assertEquals(user.getAge(), userDto.getAge());
        assertEquals(user.getEmail(), userDto.getEmail());
    }
}
