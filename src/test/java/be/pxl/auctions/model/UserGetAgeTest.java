package be.pxl.auctions.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserGetAgeTest {


    @Test
    public void returnsCorrectAgeWhenHavingBirthdayToday() {
        User user = UserBuilder.anUser()
                .withDateOfBirth(LocalDate.now().minusYears(30))
                .build();

        assertEquals(30, user.getAge());
    }

    @Test
    public void returnsCorrectAgeWhenHavingBirthdayTomorrow() {
        User user = UserBuilder.anUser()
                .withDateOfBirth(LocalDate.now().plusDays(1).minusYears(30))
                .build();

        assertEquals(29, user.getAge());
    }

    @Test
    public void returnsCorrectAgeWhenBirthdayWasYesterday() {
        User user = UserBuilder.anUser()
                .withDateOfBirth(LocalDate.now().minusDays(1).minusYears(30))
                .build();

        assertEquals(30, user.getAge());
    }

}
