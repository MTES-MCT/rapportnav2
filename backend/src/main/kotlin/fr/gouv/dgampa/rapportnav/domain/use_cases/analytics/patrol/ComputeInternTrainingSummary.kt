package fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerOrganization
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.mapStringToMissionPassengerOrganization
import fr.gouv.dgampa.rapportnav.domain.utils.ComputeDurationUtils

@UseCase
class ComputeInternTrainingSummary {

    fun execute(passengers: List<MissionPassengerEntity>?): Map<String, Int>? {
        if (passengers.isNullOrEmpty()) {
            return null
        }
        val interns = passengers.filter { it.isIntern == true}

        val nbAffMar = interns.filter { it.organization === MissionPassengerOrganization.AFF_MAR }.size
        val nbPostgraduate = interns.filter { it.organization === MissionPassengerOrganization.ESPMER }.size
        val nbHighSchool = interns.filter { it.organization === MissionPassengerOrganization.LYCEE }.size
        val nbOther = interns.filter { it.organization === MissionPassengerOrganization.OTHER }.size

        val totalInternDurationInHours: Int = interns
            .map { ComputeDurationUtils.durationInHours(it.startDateTimeUtc, it.endDateTimeUtc).toInt() }
            .reduceOrNull { acc, duration -> acc + duration } ?: 0

        return mapOf(
            "nbPassengers" to passengers.size,
            "nbInterns" to interns.size,
            "nbAffMarInterns" to nbAffMar,
            "nbPostgraduateInterns" to nbPostgraduate,
            "nbHighSchoolInterns" to nbHighSchool,
            "nbOtherInterns" to nbOther,
            "totalInternDurationInHours" to totalInternDurationInHours,
        )
    }
}
