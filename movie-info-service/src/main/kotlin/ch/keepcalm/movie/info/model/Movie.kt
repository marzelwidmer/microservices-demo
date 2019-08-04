package ch.keepcalm.movie.info.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class Movie(@Id val id: String = UUID.randomUUID().toString(), val name: String, val desc: String)