package no.nav.helse.flex

import no.nav.helse.flex.henvendelse.*
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.shouldBe
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.relational.core.conversion.DbActionExecutionException
import java.time.Instant
import java.time.OffsetDateTime
import java.util.*

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class UniqueConstraintTest : FellesTestOppsett() {
    @Autowired
    lateinit var henvendelseRepository: HenvendelseRepository

    @Autowired
    lateinit var listener: Listener

    @Test
    fun `DB liker ikke duplikat`() {
        henvendelseRepository.deleteAll()

        val henvendelseDbRecord =
            HenvendelseDbRecord(
                fnr = "1234",
                tema = "SYK",
                tidspunkt = OffsetDateTime.now().minusMonths(1).toInstant(),
                temagruppe = "HELSE",
                traadId = "123456",
            )
        henvendelseRepository.save(henvendelseDbRecord)
        val e =
            Assertions.assertThrows(DbActionExecutionException::class.java) {
                henvendelseRepository.save(henvendelseDbRecord)
            }
        (e.cause is DuplicateKeyException) shouldBe true
    }

    @Test
    fun `Listener håndterer at DB ikke liker duplikat`() {
        henvendelseRepository.deleteAll()
        val henvendelseDbRecord =
            HenvendelseKafkaDTO(
                fnr = "1234",
                tema = "SYK",
                tidspunkt = Instant.now(),
                temagruppe = "HELSE",
                traadId = "123456",
            )
        listener.lagreHenvendelse(henvendelseDbRecord)
        listener.lagreHenvendelse(henvendelseDbRecord)
        henvendelseRepository.count() `should be equal to` 1
    }
}
