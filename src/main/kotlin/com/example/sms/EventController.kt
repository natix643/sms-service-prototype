package com.example.sms

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sms/events")
class EventController {

    @GetMapping
    fun getAll(): List<EventInfo> =
        Event.values().map { it.toInfo() }

    @GetMapping("/{name}")
    fun getByName(@PathVariable name: String): ResponseEntity<EventInfo> {
        val event = Event.values().find { it.name == name }
        return if (event != null) ok(event.toInfo())
        else notFound().build()
    }
}

data class EventInfo(
    val name: Event,
    val customizable: Boolean,
    val variables: List<Variable>
)

fun Event.toInfo() = EventInfo(this, customizable, variables)
