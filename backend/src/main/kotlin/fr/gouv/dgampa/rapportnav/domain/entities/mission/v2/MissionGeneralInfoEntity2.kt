package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity

data class MissionGeneralInfoEntity2(
    val data: MissionGeneralInfoEntity? = null,
    val crew: List<MissionCrewEntity>? = null,
    val services: List<ServiceEntity>? = null
) {
    fun isCompleteForStats(): Boolean {
        return this.isCrewComplete() && this.isDataComplete()
    }

    private fun isCrewComplete(): Boolean {
        return this.crew.isNullOrEmpty()
    }

    private fun isDataComplete(): Boolean {
        return this.data != null
            && this.data.consumedFuelInLiters != null
            && this.data.consumedGOInLiters != null
            && this.data.distanceInNauticalMiles != null
    }
}
