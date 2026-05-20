package fr.gouv.dgampa.rapportnav

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class RapportNavApplication

fun main(args: Array<String>) {
    runApplication<RapportNavApplication>(*args)
}
