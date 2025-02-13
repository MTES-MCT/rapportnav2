package fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnitResource

import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.ControlUnitResourceEnv

interface IEnvControlUnitResourceRepository {

    fun findAll(): List<ControlUnitResourceEnv>?
}
