package ch.keepcalm.movie.rating

import ch.keepcalm.movie.rating.model.Rating
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import java.util.logging.Logger
import javax.annotation.PostConstruct

@SpringBootApplication
class MovieRatingServiceApplication

fun main(args: Array<String>) {
    runApplication<MovieRatingServiceApplication>(*args)
}

@RestController
@RequestMapping(value = ["/api/movies"])
class RatingResource(private val service: RatingService) {

    @GetMapping(value = ["/ratings/{movieId}"])
    fun getRating(@PathVariable movieId: String) = service.getAllRatingsByeId(movieId)
}

@Service
class RatingService(private val repository: RatingRepository) {
    fun getAllRatingsByeId(movieId: String) = repository.findAllById(movieId)
}

@Component
class DataLoader(private val repository: RatingRepository) {

    val log: Logger = Logger.getLogger(DataLoader::class.java.name)!!

    @PostConstruct
    fun load() = repository.deleteAll()
            .thenMany(
                    listOf(
                            Rating(id = "0000-0000-0000-0000-0001", rating = 7.2),
                            Rating(id = "0000-0000-0000-0000-0002", rating = 6.5),
                            Rating(id = "0000-0000-0000-0000-0003", rating = 6.6),
                            Rating(id = "0000-0000-0000-0000-0004", rating = 5.5),
                            Rating(id = "0000-0000-0000-0000-0005", rating = 6.5)
                    ).toFlux()
                            .map { Rating(id = it.id, rating = it.rating) }
                            .flatMap { repository.save(it) }
                            .thenMany(repository.findAll())
            ).subscribe { log.info("$it") }
}

interface RatingRepository : ReactiveCrudRepository<Rating, String> {
    fun findAllById(movieId: String): Flux<Rating>
}

