package com.example.sms

import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.notFound
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sms/types")
class SmsTypeController {

    @GetMapping
    fun getAll(): List<SmsTypeInfo> =
        SmsType.values().map { it.toInfo() }

    @GetMapping("/{id}")
    fun getByName(@PathVariable id: String): ResponseEntity<SmsTypeInfo> {
        val type = SmsType.values().find { it.name == id }
        return if (type != null) ok(type.toInfo())
        else notFound().build()
    }
}

data class SmsTypeInfo(
    val id: SmsType,
    val customizable: Boolean,
    val variables: List<Variable>
)

fun SmsType.toInfo() = SmsTypeInfo(this, customizable, variables.toList())
