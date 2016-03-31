package io.pivotal

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import com.codeborne.selenide.Selenide.*;
import kotlin.test.*;

@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(BinaryLoyaltyApplication::class))
@WebAppConfiguration
class IntegrationTests {

    @Test
    fun whenStartGameIsClicked_userSeesAGameCodeAndOnePlayer() {
        open("/")
        `$`("#start_game_submit").click()
        assertNotEquals(`$`("#game_code").text(),"")
        assertEquals(`$`("#num_players").text(),"1")
    }

    @Test
    fun whenExistingGameCodeIsJoined_userSeesNumberOfPlayers() {
        open("/")
        `$`("#start_game_submit").click()
        var gameCode = `$`("#game_code").text()
        open("/")
        `$`("#enter_game_code").value = gameCode
        `$`("#join_game_submit").click()
        assertEquals(`$`("#game_code").text(),gameCode)
        assertEquals(`$`("#num_players").text(),"2")
    }
}