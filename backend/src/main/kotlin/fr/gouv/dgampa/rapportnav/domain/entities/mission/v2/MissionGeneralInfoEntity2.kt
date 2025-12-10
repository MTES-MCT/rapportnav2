package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum

data class MissionGeneralInfoEntity2(
    val data: MissionGeneralInfoEntity? = null,
    val crew: List<MissionCrewEntity>? = null,
    val services: List<ServiceEntity>? = null
) {
    fun isCompleteForStats(): Boolean {
        return when(data?.service?.serviceType) {
            ServiceTypeEnum.PAM -> this.isCompletePam()
            ServiceTypeEnum.ULAM -> this.isCompleteUlam()
            else -> false
        }

    }

    fun isCompleteUlam(): Boolean {
        return this.isCrewComplete() && this.isDataCompleteUlam()
    }

    fun isCompletePam(): Boolean {
        return this.isCrewComplete() && this.isDataCompletePam()
    }

    private fun isCrewComplete(): Boolean {
        return !this.crew.isNullOrEmpty()
    }


    private fun isDataCompleteUlam(): Boolean {
        return this.data != null
            && !this.data.resources.isNullOrEmpty()
            && this.isInterMinisterialOK()
    }

    private fun isInterMinisterialOK(): Boolean {
        return if (this.data?.isWithInterMinisterialService == true) {
            !this.data.interMinisterialServices.isNullOrEmpty()
        } else {
            this.data?.interMinisterialServices.isNullOrEmpty()
        }
    }

    private fun isDataCompletePam(): Boolean {
        return this.data != null
            && this.data.consumedFuelInLiters != null
            && this.data.consumedGOInLiters != null
            && this.data.distanceInNauticalMiles != null
            && this.data.nbrOfRecognizedVessel != null
    }
}
