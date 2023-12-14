package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentRoleRepository

@UseCase
class GetNatinfs(private val agentRoleRepository: IAgentRoleRepository) {

    fun execute(): List<NatinfEntity> {
        val natinf1 = NatinfEntity(
            infraction = "non respect blabla",
            natinfCode = 111
        )
        val natinf2 = NatinfEntity(
            infraction = "mise en danger blabla",
            natinfCode = 222
        )
        val natinf3 = NatinfEntity(
            infraction = "filet peche blabla",
            natinfCode = 333
        )
        return listOf(natinf1, natinf2, natinf3)
    }
}
