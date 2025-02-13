package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.InterMinisterialServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.IInterMinisterialServiceRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.InterMinisterialServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.generalInfo.IDBInterMinisterialServiceRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository

@Repository
class JPAInterMinisterialServiceRepository(
    private val dbRepo: IDBInterMinisterialServiceRepository
): IInterMinisterialServiceRepository {
    override fun save(service: InterMinisterialServiceEntity, generalInfo: MissionGeneralInfoEntity): InterMinisterialServiceModel {
        return try {
            val model = InterMinisterialServiceModel.fromInterMinisterialServiceEntity(service, generalInfo)
            dbRepo.save(model)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save InterMinisterialService='${service.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving InterMinisterialService='${service.id}'",
                originalException = e
            )
        }
    }
}
