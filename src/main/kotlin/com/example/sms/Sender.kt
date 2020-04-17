package com.example.sms

import com.example.sms.Gateway.TWILLIO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

interface Sender {
    val gateway: Gateway
    fun send(phoneNumber: String, template: Template, variables: Map<Variable, String>)

    fun render(templateText: String, variables: Map<Variable, String>): String {
        var result = templateText
        variables.forEach { (variable, value) ->
            result = result.replace("\${$variable}", value)
        }
        return result
    }
}

@Service
class TwillioSender(
    private val messageRepository: MessageRepository
) : Sender {
    private val logger = LoggerFactory.getLogger(TwillioSender::class.java)
    override val gateway = TWILLIO

    override fun send(phoneNumber: String, template: Template, variables: Map<Variable, String>) {
        val message = Message(
            event = template.event,
            language = template.language,
            text = render(template.text, variables),
            gateway = gateway,
            phoneNumber = phoneNumber
        )
        val savedMessage = messageRepository.save(message)
        logger.info("sending message id=${savedMessage.id} event=${template.event} phoneNumber=$phoneNumber thru=$gateway")
    }
}
