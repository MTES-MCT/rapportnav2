package fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.PassengerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.PassengerOrganization
import fr.gouv.dgampa.rapportnav.domain.utils.ComputeDurationUtils

@UseCase
class ComputeInternTrainingSummary {

    fun execute(passengers: List<PassengerEntity>?): Map<String, Int>? {
        if (passengers.isNullOrEmpty()) {
            return null
        }
        val interns = passengers.filter { it.isIntern == true}

        val nbAffMar = interns.filter { it.organization === PassengerOrganization.AFF_MAR }.size
        val nbPostgraduate = interns.filter { it.organization === PassengerOrganization.ESPMER }.size
        val nbHighSchool = interns.filter { it.organization === PassengerOrganization.LYCEE }.size
        val nbOther = interns.filter { it.organization === PassengerOrganization.OTHER }.size

        val totalInternDurationInDays: Int =
            interns.sumOf { ComputeDurationUtils.durationInDays(it.startDate, it.endDate)?.toInt() ?: 0 }

        return mapOf(
            "nbPassengers" to passengers.size,
            "nbInterns" to interns.size,
            "nbAffMarInterns" to nbAffMar,
            "nbPostgraduateInterns" to nbPostgraduate,
            "nbHighSchoolInterns" to nbHighSchool,
            "nbOtherInterns" to nbOther,
            "totalInternDurationInDays" to totalInternDurationInDays,
        )
    }
}
