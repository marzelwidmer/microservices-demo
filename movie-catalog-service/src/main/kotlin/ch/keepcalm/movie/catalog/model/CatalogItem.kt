package ch.keepcalm.movie.catalog.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class CatalogItem(@Id val id: String = UUID.randomUUID().toString(), val name: String, val desc: String, val rating: Double = 0.0)