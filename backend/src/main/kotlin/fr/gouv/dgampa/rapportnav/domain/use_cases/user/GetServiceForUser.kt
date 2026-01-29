package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById

@UseCase
class GetServiceForUser(
    private val getUserFromToken: GetUserFromToken,
    private val getServiceById: GetServiceById,
) {
    fun execute(): ServiceEntity? {
        return try {
            val user = getUserFromToken.execute() ?: return null
            getServiceById.execute(user.serviceId)
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "GetServiceForUser failed",
                originalException = e
            )
        }
    }
}
