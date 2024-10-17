package com.marketplace.marketplace.user;

import com.marketplace.marketplace.exceptions.UserInvalidArgumentsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {


    @Autowired
    private UserService userService;

    @MockBean
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
}
