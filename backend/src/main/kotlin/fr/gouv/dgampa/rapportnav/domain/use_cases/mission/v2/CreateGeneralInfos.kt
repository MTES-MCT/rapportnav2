package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew.JPAMissionCrewRepository
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class CreateGeneralInfos(
    private val generalInfoRepository: IMissionGeneralInfoRepository,
    private val crewRepository: JPAMissionCrewRepository
) {

    private val logger = LoggerFactory.getLogger(CreateGeneralInfos::class.java)

    fun execute(
        missionId: Int? = null,
        missionIdUUID: UUID? = null,
        generalInfo2: MissionGeneralInfo2
    ): MissionGeneralInfoEntity2? {
        return try {
            // Try saving main general info
            val savedGeneralInfo = generalInfoRepository.save(
                generalInfo2.toMissionGeneralInfoEntity(
                    missionId = missionId,
                    missionIdUUID = missionIdUUID
                )
            )

            // Save related crew safely (if any)
            val savedCrew = generalInfo2.crew?.mapNotNull { crew ->
                try {
                    val crewEntity = crew.toMissionCrewEntity(missionId = missionId, missionIdUUID = missionIdUUID)
                    crewRepository.save(crewEntity)
                } catch (e: Exception) {
                    // Log and skip crew member if saving fails
                    logger.info("Failed to save crew member: '{}'", e.message)
                    null
                }
            }

            // Return fully composed entity
            MissionGeneralInfoEntity2(
                data = MissionGeneralInfoEntity(
                    id = savedGeneralInfo.id,
                    missionId = savedGeneralInfo.missionId,
                    missionIdUUID = savedGeneralInfo.missionIdUUID,
                    missionReportType = generalInfo2.missionReportType
                ),
                crew = savedCrew?.map { MissionCrewEntity.fromMissionCrewModel(it) }
            )

        } catch (e: Exception) {
            // Log top-level failure
            logger.info("Failed to create general info: '{}'", e.message)
            null
        }
    }
}

