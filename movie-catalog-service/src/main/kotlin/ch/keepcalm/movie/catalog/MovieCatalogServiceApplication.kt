package ch.keepcalm.movie.catalog

import ch.keepcalm.movie.catalog.model.Rating
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux


@SpringBootApplication
class MovieCatalogServiceApplication

fun main(args: Array<String>) {
    runApplication<MovieCatalogServiceApplication>(*args)
}

@RestController
@RequestMapping(value = ["/api/movies"])
class MovieCatalogResource(private val service: CatalogService) {

    // TODO [marcelwidmer-2019-08-04]: get all related movies IDs
    // TODO [marcelwidmer-2019-08-04]: for each movie ID, call movie-info-service and get details
    // TODO [marcelwidmer-2019-08-04]: put them all together
    @GetMapping(value = ["/catalog/{userId}/{id}"])
    fun getMovieCatalog(@PathVariable userId: String, @PathVariable id: String) = service.getMovieCatalog(userId = userId, id = id)

}

@Service
class CatalogService(private val webClientBuilder: WebClient.Builder) {
    fun getMovieCatalog(userId: String, id: String) = getRatings(userId = userId, id = id)//repository.findAll()

    fun getRatings(@PathVariable userId: String, @PathVariable id: String): Flux<Rating> {
        return this.webClientBuilder.baseUrl("http://movie-rating-service:8080/api/movies/ratings").build()
                .get().uri("/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToFlux(Rating::class.java)
                .log()
    }
}

@Component
class Configurationx {

    @Bean
    fun webClientBuilder() = WebClient.builder()

    @Bean
    fun jaegerTracer() = io.jaegertracing.Configuration("movie-catalog-service").tracer

}
