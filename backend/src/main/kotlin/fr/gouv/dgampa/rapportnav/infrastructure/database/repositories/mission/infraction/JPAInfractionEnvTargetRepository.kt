package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEnvTargetEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionEnvTargetRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.InfractionEnvTargetModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.infraction.IDBInfractionEnvTargetRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional


@Repository
class JPAInfractionEnvTargetRepository(
    private val dbRepo: IDBInfractionEnvTargetRepository,
) : IInfractionEnvTargetRepository {

    @Transactional
    override fun save(
        infractionTarget: InfractionEnvTargetEntity,
        infraction: InfractionEntity
    ): InfractionEnvTargetModel {
        return try {
            val model = InfractionEnvTargetModel.fromInfractionEnvTargetEntity(infractionTarget, infraction)
            dbRepo.save(model)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save InfractionEnvTarget='${infractionTarget.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving InfractionEnvTarget='${infractionTarget.id}'",
                originalException = e
            )
        }
    }

    override fun findByActionIdAndVesselIdentifier(
        actionId: String,
        vesselIdentifier: String
    ): List<InfractionEnvTargetModel> {
        return try {
            val models = dbRepo.findByActionIdAndVesselIdentifier(actionId, vesselIdentifier)
            models
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error findByActionIdAndVesselIdentifier Infraction Env Target", e)
        }
    }


}
