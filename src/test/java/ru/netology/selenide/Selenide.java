package ru.netology.selenide;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;

class RegisterDelivery {

    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
        open("http://localhost:7777");
    }

    String generateDate(int days) {
        return
                LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    // Корректное заполнение формы
    @Test
    void shouldRegisteredDelivery() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");

        $("[data-test-id='date'] input").click(); // очищаем поле даты от условия по умолчанию
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);

        String dateInput = generateDate(5); // генерим новую дату = текущая + 5 дней
        $("[data-test-id='date'] input").setValue(dateInput); // заполняем поле даты своим значением

        $("[data-test-id='name'] input").setValue("Демьян Александров");
        $("[data-test-id='phone'] input").setValue("+79200000001");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $$("[data-test-id='notification']").find(exactText("Успешно! Встреча успешно забронирована на " + dateInput));
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
    }

    // Некорректное заполнение - прошедшая дата
    @Test
    void shouldNotRegisteredDelivery() {
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");

        $("[data-test-id='date'] input").click(); // очищаем поле даты от условия по умолчанию
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);

        String dateInput = generateDate(-5); // генерим новую дату = текущая - 5 дней
        $("[data-test-id='date'] input").setValue(dateInput); // заполняем поле даты своим значением

        $("[data-test-id='name'] input").setValue("Демьян Александров");
        $("[data-test-id='phone'] input").setValue("+79200000001");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }
}
