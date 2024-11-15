package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import java.util.*

class InfractionEnvDataByOutput(
    key: String,
    data: List<InfractionEnvDataOutput>
)

class InfractionEnvTargetOutput(
    val vesselIdentifier: String? = null,
    val companyName: String? = null,
    val relevantCourt: String? = null,
    val formalNotice: FormalNoticeEnum? = null,
    val toProcess: Boolean? = null,
    val identityControlledPerson: String? = null,
    val vesselType: VesselTypeEnum? = null,
    val vesselSize: VesselSizeEnum? = null,
){}

class InfractionEnvDataOutput (
    val id: String,
    var missionId: Int? = null,
    var actionId: String? = null,
    var controlId: UUID? = null,
    var controlType: ControlType? = null,
    val infractionType: InfractionTypeEnum? = null,
    var observations: String? = null,
    var natinfs: List<String>? = null,
    var target: InfractionEnvTargetOutput? = null
){
    companion object {
        fun fromInfractionEnvEntity(envInfraction: InfractionEntity): InfractionEnvDataOutput{
            return InfractionEnvDataOutput(
                id = envInfraction.id,
                infractionType = envInfraction.infractionType,
                observations = envInfraction.observations,
                natinfs = envInfraction.natinf,
                target = InfractionEnvTargetOutput(
                    companyName = envInfraction.companyName,
                    relevantCourt = envInfraction.relevantCourt,
                    formalNotice = envInfraction.formalNotice,
                    toProcess = envInfraction.toProcess,
                    vesselIdentifier = envInfraction.registrationNumber,
                    identityControlledPerson = envInfraction.controlledPersonIdentity,
                    vesselType = envInfraction.vesselType,
                    vesselSize = envInfraction.vesselSize
                )
            )
        }

        fun fromInfractionNavEntity(navInfraction: fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity): InfractionEnvDataOutput{
            return InfractionEnvDataOutput(
                id = navInfraction.id.toString(),
                infractionType = navInfraction.infractionType,
                observations = navInfraction.observations,
                controlId = navInfraction.controlId,
                controlType = navInfraction.controlType,
                actionId = navInfraction.actionId,
                natinfs = navInfraction.natinfs,
                target = InfractionEnvTargetOutput(
                    vesselType = navInfraction.target?.vesselType,
                    vesselSize = navInfraction.target?.vesselSize,
                    vesselIdentifier = navInfraction.target?.vesselIdentifier,
                    identityControlledPerson = navInfraction.target?.identityControlledPerson
                )
            )
        }
    }
}
