package io.pivotal.model

import org.jetbrains.spek.api.Spek
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class GameSpecs: Spek() { init {

    given("a new Game") {
        val game = Game()

        on("state") {
            it("is UNSTARTED") {
                assertEquals(State.UNSTARTED, game.state())
            }
        }

        on("gameCode") {
            it("has a game code") {
                assertNotNull(game.gameCode)
            }

            it("is the correct length") {
                assertEquals(GAME_CODE_LEN, game.gameCode.length)
            }
        }

        on("players") {
            it("has one player") {
                assertEquals(1, game.players.count())
            }

            it("is the system player") {
                assertEquals(UserInfo.systemUser(), game.players[0])
            }
        }

        on("countdown") {
            it("is 0") {
                assertEquals(0, game.countdown())
            }
        }
    }

    given("a game starting in the future") {
        val game = Game()
        val currentTime = LocalDateTime.now()
        game.start(30, currentTime)

        on("state") {
            it("is STARTING") {
                assertEquals(State.STARTING, game.state())
            }
        }

        on("countdown") {
            it("is equal to the number of seconds until starting") {
                assertEquals(30, game.countdownForTime(currentTime))
            }
        }
    }

    given("a game starting in the past") {
        val game = Game()
        game.start(10, LocalDateTime.now().minusSeconds(11))

        on("state") {
            it("is STARTED") {
                assertEquals(State.STARTED, game.state())
            }
        }
    }
}}
