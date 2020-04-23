package com.example.sms

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant

@Document
data class Message(
    @Id val id: String? = null,
    val sentAt: Instant = Instant.now(),
    val type: SmsType,
    val language: Language,
    val text: String,
    val gateway: Gateway,
    val phoneNumber: String
)

enum class Gateway {
    TWILLIO,
    NEXMO,
    DAKTELA
}

interface MessageRepository : MongoRepository<Message, String>
