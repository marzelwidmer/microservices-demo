package ch.keepcalm.movie.info

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MovieInfoServiceApplication

fun main(args: Array<String>) {
	runApplication<MovieInfoServiceApplication>(*args)
}
