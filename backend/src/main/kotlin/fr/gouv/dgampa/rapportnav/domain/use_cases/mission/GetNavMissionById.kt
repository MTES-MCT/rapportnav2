package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.NavMissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.AttachControlsToActionControl
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetServiceByControlUnit
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import org.slf4j.LoggerFactory

@UseCase
class GetNavMissionById(
    private val navActionControlRepository: INavActionControlRepository,
    private val navStatusRepository: INavActionStatusRepository,
    private val navFreeNoteRepository: INavActionFreeNoteRepository,
    private val attachControlsToActionControl: AttachControlsToActionControl,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
    private val getAgentsCrewByMissionId: GetAgentsCrewByMissionId,
    private val getServiceByControlUnit: GetServiceByControlUnit,
    private val navRescueRepository: INavActionRescueRepository,
    private val navNauticalEventRepository: INavActionNauticalEventRepository,
    private val navVigimerRepository: INavActionVigimerRepository,
    private val navAntiPollutionRepository: INavActionAntiPollutionRepository,
    private val navBAAEMRepository: INavActionBAAEMRepository,
    private val navPublicOrderRepository: INavActionPublicOrderRepository,
    private val navRepresentationRepository: INavActionRepresentationRepository,
    private val navIllegalImmigrationRepository: INavActionIllegalImmigrationRepository
) {
    private val logger = LoggerFactory.getLogger(GetNavMissionById::class.java)

    fun execute(missionId: Int?, controlUnits: List<LegacyControlUnitEntity>? = null): NavMissionEntity {
        if (missionId == null) {
            logger.error("GetNavMissionById received a null missionId")
            throw IllegalArgumentException("GetNavMissionById should not receive null missionId")
        }

        logger.info("Retrieving Nav data for missionId: {}", missionId)
        try {
            val controls = fetchControls(missionId)
            val statuses = fetchStatuses(missionId)
            val notes = fetchNotes(missionId)
            val generalInfo = fetchGeneralInfo(missionId)
            val crew = fetchCrew(missionId)
            val rescues = fetchRescues(missionId)
            val nauticalEvents = fetchNauticalEvents(missionId)
            val vigimer = fetchVigimer(missionId)
            val antiPollutions = fetchAntiPollutions(missionId)
            val baaemPermanences = fetchBaaemPermanences(missionId)
            val publicOrders = fetchPublicOrders(missionId)
            val representations = fetchRepresentations(missionId)
            val illegalImmigrations = fetchIllegalImmigrations(missionId)
            val services = fetchServices(controlUnits)

            val actions =
                controls + statuses + notes + rescues + nauticalEvents + vigimer + antiPollutions + baaemPermanences + publicOrders + representations + illegalImmigrations

            logger.info("GetNavMissionById - Retrieved Nav Actions for missionId={}: {}", missionId, actions)

            val mission = NavMissionEntity(
                id = missionId,
                actions = actions.filterNotNull(),
                generalInfo = generalInfo,
                crew = crew,
                services = services
            )
            logger.info("GetNavMissionById - Successfully retrieved all Nav data for missionId: {}", missionId)
            return mission
        } catch (e: Exception) {
            logger.error("GetNavMissionById - Error retrieving Nav data for missionId: {}", missionId, e)
            throw e
        }
    }

    private fun fetchControls(missionId: Int): List<NavActionEntity> {
        return try {
            navActionControlRepository.findAllByMissionId(missionId).map { actionControl ->
                attachControlsToActionControl.toNavAction(
                    actionId = actionControl.id.toString(),
                    action = actionControl
                ).toNavActionEntity()
            }
        } catch (e: Exception) {
            logger.error("Error fetching Nav controls for missionId: {}", missionId, e)
            emptyList()
        }
    }

    private fun fetchStatuses(missionId: Int): List<NavActionEntity> {
        return try {
            navStatusRepository.findAllByMissionId(missionId).map { it.toActionStatusEntity() }
                .map { it.toNavActionEntity() }
        } catch (e: Exception) {
            logger.error("Error fetching Nav statuses for missionId: {}", missionId, e)
            emptyList()
        }
    }

    private fun fetchNotes(missionId: Int): List<NavActionEntity> {
        return try {
            navFreeNoteRepository.findAllByMissionId(missionId).map { it.toActionFreeNoteEntity() }
                .map { it.toNavActionEntity() }
        } catch (e: Exception) {
            logger.error("Error fetching Nav notes for missionId: {}", missionId, e)
            emptyList()
        }
    }

    private fun fetchGeneralInfo(missionId: Int): MissionGeneralInfoEntity? {
        return try {
            getMissionGeneralInfoByMissionId.execute(missionId)
        } catch (e: Exception) {
            logger.error("Error fetching Nav general info for missionId: {}", missionId, e)
            throw e
        }
    }

    private fun fetchCrew(missionId: Int): List<MissionCrewEntity> {
        return try {
            getAgentsCrewByMissionId.execute(missionId)
        } catch (e: Exception) {
            logger.error("Error fetching Nav crew for missionId: {}", missionId, e)
            emptyList()
        }
    }

    private fun fetchRescues(missionId: Int): List<NavActionEntity> {
        return try {
            navRescueRepository.findAllByMissionId(missionId).map { it.toActionRescueEntity() }
                .map { it.toNavActionEntity() }
        } catch (e: Exception) {
            logger.error("Error fetching Nav rescues for missionId: {}", missionId, e)
            emptyList()
        }
    }

    private fun fetchNauticalEvents(missionId: Int): List<NavActionEntity> {
        return try {
            navNauticalEventRepository.findAllByMissionId(missionId).map { it.toActionNauticalEventEntity() }
                .map { it.toNavActionEntity() }
        } catch (e: Exception) {
            logger.error("Error fetching Nav nautical events for missionId: {}", missionId, e)
            emptyList()
        }
    }

    private fun fetchVigimer(missionId: Int): List<NavActionEntity> {
        return try {
            navVigimerRepository.findAllByMissionId(missionId).map { it.toActionVigimerEntity() }
                .map { it.toNavActionEntity() }
        } catch (e: Exception) {
            logger.error("Error fetching Nav vigimer for missionId: {}", missionId, e)
            emptyList()
        }
    }

    private fun fetchAntiPollutions(missionId: Int): List<NavActionEntity> {
        return try {
            navAntiPollutionRepository.findAllByMissionId(missionId).map { it.toAntiPollutionEntity() }
                .map { it.toNavActionEntity() }
        } catch (e: Exception) {
            logger.error("Error fetching Nav anti-pollutions for missionId: {}", missionId, e)
            emptyList()
        }
    }

    private fun fetchBaaemPermanences(missionId: Int): List<NavActionEntity> {
        return try {
            navBAAEMRepository.findAllByMissionId(missionId).map { it.toActionBAAEMPermanenceEntity() }
                .map { it.toNavActionEntity() }
        } catch (e: Exception) {
            logger.error("Error fetching Nav BAAEM permanences for missionId: {}", missionId, e)
            emptyList()
        }
    }

    private fun fetchPublicOrders(missionId: Int): List<NavActionEntity> {
        return try {
            navPublicOrderRepository.findAllByMissionId(missionId).map { it.toPublicOrderEntity() }
                .map { it.toNavActionEntity() }
        } catch (e: Exception) {
            logger.error("Error fetching Nav public orders for missionId: {}", missionId, e)
            emptyList()
        }
    }

    private fun fetchRepresentations(missionId: Int): List<NavActionEntity> {
        return try {
            navRepresentationRepository.findAllByMissionId(missionId).map { it.toRepresentationEntity() }
                .map { it.toNavActionEntity() }
        } catch (e: Exception) {
            logger.error("Error fetching Nav representations for missionId: {}", missionId, e)
            emptyList()
        }
    }

    private fun fetchIllegalImmigrations(missionId: Int): List<NavActionEntity> {
        return try {
            navIllegalImmigrationRepository.findAllByMissionId(missionId).map { it.toActionIllegalImmigrationEntity() }
                .map { it.toNavActionEntity() }
        } catch (e: Exception) {
            logger.error("Error fetching Nav illegal immigrations for missionId: {}", missionId, e)
            emptyList()
        }
    }

    private fun fetchServices(controlUnits: List<LegacyControlUnitEntity>?): List<ServiceEntity> {
        return try {
            getServiceByControlUnit.execute(controlUnits)
        } catch (e: Exception) {
            logger.error("Error fetching Nav services for controlUnits: {}", controlUnits, e)
            emptyList()
        }
    }
}
