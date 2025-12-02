package fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.controlPolicies

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.analytics.ControlPoliciesEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2

@UseCase
class ComputeControlPolicies(
    private val computeProFishingControlPolicy: ComputeProFishingControlPolicy,
    private val computeNavControlPolicy: ComputeNavControlPolicy,
    private val computeEnvControlPolicy: ComputeEnvControlPolicy,
) {

    fun execute(mission: MissionEntity2?): ControlPoliciesEntity? {
        if (mission == null) return null

        return ControlPoliciesEntity(
            proFishing = computeProFishingControlPolicy.computeFishingRelatedInfractions(mission = mission),
            security = computeNavControlPolicy.execute(mission = mission, controlType = ControlType.SECURITY),
            navigation = computeNavControlPolicy.execute(mission = mission, controlType = ControlType.NAVIGATION),
            gensDeMer = computeNavControlPolicy.execute(mission = mission, controlType = ControlType.GENS_DE_MER),
            administrative = computeNavControlPolicy.execute(mission = mission, controlType = ControlType.ADMINISTRATIVE),
            envPollution = computeEnvControlPolicy.execute(mission = mission),
            other = computeProFishingControlPolicy.computeOtherInfractions(mission = mission),
        )
    }
}
