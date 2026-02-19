package com.nextepisode.user_service.service;

import com.nextepisode.user_service.entity.user.User;
import com.nextepisode.user_service.exception.exceptions.BusinessValidationException;
import com.nextepisode.user_service.exception.exceptions.ResourceNotFoundException;
import com.nextepisode.user_service.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo;          // dependency mocked

    @InjectMocks
    private UserService userService;      // REAL service with repo injected

    private User testUser;
    private static final String TEST_USERNAME = "testuser";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername(TEST_USERNAME);
        testUser.setEmail("test@example.com"); // ✅ required

    }

    @Test
    void findByUsername_whenUserExists_returnsOptionalUser() {
        // Arrange
        when(repo.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userService.findByUsername(TEST_USERNAME);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testUser);

        // Verify interaction (proves service called repo correctly)
        verify(repo).findByUsername(TEST_USERNAME);
    }


    @Test
    void findByUsername_whenUserMissing_returnsEmptyOptional() {
        // Arrange
        when(repo.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findByUsername(TEST_USERNAME);

        // Assert
        assertThat(result).isEmpty();

        // Verify interaction (proves service called repo correctly)
        verify(repo).findByUsername(TEST_USERNAME);
    }


    @Test
    void getByUsername_whenUserExists_returnsOptionalUser() {
        // Arrange
        when(repo.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(testUser));

        // Act
        User user = userService.getUserByUsername(TEST_USERNAME);

        // Assert
        assertThat(user).isNotNull();
        assertThat(user).isEqualTo(testUser);

        // Verify interaction (proves service called repo correctly)
        verify(repo).findByUsername(TEST_USERNAME);
    }


    @Test
    void getByUsername_whenUserMissing_returnsNotFound() {
        // Arrange
        when(repo.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        // Act
        assertThatThrownBy(() -> userService.getUserByUsername(TEST_USERNAME)).isInstanceOf(ResourceNotFoundException.class).hasMessageContaining(TEST_USERNAME);

        // Verify interaction (proves service called repo correctly)
        verify(repo).findByUsername(TEST_USERNAME);
    }


    @Nested
    @DisplayName("create() method")
    class CreateTests {

        @Test
        @DisplayName("Should create user movie record successfully")
        void shouldCreateUser() {
            when(repo.save(testUser)).thenReturn(testUser);

            User savedUser = userService.createUser(testUser);

            assertThat(savedUser).isNotNull();
            assertThat(savedUser.getUsername()).isEqualTo(TEST_USERNAME);
        }

        @Test
        @DisplayName("Should throw BusinessValidationException when username is missing")
        void shouldThrowWhenUsernameMissing() {
            testUser.setUsername(null);

            assertThatThrownBy(() -> userService.createUser(testUser)).isInstanceOf(BusinessValidationException.class);

            verify(repo, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw BusinessValidationException when email is missing")
        void shouldCreateUserWhenEmailMissing() {
            testUser.setEmail(null);

            assertThatThrownBy(() -> userService.createUser(testUser)).isInstanceOf(BusinessValidationException.class);

            verify(repo, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Should throw BusinessValidationException when email is missing")
        void shouldCreateUserFromRegisteredEvent() {
            String username = "user_from_mq";
            String email = "user_from_mq@gmail.com";

            when(userService.findByUsername(username)).thenReturn(Optional.empty());

            when(repo.save(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                return user;
            });


            // Act
            userService.createUserFromRegisteredEvent(username, email);

            verify(repo, times(1)).findByUsername(username);
            verify(repo, times(1)).save(argThat(user ->
                    user.getUsername().equals(username) &&
                            user.getEmail().equals(email)
            ));
        }
    }
}
