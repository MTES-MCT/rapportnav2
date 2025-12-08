package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById

@UseCase
class GetServiceForUser(
    private val getUserFromToken: GetUserFromToken,
    private val getServiceById: GetServiceById,
) {
    fun execute(): ServiceEntity? {
        val user = getUserFromToken.execute()
        return user?.let {
            getServiceById.execute(user.serviceId)
        }
    }
}
