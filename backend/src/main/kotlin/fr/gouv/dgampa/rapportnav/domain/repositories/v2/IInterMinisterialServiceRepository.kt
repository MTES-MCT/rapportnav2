package fr.gouv.dgampa.rapportnav.domain.repositories.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.InterMinisterialServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.InterMinisterialServiceModel

interface IInterMinisterialServiceRepository {

    fun save(service: InterMinisterialServiceEntity, generalInfo: MissionGeneralInfoEntity): InterMinisterialServiceModel
}
