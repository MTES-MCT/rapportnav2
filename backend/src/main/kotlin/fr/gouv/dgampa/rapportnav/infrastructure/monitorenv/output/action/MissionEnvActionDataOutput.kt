package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.PatchedEnvActionEntity
import java.time.ZonedDateTime
import java.util.*

data class MissionEnvActionDataOutput(
    open val id: UUID,
    open val actionStartDateTimeUtc: ZonedDateTime? = null,
    open val actionEndDateTimeUtc: ZonedDateTime? = null,
    open val actionType: ActionTypeEnum,
    open val observationsByUnit: String? = null,
) {
    fun toPatchableEnvActionEntity(): PatchedEnvActionEntity {
        return PatchedEnvActionEntity(
            id = this.id,
            actionStartDateTimeUtc = this.actionStartDateTimeUtc,
            actionEndDateTimeUtc = this.actionEndDateTimeUtc,
            observationsByUnit = this.observationsByUnit
        )
    }

}
