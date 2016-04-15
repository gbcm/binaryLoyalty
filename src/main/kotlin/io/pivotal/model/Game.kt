package io.pivotal.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class Game(val gameCode: String,
           var startTime: LocalDateTime?,
           val players: MutableList<UserInfo>) {

    constructor() : this(generateGameCode(), null, mutableListOf(UserInfo.systemUser())){ }

    companion object {
        fun generateGameCode() : String {
            val rand = Random()
            var ret = ""
            for (x in 1..GAME_CODE_LEN) {
                ret += ALPHA_NUM[rand.nextInt(ALPHA_NUM.length)]
            }
            return ret
        }
    }

    fun start(seconds: Long = 15, currentTime: LocalDateTime = LocalDateTime.now()) {
        startTime = currentTime.plusSeconds(seconds)
    }

    @JsonProperty("state")
    fun state() : State {
        return stateForTime(LocalDateTime.now())
    }

    fun stateForTime(currentTime: LocalDateTime) : State {
        if (startTime == null) {
            return State.UNSTARTED
        }
        if ((startTime as LocalDateTime).isAfter(currentTime)) {
            return State.STARTING
        }
        return State.STARTED
    }

    @JsonProperty("countdown")
    fun countdown() : Long {
        return countdownForTime(LocalDateTime.now())
    }

    fun countdownForTime(currentTime: LocalDateTime) : Long {
        if (state() == State.STARTING) {
            return currentTime.until(startTime, ChronoUnit.SECONDS)
        }
        return 0
    }
}


enum class UserType {
    SYSTEM, LOYALIST
}

class UserInfo(val userName: String, val userType: UserType = UserType.LOYALIST) {
    companion object {
        fun systemUser(): UserInfo {
            return UserInfo("System", UserType.SYSTEM)
        }
    }

    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as UserInfo

        if (userName != other.userName) return false
        if (userType != other.userType) return false

        return true
    }

    override fun hashCode(): Int{
        var result = userName.hashCode()
        result += 31 * result + userType.hashCode()
        return result
    }


}

enum class State {
    UNSTARTED, STARTING, STARTED
}

val ALPHA_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
val GAME_CODE_LEN = 4

