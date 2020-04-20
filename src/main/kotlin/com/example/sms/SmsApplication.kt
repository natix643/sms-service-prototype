package com.example.sms

import com.example.sms.Event.DELIVERY_IM_HERE
import com.example.sms.Language.CS
import com.example.sms.Language.EN
import com.example.sms.Variable.PROOF_CODE
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

fun main(args: Array<String>) {
    runApplication<SmsApplication>(*args)
}

@SpringBootApplication
class SmsApplication {
    @Bean
    fun initDb(templateRepository: TemplateRepository) = ApplicationRunner {
        templateRepository.saveAll(listOf(
            Template(
                event = DELIVERY_IM_HERE,
                businessId = null,
                send = true,
                translations = mapOf(
                    EN to "driver is here, your code is \${$PROOF_CODE}",
                    CS to "řidič je tady, váš kód je \${$PROOF_CODE}"
                )
            ),
            Template(
                event = DELIVERY_IM_HERE,
                businessId = "1",
                send = false,
                translations = mapOf()
            )
        ))
    }
}
