package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.controlUnitResource

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.isPamShip
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnit.IEnvControlUnitRepository
import org.slf4j.LoggerFactory

/**
 * Resolves the MonitorEnv resource a PAM service's usage metrics are attached to: the service's control units
 * (MonitorEnv control unit ids) are fetched one at a time, and among their non-archived resources the single ship
 * (see [isPamShip]) is kept. Null when it can't be resolved unambiguously — callers decide whether that blocks or is skipped.
 */
@UseCase
class GetPamShipResource(
    private val controlUnitRepository: IEnvControlUnitRepository
) {
    private val logger = LoggerFactory.getLogger(GetPamShipResource::class.java)

    /** For the save paths: a MonitorEnv failure is logged and gives null, so it never blocks a save. */
    fun execute(service: ServiceEntity?): LegacyControlUnitResourceEntity? {
        return try {
            resolve(service)
        } catch (e: Exception) {
            logger.warn("GetPamShipResource: could not fetch MonitorEnv resources for service id=${service?.id}", e)
            null
        }
    }

    /** Same rule as [execute], but MonitorEnv failures propagate (the backfill aborts rather than mis-reporting). */
    fun resolve(service: ServiceEntity?): LegacyControlUnitResourceEntity? {
        if (service?.serviceType != ServiceTypeEnum.PAM) return null
        val controlUnitIds = service.controlUnits.orEmpty().distinct()
        if (controlUnitIds.isEmpty()) {
            logger.warn("GetPamShipResource: PAM service id=${service.id} has no control unit")
            return null
        }

        val ships = controlUnitIds.flatMap { activeShipsOf(controlUnitRepository.findById(it)) }
        if (ships.size != 1) {
            logger.warn(
                "GetPamShipResource: expected exactly one ship for PAM service id=${service.id}, " +
                    "controlUnits=$controlUnitIds, found ${ships.size}: ${ships.map { it.id }}"
            )
            return null
        }
        return ships.single()
    }

    private fun activeShipsOf(controlUnit: ControlUnitEntity): List<LegacyControlUnitResourceEntity> {
        return controlUnit.resources
            .filter { !it.isArchived && it.type?.isPamShip() == true }
            .mapNotNull { resource ->
                val id = resource.id ?: return@mapNotNull null
                LegacyControlUnitResourceEntity(id = id, controlUnitId = controlUnit.id, name = resource.name, type = resource.type)
            }
    }
}
