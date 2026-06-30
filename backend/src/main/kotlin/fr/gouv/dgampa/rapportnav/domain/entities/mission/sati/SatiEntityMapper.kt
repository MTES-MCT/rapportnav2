package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati.Sati
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati.SatiModelMapper.toModel

object SatiEntityMapper {
    fun merge(sati: SatiEntity, action: MissionAction): SatiEntity {
        return sati.copy(
            actionId = action.id?.toString() ?: "",
            startDatetimeUtc = action.actionDatetimeUtc,
            endDatetimeUtc = action.actionEndDatetimeUtc,
            vessel = sati.vessel?.copy(
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
                flagState = action.flagState,
                owner = SatiPartyEntity(
                    contact = ContactEntity(
                        id = null,
                        fullName = action.proprietorName,
                        email = action.proprietorEmails?.firstOrNull(),
                        phone = action.proprietorPhones?.firstOrNull(),
                        nationality = action.proprietorNationality,
                        address = AddressEntity(fullAddress = action.proprietorAddress)
                    )
                ),
                operator = (sati.vessel?.operator ?: SatiPartyEntity()).copy(
                    contact = (sati.vessel?.operator?.contact ?: ContactEntity()).copy(
                        fullName = action.operatorName,
                        email = action.operatorEmails?.firstOrNull(),
                        phone = action.operatorPhones?.firstOrNull(),
                        nationality = action.operatorNationality,
                        address = (sati.vessel?.operator?.contact?.address ?: AddressEntity()).copy(
                            fullAddress = action.operatorAddress
                        )
                    )
                )
            )
        )
    }

    fun merge(sati: SatiEntity?, input: Sati): SatiEntity? {
        return sati
    }
    fun isEquals(fromDb: SatiEntity?, input: SatiEntity): Boolean {
        if (fromDb == null) return false
        return toModel(fromDb) == toModel(input)
    }
}
