package fr.gouv.dgampa.rapportnav

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class RapportNavApplication

fun main(args: Array<String>) {
    runApplication<RapportNavApplication>(*args)
}
