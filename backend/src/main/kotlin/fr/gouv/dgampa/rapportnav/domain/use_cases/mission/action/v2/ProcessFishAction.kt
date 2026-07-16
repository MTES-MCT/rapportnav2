package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidationPolicies
import java.util.*


@UseCase
class ProcessFishAction(
    getStatusForAction: GetStatusForAction,
    private val getComputeTarget: GetComputeTarget,
    private val getMissionDates: GetMissionDates,
    private val getComputeSati: GetComputeSati,
    private val entityValidityValidator: EntityValidityValidator
) : AbstractGetMissionAction(getStatusForAction) {

    fun execute(ownerId: UUID, action: MissionAction): MissionFishActionEntity {
        val entity = MissionFishActionEntity.fromFishAction(ownerId = ownerId, action = action)
        val sati = getComputeSati.execute(action = action)
        val targets = getComputeTarget.execute(actionId = entity.getActionId(), isControl = entity.isControl())

        entity.sati = sati
        entity.targets = targets

        val missionDates = getMissionDates.execute(missionId = ownerId)
        val policy = ValidationPolicies.forMissionStartDate(missionDates?.startDateTimeUtc)

        entity.computeValidity(validator = entityValidityValidator, policy = policy)
        return entity
    }
}
