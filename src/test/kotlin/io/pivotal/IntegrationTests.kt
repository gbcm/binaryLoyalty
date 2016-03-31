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
    fun whenStartGameIsClicked_userSeesAGameCode() {
        open("/")
        `$`("#submit").click()
        assertNotEquals(`$`("#game_code").text(),"")
    }
}