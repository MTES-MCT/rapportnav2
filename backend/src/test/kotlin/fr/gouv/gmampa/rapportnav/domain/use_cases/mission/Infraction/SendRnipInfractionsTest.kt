package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.Infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.repositories.rnip.IRnipInfractionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.SendRnipInfractions
import fr.gouv.dgampa.rapportnav.infrastructure.rnip.inputs.RnipInfractionInput
import fr.gouv.dgampa.rapportnav.infrastructure.rnip.inputs.RnipInspectorInput
import fr.gouv.dgampa.rapportnav.infrastructure.rnip.outputs.RnipInfractionResultOutput
import fr.gouv.dgampa.rapportnav.infrastructure.rnip.outputs.RnipLineStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.UUID

@SpringBootTest(classes = [SendRnipInfractions::class])
class SendRnipInfractionsTest {

    @Autowired
    private lateinit var useCase: SendRnipInfractions

    @MockitoBean
    private lateinit var repository: IRnipInfractionRepository

    @Test
    fun `sends infractions to the repository and returns its results`() {
        val id = UUID.randomUUID()
        val infractions = listOf(
            RnipInfractionInput(
                id = id,
                dateEnvoi = Instant.parse("2026-11-02T10:00:00Z"),
                numeroRapport = "SATI-42",
                dateDeCommission = Instant.parse("2026-10-30T08:30:00Z"),
                module = SatiModuleType.M4,
                natinf = "2593",
                inspecteur = RnipInspectorInput(
                    nom = "Durand",
                    prenom = "Alice",
                    nomDuService = "DML 29",
                    codeUnite = "ULAM 29",
                    provenance = "RapportNav"
                )
            )
        )
        val results = listOf(RnipInfractionResultOutput(id = id, success = true, status = RnipLineStatus.ACCEPTED))

        whenever(repository.send(infractions)).thenReturn(results)

        assertThat(useCase.execute(infractions)).isEqualTo(results)
        verify(repository).send(infractions)
    }
}
