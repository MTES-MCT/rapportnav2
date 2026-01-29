package fr.gouv.dgampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById

@UseCase
class GetControlUnitsForUser(
    private val getUserFromToken: GetUserFromToken,
    private val getServiceById: GetServiceById,
) {
    fun execute(): List<Int>? {
        return try {
            val user = getUserFromToken.execute() ?: return null
            val service = getServiceById.execute(user.serviceId)
            service?.controlUnits
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "GetControlUnitsForUser failed",
                originalException = e
            )
        }
    }
}
