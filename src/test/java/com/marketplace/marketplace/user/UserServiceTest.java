package com.marketplace.marketplace.user;

import com.marketplace.marketplace.DTO.UpdateUser;
import com.marketplace.marketplace.exceptions.UserInvalidArgumentsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    private UserRepository userRepository;


    @Test
    void UserServiceSaveUser_ValidUser_SavedUser() {
        User userToSave = User.builder().sub("abc").email("mail@mail.com").build();

        User wantedUser = userToSave;

        doReturn(userToSave).when(userRepository).save(any(User.class));

        User savedUser = userService.saveUser(userToSave);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo(wantedUser.getEmail());
        assertThat(savedUser.getSub()).isEqualTo(wantedUser.getSub());
    }

    @ParameterizedTest
    @MethodSource("UserServiceSaveUser_InvalidUser_SavedUser_TestData")
    void UserServiceSaveUser_InvalidUser_SavedUser(String sub, String email, Class<? extends Exception> expectedException) {
        User userToSave = User.builder().sub(sub).email(email).build();
        User wantedUser = userToSave;

        assertThatThrownBy(() -> userService.saveUser(userToSave))
                .isInstanceOf(expectedException);
    }

    private static Stream<Arguments> UserServiceSaveUser_InvalidUser_SavedUser_TestData() {
        return Stream.of(
                Arguments.of(null, null, UserInvalidArgumentsException.class),
                Arguments.of("", null, UserInvalidArgumentsException.class),
                Arguments.of(null, "", UserInvalidArgumentsException.class),
                Arguments.of("", "", UserInvalidArgumentsException.class),
                Arguments.of(" ", "mail@mail.com", UserInvalidArgumentsException.class),
                Arguments.of("sud", " ", UserInvalidArgumentsException.class),
                Arguments.of("sud", "nomail", UserInvalidArgumentsException.class)
        );
    }

    @Test
    void UserServiceUpdateUser_ValidUpdateUser_UpdatedUser() {
        String validNumber = "+49 151 12345678";

        User userToUpdate = User.builder()
                .sub("somesub")
                .email("someemail")
                .phoneNumber("+4911111111")
                .build();

        UpdateUser updateUser = UpdateUser.builder()
                .phoneNumber(validNumber)
                .build();


        UserService userServiceMock = Mockito.spy(userService);
        doAnswer(invocation -> invocation.getArgument(0))
                .when(userServiceMock).saveUser(any(User.class));

        User result = userServiceMock.updateUser(userToUpdate, updateUser);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(userToUpdate.getEmail());
        assertThat(result.getSub()).isEqualTo(userToUpdate.getSub());
        assertThat(result.getPhoneNumber()).isEqualTo(validNumber);
    }

    @ParameterizedTest
    @CsvSource({
            "' '",
            "''",
            "abc",
            "+493333",
            "+fkn"
    })
    void UserServiceUpdateUser_InvalidUpdateUser_UpdatedUser(String phoneNumber) {
        String originalPhoneNumber = "+4911111111";

        User userToUpdate = User.builder()
                .sub("somesub")
                .email("someemail")
                .phoneNumber(originalPhoneNumber)
                .build();

        UpdateUser updateUser = UpdateUser.builder()
                .phoneNumber(phoneNumber)
                .build();

        UserService userServiceMock = Mockito.spy(userService);

        assertThatThrownBy(() -> userService.updateUser(userToUpdate, updateUser))
                .isInstanceOf(UserInvalidArgumentsException.class);
    }



}
