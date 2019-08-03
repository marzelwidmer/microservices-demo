package ch.keepcalm.movie.information

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InformationServiceApplication

fun main(args: Array<String>) {
	runApplication<InformationServiceApplication>(*args)
}
