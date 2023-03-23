package rru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import rru.yandex.practicum.filmorate.controller.UserController;
import rru.yandex.practicum.filmorate.exceptions.ValidationException;
import rru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {

    @Test
    public void validateUserWithLoginBlank() {
        User user = new User();
        user.setName("Ольга");
        user.setLogin("");
        user.setEmail("Olga@email.ru");
        user.setBirthday(LocalDate.of(1985, 6, 15));

        assertThrows(ValidationException.class, () -> UserController.validationUser(user));
    }

    @Test
    public void validateUserWithLoginWrong() {
        User user = new User();
        user.setName("Глеб");
        user.setLogin("Gleb b");
        user.setEmail("gleb@email.ru");
        user.setBirthday(LocalDate.of(1992, 11, 10));

        assertThrows(ValidationException.class, () -> UserController.validationUser(user));
    }

    @Test
    public void validateUserWithNullEmail() {
        User user = new User();
        user.setName("Павел");
        user.setLogin("Pavel");
        user.setBirthday(LocalDate.of(1975, 11, 12));

        assertThrows(ValidationException.class, () -> UserController.validationUser(user));
    }

    @Test
    public void validateUserWithWrongEmail() {
        User user = new User();
        user.setName("Alex");
        user.setLogin("Alex10");
        user.setEmail("alexemail.ru");
        user.setBirthday(LocalDate.of(1991, 5, 15));

        assertThrows(ValidationException.class, () -> UserController.validationUser(user));
    }

    @Test
    public void validateUserWithBlankEmail() {
        User user = new User();
        user.setName("Мария");
        user.setLogin("Mari");
        user.setEmail("");
        user.setBirthday(LocalDate.of(1990, 11, 10));

        assertThrows(ValidationException.class, () -> UserController.validationUser(user));
    }

    @Test
    public void withWrongBirthday() {
        User user = new User();
        user.setName("Natali");
        user.setLogin("Natali7");
        user.setEmail("natali@email.ru");
        user.setBirthday(LocalDate.of(2028, 5, 16));

        assertThrows(ValidationException.class, () -> UserController.validationUser(user));
    }
}
