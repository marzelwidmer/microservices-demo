package ch.keepcalm.movie.info

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Component
import reactor.kotlin.core.publisher.toFlux
import java.util.*
import javax.annotation.PostConstruct

@SpringBootApplication
class MovieInfoServiceApplication

fun main(args: Array<String>) {
    runApplication<MovieInfoServiceApplication>(*args)
}


@Component
class DataLoader(private val repository: MovieRepository) {

    @PostConstruct
    fun load() = repository.deleteAll()
            .thenMany( // When it finish then ...
                    listOf(
                            Movie(name = "Fast & Furious Presents: Hobbs & Shaw"),
                            Movie(name = "The Nightingale"),
                            Movie(name = "Luce"),
                            Movie(name = "Them That Follow"),
                            Movie(name = "La paranza dei bambini")
                    ).toFlux() // Convert List to a Flux Object
                            .map { Movie(name = it.name) } // [a, b, c] f(x) => [f(a), f(b), f(c)]
                            .flatMap { repository.save(it) } // [[a, b] ,[c, d] f(x) => [f(a), f(b), f(c), f(d)]
                            .thenMany(repository.findAll()) // Search entries when it is finish...
            ).subscribe { print(it) } // Then print it out...
}

interface MovieRepository : ReactiveCrudRepository<Movie, String>

@Document
data class Movie(val id: String = UUID.randomUUID().toString(), val name: String)
