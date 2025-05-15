package fr.gouv.dgampa.rapportnav.domain.repositories.mission

import fr.gouv.cnsp.monitorfish.infrastructure.api.outputs.VesselIdentityDataOutput
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.input.PatchActionInput

interface IFishActionRepository {
    fun findFishActions(missionId: Int): List<MissionAction>

    fun patchAction(actionId: String, action: PatchActionInput): MissionAction?

    fun getVessels(): List<VesselIdentityDataOutput>
}
