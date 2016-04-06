package io.pivotal

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import kotlin.concurrent.timer

@Controller
@RequestMapping("/api")
class ApiController {

    val gamesStatesMap = hashMapOf<String, GameState>()

    @RequestMapping(value = "/startGame/{gameCode}", method = arrayOf(RequestMethod.POST))
    @ResponseBody
    fun startGame(@PathVariable gameCode : String) {
        var gameState = gamesStatesMap[gameCode]
        if (gameState == null) {
            gameState = GameState()
        }

        gameState.state = State.STARTING
        gameState.num = 15;
        gamesStatesMap[gameCode] = gameState

        val gameStartTimer = timer(daemon = true, period = 1000, initialDelay = 1000.toLong()) {
            var timerState = gamesStatesMap[gameCode]
            if (timerState == null) {
                this.cancel()
                //This is a weird error
            } else if (timerState.state == State.STARTED) {
                this.cancel()
            } else if (timerState.state == State.STARTING) {
                timerState.num--
                if (timerState.num <= 0) {
                    timerState.state = State.STARTED
                }
                gamesStatesMap[gameCode] = timerState
            }
        }
    }

    @RequestMapping(value = "/state/{gameCode}", method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun getGameState(@PathVariable gameCode : String) : GameState {
        var state = gamesStatesMap[gameCode]
        if (state != null) {
            return state
        }
        return GameState()
    }
}

class GameState {
    var state = State.UNSTARTED
    var num = 0
}

enum class State {
    UNSTARTED, STARTING, STARTED
}