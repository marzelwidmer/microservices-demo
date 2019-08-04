package ch.keepcalm.movie.info

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import java.util.*
import java.util.logging.Logger
import javax.annotation.PostConstruct

@SpringBootApplication
class MovieInfoServiceApplication

fun main(args: Array<String>) {
    runApplication<MovieInfoServiceApplication>(*args)
}

@RestController
@RequestMapping(value = ["/api/movies"])
class MovieResource(private val service: MovieService) {

    @GetMapping(value = ["/info/{movieId}"])
    fun getMovies(@PathVariable movieId: String) = service.getAllMoviesById(movieId)
}

@Service
class MovieService(private val repository: MovieRepository) {
    fun getAllMoviesById(movieId: String) = repository.findAllById(movieId)
}

@Component
class DataLoader(private val repository: MovieRepository) {

    val log: Logger = Logger.getLogger(DataLoader::class.java.name)!!

    @PostConstruct
    fun load() = repository.deleteAll()
            .thenMany( // When it finish then ...
                    listOf(
                            Movie(id = "0000-0000-0000-0000-0001", name = "Fast & Furious Presents: Hobbs & Shaw"),
                            Movie(id = "0000-0000-0000-0000-0002", name = "The Nightingale"),
                            Movie(id = "0000-0000-0000-0000-0003", name = "Luce"),
                            Movie(id = "0000-0000-0000-0000-0004", name = "Them That Follow"),
                            Movie(id = "0000-0000-0000-0000-0005", name = "La paranza dei bambini")
                    ).toFlux() // Convert List to a Flux Object
                            .map { Movie(id = it.id, name = it.name) } // [a, b, c] f(x) => [f(a), f(b), f(c)]
                            .flatMap { repository.save(it) } // [[a, b] ,[c, d] f(x) => [f(a), f(b), f(c), f(d)]
                            .thenMany(repository.findAll()) // Search entries when it is finish...
            ).subscribe { log.info("$it") } // Then print it out...
}

interface MovieRepository : ReactiveCrudRepository<Movie, String> {
    fun findAllById(movieId: String): Flux<Movie>
}

@Document
data class Movie(@Id val id: String = UUID.randomUUID().toString(), val name: String)
