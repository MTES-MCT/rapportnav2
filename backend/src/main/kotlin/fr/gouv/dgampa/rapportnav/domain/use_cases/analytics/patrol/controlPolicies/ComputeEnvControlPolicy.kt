package fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.controlPolicies

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.analytics.ControlPolicyData
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.helpers.CountInfractions
import org.slf4j.LoggerFactory


@UseCase
class ComputeEnvControlPolicy(
    private val countInfractions: CountInfractions = CountInfractions()
) {

    private val logger = LoggerFactory.getLogger(ComputeEnvControlPolicy::class.java)

    fun execute(mission: MissionEntity?): ControlPolicyData? {
        if (mission == null) {
            logger.warn("ComputeEnvControlPolicy.execute() called with null mission")
            return null
        }

        val envActions = mission.actions
            .orEmpty()
            .filterIsInstance<MissionEnvActionEntity>()

        if (envActions.isEmpty()) {
            logger.info("No environmental actions found for mission ${mission.id}")
            return ControlPolicyData()
        }

        val controls = envActions.filter { it.envActionType == ActionTypeEnum.CONTROL }

        val nbInfractionsWithRecord =
            countInfractions.countEnvInfractions(controls, InfractionTypeEnum.WITH_REPORT)
        val nbInfractionsWithoutRecord =
            countInfractions.countEnvInfractions(controls, InfractionTypeEnum.WITHOUT_REPORT)

        return ControlPolicyData(
            nbControls = controls.size,
            nbControlsSea = null,
            nbControlsLand = null,
            nbInfractionsWithRecord = nbInfractionsWithRecord,
            nbInfractionsWithoutRecord = nbInfractionsWithoutRecord
        )
    }
}
