package no.nav.helse.flex

import no.nav.helse.flex.henvendelse.HenvendelseKafkaDTO
import no.nav.helse.flex.henvendelse.HenvendelseRepository
import no.nav.helse.flex.henvendelse.topic
import org.amshove.kluent.`should be equal to`
import org.apache.kafka.clients.producer.ProducerRecord
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class LagreKafkaMeldingerTest : FellesTestOppsett() {

    @Autowired
    lateinit var henvendelseRepository: HenvendelseRepository

    @Test
    @Order(1)
    fun `Tar imot henvendelse`() {
        henvendelseRepository.count() `should be equal to` 0
        kafkaProducer.send(
            ProducerRecord(
                topic,
                UUID.randomUUID().toString(),
                HenvendelseKafkaDTO(fnr = "1234", tema = "SYK", tidspunkt = Instant.EPOCH, temagruppe = "HELSE", traadId = "123456").serialisertTilString()
            )
        )

        await().atMost(5, TimeUnit.SECONDS).until {
            henvendelseRepository.count() == 1L
        }

        henvendelseRepository.count() `should be equal to` 1
        val sykepengesoknadIdDbRecord = henvendelseRepository.findAll().toList().first()
        sykepengesoknadIdDbRecord.fnr `should be equal to` "1234"
    }
}
