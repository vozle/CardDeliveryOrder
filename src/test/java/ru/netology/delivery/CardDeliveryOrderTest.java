package ru.netology.delivery;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryOrderTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
//        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999/");
    }

    @Test
    public void shouldTestPlanningDateWithRePlanning() {
        var daysToFirstMeeting = 3;
        var firstMeetingDate = DataGenerator.generateDate(daysToFirstMeeting);
        var daysToSecondMeeting = 6;
        var secondMeetingDate = DataGenerator.generateDate(daysToSecondMeeting);

        $("[placeholder=\"Город\"]").setValue(DataGenerator.generateCity("ru"));
        $("[placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[placeholder=\"Дата встречи\"]").setValue(DataGenerator.generateDate(3));
        $("[name=\"name\"]").setValue(DataGenerator.generateName("ru"));
        $("[name=\"phone\"]").setValue(DataGenerator.generatePhone("ru"));
        $("[data-test-id=\"agreement\"]").click();
        $(byText("Запланировать")).click();
        $("[data-test-id=\"success-notification\"]").shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15));
        $("[placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[placeholder=\"Дата встречи\"]").setValue(DataGenerator.generateDate(6));
        $(byText("Запланировать")).click();
        $("[data-test-id=\"replan-notification\"]").shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"), Duration.ofSeconds(15));
        $(byText("Перепланировать")).click();
        $("[data-test-id=\"success-notification\"]").shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15));
    }

}