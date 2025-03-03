package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.AddOrUpdateMissionCrew
import org.slf4j.LoggerFactory

@UseCase
class CreateOrUpdateMissionCrew(
    private val addOrUpdateMissionCrew: AddOrUpdateMissionCrew,
    private val getGeneralInfo2: GetGeneralInfo2,
    private val deleteMissionCrew2: DeleteMissionCrew2,
) {
    private val logger = LoggerFactory.getLogger(CreateOrUpdateMissionCrew::class.java)

    fun execute(missionId: Int, updatedCrew: List<MissionCrewEntity>?): Boolean {
        if (updatedCrew == null) {
            logger.info("No crew to update for mission $missionId")
            return true
        }

        try {
            // Fetch the existing crew for the mission.
            val existingCrew = getGeneralInfo2.fetchCrew(missionId)

            // Determine crew to delete: existing crew members that are not in the updated list.
            val crewToDelete = existingCrew.filter { existingMember ->
                updatedCrew.none { updatedMember ->
                    (existingMember.id != null && updatedMember.id == existingMember.id) ||
                        (updatedMember.id == null &&
                            existingMember.agent.id == updatedMember.agent.id &&
                            existingMember.missionId == updatedMember.missionId)
                }
            }

            // Compute crew to add or update by subtracting the crew to delete from the updated crew.
            val crewToAddOrUpdate = updatedCrew.filter { updatedMember ->
                crewToDelete.none { deleteMember ->
                    (deleteMember.id != null && updatedMember.id == deleteMember.id) ||
                        (deleteMember.id == null &&
                            updatedMember.agent.id == deleteMember.agent.id &&
                            updatedMember.missionId == deleteMember.missionId)
                }
            }

            logger.info("Adding/updating ${crewToAddOrUpdate.size} crew members for mission $missionId")
            crewToAddOrUpdate.forEach { member ->
                addOrUpdateMissionCrew.addOrUpdateMissionCrew(member)
            }

            logger.info("Deleting ${crewToDelete.size} crew members from mission $missionId")
            crewToDelete.forEach { member ->
                member.id?.let { deleteMissionCrew2.execute(it) }
            }

            return true
        } catch (e: Exception) {
            logger.error("Error while updating crew for mission id $missionId: ${e.message}")
            return false
        }
    }
}
