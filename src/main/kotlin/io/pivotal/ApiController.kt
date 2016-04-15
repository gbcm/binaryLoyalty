package io.pivotal

import io.pivotal.model.Game
import io.pivotal.model.GameRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/api")
class ApiController @Autowired constructor(val gameRepository: GameRepository) {

    @RequestMapping(value = "/startGame/{gameCode}", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun startGame(@PathVariable gameCode : String) {
        val game = gameRepository.fetchGame(gameCode)
        game.start()
        gameRepository.save(game)
    }

    @RequestMapping(value = "/game/{gameCode}", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun getGameStatus(@PathVariable gameCode : String) : Game {
        return gameRepository.fetchGame(gameCode)
    }
}
