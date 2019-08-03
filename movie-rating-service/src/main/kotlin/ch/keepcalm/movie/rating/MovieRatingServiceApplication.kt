package ch.keepcalm.movie.rating

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MovieRatingServiceApplication

fun main(args: Array<String>) {
	runApplication<MovieRatingServiceApplication>(*args)
}
