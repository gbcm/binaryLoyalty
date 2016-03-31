package io.pivotal

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import com.codeborne.selenide.Selenide.*;
import com.codeborne.selenide.SelenideElement
import kotlin.test.*;

@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(BinaryLoyaltyApplication::class))
@WebAppConfiguration
class IntegrationTests {

    fun jq(arg : String): SelenideElement {
        return `$`(arg)
    }

    @Test
    fun whenStartGameIsClicked_userSeesAGameCodeAndOnePlayer() {
        open("/")
        jq("#start_game_submit").click()
        assertNotEquals(jq("#game_code").text(),"")
        assertEquals(jq("#num_players").text(),"1")
    }

    @Test
    fun whenExistingGameCodeIsJoined_userSeesNumberOfPlayers() {
        open("/")
        jq("#start_game_submit").click()
        var gameCode = jq("#game_code").text()
        open("/")
        jq("#enter_game_code").value = gameCode
        jq("#join_game_submit").click()
        jq("#enter_user_name").value = "Test"
        jq("#user_info_submit").click()
        assertEquals(jq("#user_name").text(),"Test")
        assertEquals(jq("#game_code").text(),gameCode)
        assertEquals(jq("#num_players").text(),"2")
    }
}