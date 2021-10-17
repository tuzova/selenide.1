package ru.netology.selenide;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;

class RegisterDelivery {

    Calendar c = new GregorianCalendar();

    @BeforeEach
    void setUp() {
        Configuration.browser = "chrome";
    }

    // Корректное заполнение формы
    @Test
    void shouldRegisteredDelivery() {
        open("http://localhost:7777");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");

        c.add(Calendar.DAY_OF_YEAR, 5); // устанавливаем дату = текущая + 5 дней
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy"); // переводим дату в нужный формат
        String dateInput = format1.format(c.getTime()); // дату в нужном формате присваиваем переменной

        $("[data-test-id='date'] input").click(); // очищаем поле даты от условия по умолчанию
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(dateInput); // заполняем поле даты своим значением

        $("[data-test-id='name'] input").setValue("Демьян Александров");
        $("[data-test-id='phone'] input").setValue("+79200000001");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
    }

    // Некорректное заполнение - прошедшая дата
    @Test
    void shouldNotRegisteredDelivery() {
        open("http://localhost:7777");
        $("[data-test-id='city'] input").setValue("Санкт-Петербург");

        c.add(Calendar.DAY_OF_YEAR, -5); // устанавливаем дату = текущая - 5 дней
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy"); // переводим дату в нужный формат
        String dateInput = format1.format(c.getTime()); // дату в нужном формате присваиваем переменной

        $("[data-test-id='date'] input").click(); // очищаем поле даты от условия по умолчанию
        $("[data-test-id='date'] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(dateInput); // заполняем поле даты своим значением

        $("[data-test-id='name'] input").setValue("Демьян Александров");
        $("[data-test-id='phone'] input").setValue("+79200000001");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='date'] .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));

    }
}
