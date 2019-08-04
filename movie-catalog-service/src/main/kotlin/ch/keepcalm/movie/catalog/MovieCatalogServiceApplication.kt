package ch.keepcalm.movie.catalog

import ch.keepcalm.movie.catalog.model.CatalogItem
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.kotlin.core.publisher.toFlux
import java.util.logging.Logger
import javax.annotation.PostConstruct

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
    @GetMapping(value = ["/catalog/{userId}"])
    fun getMovieCatalog(@PathVariable userId: String) = service.getMovieCatalog(userId)

}

@Service
class CatalogService(private val repository: CatalogRepository) {
    fun getMovieCatalog(userId: String) = repository.findAll()
}

@Component
class DataLoader(private val repository: CatalogRepository) {

    val log: Logger = Logger.getLogger(DataLoader::class.java.name)!!

    @PostConstruct
    fun load() =
            repository.deleteAll()
                    .thenMany(
                            listOf(
                                    CatalogItem(name = "Fast & Furious Presents: Hobbs & Shaw", desc = "Lawman Luke Hobbs and outcast Deckard Shaw form an unlikely alliance when a cyber-genetically enhanced villain threatens the future of humanity.", rating = 7.2),
                                    CatalogItem(name = "The Nightingale", desc = "Set in 1825, Clare, a young Irish convict woman, chases a British officer through the rugged Tasmanian wilderness, bent on revenge for a terrible act of violence he committed against her family. On the way she enlists the services of an Aboriginal tracker named Billy, who is also marked by trauma from his own violence-filled past.", rating = 6.5),
                                    CatalogItem(name = "Luce", desc = "A married couple is forced to reckon with their idealized image of their son, adopted from war-torn Eritrea, after an alarming discovery by a devoted high school teacher threatens his status as an all-star student.", rating = 6.6),
                                    CatalogItem(name = "Them That Follow", desc = "Set deep in the wilds of Appalachia, where believers handle death-dealing snakes to prove themselves before God, Them That Follow tells the story of a pastor's daughter who holds a secret that threatens to tear her community apart.", rating = 5.5),
                                    CatalogItem(name = "La paranza dei bambini", desc = "A gang of teenage boys stalk the streets of Naples armed with hand guns and AK-47s to do their mob bosses' bidding.", rating = 6.5)
                            ).toFlux()
                                    .map { CatalogItem(name = it.name, desc = it.desc, rating = it.rating) }
                                    .flatMap { repository.save(it) }
                                    .thenMany(repository.findAll())
                    ).subscribe { log.info("$it") }
}

interface CatalogRepository : ReactiveCrudRepository<CatalogItem, String>

