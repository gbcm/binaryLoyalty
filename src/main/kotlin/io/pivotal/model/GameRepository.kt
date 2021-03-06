package io.pivotal.model

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
class GameRepository {
    val gamesMap = hashMapOf<String, Game>()

    fun fetchGame(gameCode : String) : Game {
        return gamesMap[gameCode] ?: throw GameNotFoundException("gameCode '$gameCode' not found");
    }

    fun save(game: Game) {
        gamesMap.put(game.gameCode, game)
    }
}

class GameNotFoundException : Throwable {
    constructor(message : String) : super(message){
    }
}