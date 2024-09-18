package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.control.ControlAdministrative
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.control.ControlSecurity
import java.time.Instant
import java.util.*

data class ActionControlInput(
    val id: UUID? = null,
    val missionId: Int,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val controlMethod: ControlMethod? = null,
    val vesselType: VesselTypeEnum? = null,
    val vesselIdentifier: String? = null,
    val vesselSize: VesselSizeEnum? = null,
    val identityControlledPerson: String? = null,
    val observations: String? = null,
    val controlAdministrative: ControlAdministrative? = null,
    val controlGensDeMer: ControlGensDeMer? = null,
    val controlNavigation: ControlNavigation? = null,
    val controlSecurity: ControlSecurity? = null
) {
    fun toActionControlEntity(): ActionControlEntity {
        return ActionControlEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc ?: Instant.now(),
            endDateTimeUtc = endDateTimeUtc ?: Instant.now(),
            latitude = latitude,
            longitude = longitude,
            controlMethod = controlMethod,
            vesselType = vesselType,
            vesselIdentifier = vesselIdentifier,
            vesselSize = vesselSize,
            identityControlledPerson = identityControlledPerson,
            observations = observations,
            controlAdministrative = controlAdministrative?.toControlAdministrativeEntity(
                missionId = missionId,
                actionId = id.toString()
            ),
            controlGensDeMer = controlGensDeMer?.toControlGensDeMerEntity(
                missionId = missionId,
                actionId = id.toString()
            ),
            controlNavigation = controlNavigation?.toControlNavigationEntity(
                missionId = missionId,
                actionId = id.toString()
            ),
            controlSecurity = controlSecurity?.toControlSecurityEntity(missionId = missionId, actionId = id.toString()),
        )

    }
}
