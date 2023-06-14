package no.nav.helse.flex

import no.nav.helse.flex.kafka.rapidTopic
import no.nav.helse.flex.repository.SykepengesoknadIdRepository
import no.nav.helse.flex.repository.SykepengesoknadVedtaksperiodeRepository
import no.nav.helse.flex.repository.VedtaksperiodeForkastetRepository
import no.nav.helse.flex.repository.VedtaksperiodeFunksjonellFeilRepository
import no.nav.helse.flex.repository.VedtaksperiodeTilstandRepository
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should contain`
import org.amshove.kluent.shouldNotBeNull
import org.apache.kafka.clients.producer.ProducerRecord
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import java.util.concurrent.TimeUnit

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class LagreKafkaMeldingerTest : FellesTestOppsett() {

    @Autowired
    lateinit var henvendelseRepository: HenvendelseRepository


    @Test
    @Order(1)
    fun `Tar imot henvendelse ider`() {
        sykepengesoknadIdRepository.count() `should be equal to` 0
        kafkaProducer.send(ProducerRecord(rapidTopic, UUID.randomUUID().toString(), soknad))

        await().atMost(20, TimeUnit.SECONDS).until {
            sykepengesoknadIdRepository.count() == 1L
        }

        sykepengesoknadIdRepository.count() `should be equal to` 1
        val sykepengesoknadIdDbRecord = sykepengesoknadIdRepository.findAll().toList().first()
        sykepengesoknadIdDbRecord.sykepengesoknadAtId `should be equal to` "109d1fd9-50cf-4e21-b5e3-dbf10665a849"
        sykepengesoknadIdDbRecord.sykepengesoknadUuid `should be equal to` "7adb45bf-6de2-3c0a-bafe-1f822ddf21a4"

        sykepengesoknadIdRepository.insert(
            sykepengesoknadUuid = sykepengesoknadIdDbRecord.sykepengesoknadUuid,
            sykepengesoknadAtId = sykepengesoknadIdDbRecord.sykepengesoknadAtId
        )

        sykepengesoknadIdRepository.count() `should be equal to` 1
    }
}
