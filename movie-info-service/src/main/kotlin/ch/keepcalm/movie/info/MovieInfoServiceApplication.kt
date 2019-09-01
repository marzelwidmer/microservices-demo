package ch.keepcalm.movie.info

import ch.keepcalm.movie.info.model.Movie
import io.jaegertracing.Configuration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
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
class Configuration {

//    @Bean
//    fun jaegerTracer() : io.opentracing.Tracer = Configuration("movie-info-service").tracer

}

@Component
class DataLoader(private val repository: MovieRepository) {

    val log: Logger = Logger.getLogger(DataLoader::class.java.name)!!

    @PostConstruct
    fun load() = repository.deleteAll()
            .thenMany( // When it finish then ...
                    listOf(
                            Movie(id = "0000-0000-0000-0000-0001", name = "Fast & Furious Presents: Hobbs & Shaw", desc = "Lawman Luke Hobbs and outcast Deckard Shaw form an unlikely alliance when a cyber-genetically enhanced villain threatens the future of humanity."),
                            Movie(id = "0000-0000-0000-0000-0002", name = "The Nightingale", desc = "Set in 1825, Clare, a young Irish convict woman, chases a British officer through the rugged Tasmanian wilderness, bent on revenge for a terrible act of violence he committed against her family. On the way she enlists the services of an Aboriginal tracker named Billy, who is also marked by trauma from his own violence-filled past."),
                            Movie(id = "0000-0000-0000-0000-0003", name = "Luce", desc = "A married couple is forced to reckon with their idealized image of their son, adopted from war-torn Eritrea, after an alarming discovery by a devoted high school teacher threatens his status as an all-star student."),
                            Movie(id = "0000-0000-0000-0000-0004", name = "Them That Follow", desc = "Set deep in the wilds of Appalachia, where believers handle death-dealing snakes to prove themselves before God, Them That Follow tells the story of a pastor's daughter who holds a secret that threatens to tear her community apart."),
                            Movie(id = "0000-0000-0000-0000-0005", name = "La paranza dei bambini", desc = "A gang of teenage boys stalk the streets of Naples armed with hand guns and AK-47s to do their mob bosses' bidding.")
                    ).toFlux() // Convert List to a Flux Object
                            .map { Movie(id = it.id, name = it.name, desc = it.desc) } // [a, b, c] f(x) => [f(a), f(b), f(c)]
                            .flatMap { repository.save(it) } // [[a, b] ,[c, d] f(x) => [f(a), f(b), f(c), f(d)]
                            .thenMany(repository.findAll()) // Search entries when it is finish...
            ).subscribe { log.info("$it") } // Then print it out...
}

interface MovieRepository : ReactiveCrudRepository<Movie, String> {
    fun findAllById(movieId: String): Flux<Movie>
}

