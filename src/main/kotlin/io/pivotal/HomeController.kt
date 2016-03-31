package io.pivotal

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.util.*

@Controller
class HomeController {

    val ALPHA_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val GAME_CODE_LEN = 4

    @RequestMapping(value = "/", method = arrayOf(RequestMethod.GET))
    fun index() : String {
        return "landing"
    }

    @RequestMapping(value = "/", method = arrayOf(RequestMethod.POST))
    fun startGame(model : Model) : String {
        model.addAttribute("game_code",generateGameCode())
        return "lobby"
    }

    fun generateGameCode() : String {
        val rand = Random()
        var ret = ""
        for (x in 1..GAME_CODE_LEN) {
            ret += ALPHA_NUM[rand.nextInt(ALPHA_NUM.length)]
        }
        return ret
    }
}