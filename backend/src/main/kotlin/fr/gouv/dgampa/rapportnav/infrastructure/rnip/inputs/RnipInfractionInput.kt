package fr.gouv.dgampa.rapportnav.infrastructure.rnip.inputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import java.time.Instant
import java.util.UUID


data class RnipInfractionInput(
    val id: UUID, //Generated
    val dateEnvoi: Instant, //generated
    val numeroRapport: String, //satiID
    val dateDeCommission: Instant, //startDate
    val module: SatiModuleType,
    val natinf: String,
    val navire: RnipNavireInput? = null,
    val inspecteur: RnipInspectorInput,
    val etablissement: RnipEstablishmentInput? = null
)
