package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetType
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.target2.v2.TargetModel
import java.time.Instant
import java.util.*


class TargetEntity(
    var id: UUID,
    var actionId: String,
    var targetType: TargetType,
    var status: TargetStatusType? = null,
    var agent: String? = null,
    var vesselName: String? = null,
    var vesselType: VesselTypeEnum? = null,
    var vesselSize: VesselSizeEnum? = null,
    var vesselIdentifier: String? = null,
    var startDateTimeUtc: Instant? = null,
    var endDateTimeUtc: Instant? = null,
    var identityControlledPerson: String? = null,
    var source: MissionSourceEnum? = null,
    var controls: List<ControlEntity>? = listOf(),
    var externalData: TargetExternalDataEntity? = null
) {

    fun toTargetModel(): TargetModel {
        return TargetModel(
            id = id,
            agent = agent,
            actionId = actionId,
            targetType = targetType,
            vesselName = vesselName,
            status = status?.toString(),
            vesselIdentifier = vesselIdentifier,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            vesselType = vesselType?.toString(),
            vesselSize = vesselSize?.toString(),
            identityControlledPerson = identityControlledPerson,
            source = source?.toString(),
            externalId = externalData?.id,
            controls = controls?.map { it.toControlModel() }
        )
    }

    fun getControlByType(controlType: ControlType): ControlEntity? {
        return this.controls?.find { it.controlType == controlType }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TargetEntity
        if (id != other.id) return false
        if (actionId != other.actionId) return false
        if (targetType != other.targetType) return false
        if (status != other.status) return false
        if (agent != other.agent) return false
        if (source != other.source) return false
        if (vesselName != other.vesselName) return false
        if (vesselType != other.vesselType) return false
        if (vesselSize != other.vesselSize) return false
        if (externalData != other.externalData) return false
        if (vesselIdentifier != other.vesselIdentifier) return false
        if (startDateTimeUtc != other.startDateTimeUtc) return false
        if (endDateTimeUtc != other.endDateTimeUtc) return false
        if (identityControlledPerson != other.identityControlledPerson) return false
        if (controls?.size != other.controls?.size) return false
        if (controls?.toSet() != other.controls?.toSet()) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + actionId.hashCode()
        result = 31 * result + source.hashCode()
        result = 31 * result + targetType.hashCode()
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (agent?.hashCode() ?: 0)
        result = 31 * result + (vesselName?.hashCode() ?: 0)
        result = 31 * result + (vesselType?.hashCode() ?: 0)
        result = 31 * result + (vesselSize?.hashCode() ?: 0)
        result = 31 * result + (vesselIdentifier?.hashCode() ?: 0)
        result = 31 * result + (startDateTimeUtc?.hashCode() ?: 0)
        result = 31 * result + (endDateTimeUtc?.hashCode() ?: 0)
        result = 31 * result + (identityControlledPerson?.hashCode() ?: 0)
        result = 31 * result + (controls?.hashCode() ?: 0)
        result = 31 * result + (externalData?.hashCode() ?: 0)
        return result
    }

    companion object {
        fun fromTargetModel(model: TargetModel): TargetEntity {
            return TargetEntity(
                id = model.id,
                agent = model.agent,
                actionId = model.actionId,
                targetType = model.targetType,
                vesselName = model.vesselName,
                vesselIdentifier = model.vesselIdentifier,
                startDateTimeUtc = model.startDateTimeUtc,
                endDateTimeUtc = model.endDateTimeUtc,
                identityControlledPerson = model.identityControlledPerson,
                source = model.source?.let { MissionSourceEnum.valueOf(it) },
                status = model.status?.let { TargetStatusType.valueOf(it) },
                vesselType = model.vesselType?.let { VesselTypeEnum.valueOf(it) },
                vesselSize = model.vesselSize?.let { VesselSizeEnum.valueOf(it) },
                controls = model.controls?.map { ControlEntity.fromControlModel(it) },
                externalData = if(model.externalId != null) TargetExternalDataEntity(id = model.externalId!!) else null
            )
        }
    }
}
