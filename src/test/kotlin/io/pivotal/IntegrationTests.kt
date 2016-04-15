package io.pivotal

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import com.codeborne.selenide.Selenide.*
import com.codeborne.selenide.SelenideElement
import com.codeborne.selenide.Condition.*
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import kotlin.test.*

@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(BinaryLoyaltyApplication::class))
@WebAppConfiguration
class IntegrationTests {

    fun jq(arg : String): SelenideElement {
        return `$`(arg)
    }

    @Test
    fun whenCreateGameIsClicked_userSeesAGameCodeAndOnePlayer() {
        open("/")
        jq("#create_game_submit").click()
        assertNotEquals(jq("#game_code").text(),"")
    }

    @Test
    fun whenExistingGameCodeIsJoined_userSeesOtherPlayers() {
        open("/")
        jq("#create_game_submit").click()
        var gameCode = jq("#game_code").text()
        open("/")
        jq("#enter_game_code").value = gameCode
        jq("#join_game_submit").click()
        jq("#enter_user_name").value = "Alice"
        jq("#user_info_submit").click()

        assertEquals(jq("#user_name").text(),"Alice")
        assertEquals(jq("#game_code").text(),gameCode)
        jq("body").shouldHave(text("System"))
        jq("body").shouldHave(text("Alice"))
    }

    @Test
    fun whenStartGameAPIIsCalled_itStartsTheGameAfterFifteenSeconds() {
        open("/")
        jq("#create_game_submit").click()
        var gameCode = jq("#game_code").text()

        open("/api/game/" + gameCode)
            jq("body").shouldHave(text("UNSTARTED"))
        var httppost = HttpPost("http://localhost:8080//api/startGame/" + gameCode)
        HttpClients.createDefault().execute(httppost)
        open("/api/game/" + gameCode)
        jq("body").shouldHave(text("STARTING"))
        Thread.sleep(16*1000)
        open("/api/game/" + gameCode)
        jq("body").shouldHave(text("STARTED"))
    }
}