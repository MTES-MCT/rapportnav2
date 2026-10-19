package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.inquiry.v1.adapters.output

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity

object InquiryOutputMapper {

    fun toOutput(entity: InquiryEntity, servicesById: Map<Int, ServiceEntity>): InquiryOutput {
        val workSessions = entity.actions.orEmpty().filterIsInstance<MissionNavActionEntity>()
        return InquiryOutput(
            id = requireNotNull(entity.id) { "InquiryEntity.id must be set to build an InquiryOutput" },
            vesselId = entity.vessel?.vesselId,
            missionId = entity.missionId,
            type = entity.type,
            origin = entity.origin,
            status = entity.status,
            conclusion = entity.conclusion,
            startDateTimeUtc = entity.startDateTimeUtc,
            endDateTimeUtc = entity.endDateTimeUtc,
            isSignedByInspector = entity.isSignedByInspector,
            service = entity.serviceId?.let { toServiceOutput(it, servicesById[it]) },
            infractions = toInfractionOutputs(workSessions),
            observations = workSessions.mapNotNull { it.observations?.takeIf(String::isNotBlank) }
        )
    }

    private fun toServiceOutput(serviceId: Int, service: ServiceEntity?): InquiryServiceOutput {
        return InquiryServiceOutput(
            id = serviceId,
            name = service?.name,
            controlUnitIds = service?.controlUnits.orEmpty()
        )
    }

    private fun toInfractionOutputs(workSessions: List<MissionNavActionEntity>): List<InquiryInfractionOutput> {
        return workSessions
            .flatMap { it.targets.orEmpty() }
            .flatMap { it.controls.orEmpty() }
            .flatMap { toInfractionOutputs(it) }
    }

    private fun toInfractionOutputs(control: ControlEntity): List<InquiryInfractionOutput> {
        return control.infractions.orEmpty().map {
            InquiryInfractionOutput(
                controlType = control.controlType,
                infractionType = it.infractionType,
                natinfs = it.natinfs,
                observations = it.observations
            )
        }
    }
}
