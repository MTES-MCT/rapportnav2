package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati.Sati
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati.SatiModelMapper.toModel

object SatiEntityMapper {
    fun merge(sati: SatiEntity, action: MissionAction): SatiEntity {
        return SatiEntity(
            id = sati.id,
            module = sati.module,
            actionId = action.id?.toString() ?: "",
            createdAt = sati.createdAt,
            updatedAt = sati.updatedAt,
            vessel = SatiVesselEntity(
                jpe = SatiJpeEntity(
                    pnoId = action.pnoReportId,
                    portName = action.lastDeparturePortName,
                    portId = action.lastDeparturePortLocode,
                    lastStopDate = action.lastDepartureDateTime,
                    tripNumber = action.tripNumber ?: sati.vessel?.jpe?.tripNumber,
                    pnoType = action.pnoPurpose ?: sati.vessel?.jpe?.pnoType,
                ),
                ircs = action.ircs,
                imo = action.imo,
                type = action.vesselType,
                name = action.vesselName,
                length = action.vesselLength,
                immat = action.internalReferenceNumber,
                extRef = action.externalReferenceNumber,
                owner = SatiPartyEntity(
                    contact = ContactEntity(
                        id = null,
                        createdAt = null,
                        fullName = action.proprietorName,
                        email = action.proprietorEmails?.firstOrNull(),
                        phone = action.proprietorPhones?.firstOrNull(),
                        nationality = action.proprietorNationality,
                        address = AddressEntity(
                            fullAddress = action.proprietorAddress
                        ),
                    )

                ),
                charterer = null,
                agent = sati.vessel?.agent,
                master = sati.vessel?.master,
                flagState = action.flagState,
            ),
            resource = sati.resource,
            inspectors = sati.inspectors,
            startDatetimeUtc = action.actionDatetimeUtc,
            endDatetimeUtc = action.actionEndDatetimeUtc
        )
    }

    fun merge(sati: SatiEntity?, input: Sati): SatiEntity? {
        return sati
    }

    fun isEquals(fromDb: SatiEntity?, entity: SatiEntity?): Boolean {
        if (fromDb == null || entity == null) return false
        return toModel(fromDb) == toModel(entity)
    }
}
