package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity
import java.time.Instant
import java.util.*

class Target(
    var id: UUID? = null,
    var actionId: String,
    var targetType: TargetType,
    var status: TargetStatusType? = null,
    var agent: String? = null,
    var vesselName: String? = null,
    var source: MissionSourceEnum? = null,
    var vesselType: VesselTypeEnum? = null,
    var vesselSize: VesselSizeEnum? = null,
    var vesselIdentifier: String? = null,
    var startDateTimeUtc: Instant? = null,
    var endDateTimeUtc: Instant? = null,
    var identityControlledPerson: String? = null,
    var controls: List<Control>? = listOf(),
    var externalData: TargetExternalData? = null
) {
    fun toTargetEntity(): TargetEntity {
        return TargetEntity(
            id = id?: UUID.randomUUID(),
            agent = agent,
            actionId = actionId,
            targetType = targetType,
            vesselName = vesselName,
            status = status,
            vesselIdentifier = vesselIdentifier,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            vesselType = vesselType,
            vesselSize = vesselSize,
            source = source,
            identityControlledPerson = identityControlledPerson,
            controls = controls?.map { it.toControlEntity() },
            externalData = externalData?.toTargetExternalDataEntity()
        )
    }

    companion object {
        fun fromTargetEntity(entity: TargetEntity): Target {
            return Target(
                id = entity.id,
                agent = entity.agent,
                actionId = entity.actionId,
                targetType = entity.targetType,
                vesselName = entity.vesselName,
                status = entity.status,
                source = entity.source,
                vesselType = entity.vesselType,
                vesselSize = entity.vesselSize,
                vesselIdentifier = entity.vesselIdentifier,
                startDateTimeUtc = entity.startDateTimeUtc,
                endDateTimeUtc = entity.endDateTimeUtc,
                identityControlledPerson = entity.identityControlledPerson,
                controls = entity.controls?.map { Control.fromControlEntity(it) }?.sortedBy { it.controlType },
                externalData = entity.externalData?.let { TargetExternalData.fromTargetExternalDataEntity(it) }
            )
        }
    }
}
