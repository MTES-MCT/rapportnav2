package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.CrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.PassengerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.GeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum

data class MissionGeneralInfoEntity(
    val data: GeneralInfoEntity? = null,
    val crew: List<CrewEntity>? = null,
    val passengers: List<PassengerEntity>? = null,
    val services: List<ServiceEntity>? = null
) {

    fun isCompleteForStats(): Boolean {
        val serviceType = data?.service?.serviceType?: services?.first()?.serviceType
        return when(serviceType) {
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

    fun setResources(resources: List<LegacyControlUnitResourceEntity>?) {
        return data?.resources = resources
    }

    fun isResourcesComplete(): Boolean {
        return this.data?.isResourcesNotUsed == true || this.data?.resources?.isNotEmpty() == true
    }

    private fun isCrewComplete(): Boolean {
        return !this.crew.isNullOrEmpty()
    }


    private fun isDataCompleteUlam(): Boolean {
        return this.data != null
            && this.isInterMinisterialOK()
            && this.isResourcesComplete()
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
