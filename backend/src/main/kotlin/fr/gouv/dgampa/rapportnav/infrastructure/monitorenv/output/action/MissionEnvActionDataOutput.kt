package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.PatchedEnvActionEntity
import java.time.ZonedDateTime
import java.util.*

data class MissionEnvActionDataOutput(
    val id: UUID,
    val actionStartDateTimeUtc: ZonedDateTime? = null,
    val actionEndDateTimeUtc: ZonedDateTime? = null,
    val observationsByUnit: String? = null,
    val hasDivingDuringOperation: Boolean? = null,
    val incidentDuringOperation: Boolean? = null
) {
    fun toPatchableEnvActionEntity(): PatchedEnvActionEntity {
        return PatchedEnvActionEntity(
            id = this.id,
            actionStartDateTimeUtc = this.actionStartDateTimeUtc?.toInstant(),
            actionEndDateTimeUtc = this.actionEndDateTimeUtc?.toInstant(),
            observationsByUnit = this.observationsByUnit,
            hasDivingDuringOperation = this.hasDivingDuringOperation,
            incidentDuringOperation = this.incidentDuringOperation
        )
    }

}
