package fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnitResource

import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.ControlUnitResourceEnv
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.inputs.PatchResourceInput

interface IEnvControlUnitResourceRepository {

    fun findAll(): List<ControlUnitResourceEnv>

    fun patch(id: Int, resource: PatchResourceInput): ControlUnitResourceEnv
}
