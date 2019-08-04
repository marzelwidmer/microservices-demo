package ch.keepcalm.movie.rating.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Rating(@Id val id: String = UUID.randomUUID().toString(), val rating: Double = 0.0)