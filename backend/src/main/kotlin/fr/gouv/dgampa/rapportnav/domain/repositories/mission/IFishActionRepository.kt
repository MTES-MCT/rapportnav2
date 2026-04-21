package fr.gouv.dgampa.rapportnav.domain.repositories.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.VesselEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.PortEntity
import fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.input.PatchActionInput

interface IFishActionRepository {
    fun findFishActions(missionId: Int): List<MissionAction>

    fun patchAction(actionId: String, action: PatchActionInput): MissionAction?

    fun getVessels(): List<VesselEntity>

    fun getPorts(): List<PortEntity>
}
