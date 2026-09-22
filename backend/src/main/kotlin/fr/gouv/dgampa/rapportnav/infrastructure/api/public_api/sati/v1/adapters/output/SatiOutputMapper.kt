package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.sati.v1.adapters.output

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati.SatiMapper

object SatiOutputMapper {

    fun toOutput(entity: SatiEntity): SatiOutput {
        val data = SatiMapper.fromEntity(entity)
        return SatiOutput(
            id = requireNotNull(entity.id) { "SatiEntity.id must be set to build a SatiOutput" },
            version = entity.version,
            status = entity.status,
            actionId = data.actionId,
            module = data.module,
            resource = data.resource,
            vessel = data.vessel,
            startDatetimeUtc = data.startDatetimeUtc,
            endDatetimeUtc = data.endDatetimeUtc,
            principalInspector = data.principalInspector,
            otherInspectors = data.otherInspectors,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            updatedBy = entity.updatedBy
        )
    }

    fun toListOutput(entity: SatiEntity): SatiListOutput {
        return SatiListOutput(
            id = requireNotNull(entity.id) { "SatiEntity.id must be set to build a SatiListOutput" },
            version = entity.version,
            actionId = entity.actionId,
            module = entity.module,
            status = entity.status,
            startDatetimeUtc = entity.startDatetimeUtc,
            endDatetimeUtc = entity.endDatetimeUtc,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            updatedBy = entity.updatedBy
        )
    }
}
