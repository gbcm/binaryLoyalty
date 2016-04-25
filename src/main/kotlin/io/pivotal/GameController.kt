package io.pivotal

import io.pivotal.form.GameCodeForm
import io.pivotal.form.UserInfoForm
import io.pivotal.model.Game
import io.pivotal.model.GameRepository
import io.pivotal.model.UserInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import com.fasterxml.jackson.databind.ObjectMapper

@Controller
class GameController @Autowired constructor(val gameRepository: GameRepository) {

    companion object {
        val objectMapper = ObjectMapper()
    }

    @RequestMapping(value = "/", method = arrayOf(RequestMethod.GET))
    fun index(model : Model) : String {
        model.addAttribute("game_code_form", GameCodeForm())
        return "landing"
    }

    @RequestMapping(value = "/createGame", method = arrayOf(RequestMethod.POST))
    fun createGame(model : Model) : String {
        model.addAttribute("num_players", 1)

        var game = Game()
        gameRepository.save(game)
        model.addAttribute("user_info", game.players.get(0))
        model.addAttribute("game_json", objectMapper.writeValueAsString(game))

        return "lobby"
    }

    @RequestMapping(value = "/joinGame", method = arrayOf(RequestMethod.POST))
    fun joinGame(model : Model, @ModelAttribute("game_code_form") gameCodeForm : GameCodeForm ) : String {
        val game = gameRepository.fetchGame(gameCodeForm.gameCode)
        if (game != null) {
            var ui = UserInfoForm()
            ui.gameCode = game.gameCode
            model.addAttribute("user_info_form", ui)
            model.addAttribute("game", game)
            model.addAttribute("game_json", objectMapper.writeValueAsString(game))

            return "userInfo"
        }
        model.addAttribute("game_code_form", GameCodeForm())
        return "landing"
    }

    @RequestMapping(value = "/userInfo", method = arrayOf(RequestMethod.POST))
    fun enterUserInfo(model : Model, @ModelAttribute("user_info_form") userInfoForm : UserInfoForm) : String {
        val game = gameRepository.fetchGame(userInfoForm.gameCode)
        if (game != null) {
            val userInfo = UserInfo(userInfoForm.userName)
            game.players.add(userInfo)
            gameRepository.save(game)
            model.addAttribute("user_info", userInfo)
            model.addAttribute("game", game)
            model.addAttribute("game_json", objectMapper.writeValueAsString(game))
            return "lobby"
        }
        model.addAttribute("game_code_form", GameCodeForm())
        return "landing"
    }
}

