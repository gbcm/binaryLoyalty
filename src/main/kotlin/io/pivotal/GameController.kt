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
    val gamesMap = hashMapOf<String, MutableList<UserInfo>>()

    @RequestMapping(value = "/", method = arrayOf(RequestMethod.GET))
    fun index(model : Model) : String {
        model.addAttribute("game_code_form", GameCode())
        return "landing"
    }

    @RequestMapping(value = "/startGame", method = arrayOf(RequestMethod.POST))
    fun startGame(model : Model) : String {
        var gameCode = generateGameCode()
        model.addAttribute("num_players", 1)

        var ui = UserInfo()
        ui.gameCode = gameCode
        ui.userName = "System"

        gamesMap.put(gameCode, mutableListOf(ui))
        model.addAttribute("user_info", ui)

        return "lobby"
    }

    @RequestMapping(value = "/joinGame", method = arrayOf(RequestMethod.POST))
    fun joinGame(model : Model, @ModelAttribute("game_code_form") gameCode : GameCode ) : String {
        val players = gamesMap[gameCode.code]
        if (players != null) {
            var ui = UserInfo()
            ui.gameCode = gameCode.code
            players.add(ui)
            model.addAttribute("user_info_form", ui)
            return "userInfo"
        }
        model.addAttribute("game_code_form", GameCode())
        return "landing"
    }

    @RequestMapping(value = "/userInfo", method = arrayOf(RequestMethod.POST))
    fun enterUserInfo(model : Model, @ModelAttribute("user_info_form") userInfo : UserInfo ) : String {
        val players = gamesMap[userInfo.gameCode]
        if (players != null) {
            model.addAttribute("num_players", players.size)
            model.addAttribute("user_info", userInfo)
            return "lobby"
        }
        model.addAttribute("game_code_form", GameCode())
        return "landing"
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

class UserInfo {
    var gameCode = ""
    var userName = ""
    init {}
}

