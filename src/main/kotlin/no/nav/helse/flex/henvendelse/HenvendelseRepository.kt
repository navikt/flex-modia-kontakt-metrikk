package no.nav.helse.flex.henvendelse

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface HenvendelseRepository : CrudRepository<HenvendelseDbRecord, String>

@Table("henvendelse")
data class HenvendelseDbRecord(
    @Id
    val id: String? = null,
    val fnr: String,
    val tema: String?,
    val temagruppe: String,
    val threadId: String,
    val tidspunkt: Instant
)
