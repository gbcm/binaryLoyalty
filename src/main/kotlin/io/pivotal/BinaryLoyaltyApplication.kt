package io.pivotal

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class BinaryLoyaltyApplication

fun main(args: Array<String>) {
    SpringApplication.run(BinaryLoyaltyApplication::class.java, *args)
}
