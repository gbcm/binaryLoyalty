package io.pivotal

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.util.*

@Controller
class GameController {

    val ALPHA_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val GAME_CODE_LEN = 4
    val gamesMap = hashMapOf<String, Int>()

    @RequestMapping(value = "/", method = arrayOf(RequestMethod.GET))
    fun index(model : Model) : String {
        var gc = GameCode()
        model.addAttribute("game_code_form", gc)
        return "landing"
    }

    @RequestMapping(value = "/startGame", method = arrayOf(RequestMethod.POST))
    fun startGame(model : Model) : String {
        var gameCode = generateGameCode()
        model.addAttribute("game_code", gameCode)
        model.addAttribute("num_players", 1)
        gamesMap.put(gameCode,1)
        return "lobby"
    }

    @RequestMapping(value = "/joinGame", method = arrayOf(RequestMethod.POST))
    fun joinGame(model : Model, @ModelAttribute("game_code_form") gameCode : GameCode ) : String {
        var numPlayers = gamesMap.get(gameCode.code)
        if (numPlayers == null) {
            return "landing"
        } else {
            gamesMap[gameCode.code] = numPlayers + 1
            model.addAttribute("game_code", gameCode.code)
            model.addAttribute("num_players", numPlayers + 1)
        }
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

class GameCode {
    var code: String = ""
    init {}
}
