package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2


import java.util.*

class MissionActionCrossControlEntity(
    var id: UUID? = null,
    val nbrOfHours: Int? = null,
    val isSignedByInspector: Boolean? = null,
    val status: CrossControlStatusType? = null,
    val conclusion: CrossControlConclusionType? = null,
    val crossControlData: CrossControlDataEntity? = null
) {
    fun withId(id: UUID?) {
        this.id = id
    }

    fun fromCrossControlEntity(crossControl: CrossControlEntity?): MissionActionCrossControlEntity {
        return MissionActionCrossControlEntity(
            id = id,
            status = status,
            nbrOfHours = nbrOfHours,
            conclusion = conclusion,
            isSignedByInspector = isSignedByInspector,
            crossControlData = CrossControlDataEntity(
                type = crossControl?.type,
                origin = crossControl?.origin,
                agentId = crossControl?.agentId,
                vesselId = crossControl?.vesselId,
                serviceId = crossControl?.serviceId,
                isRefentialClosed = crossControl?.status == CrossControlStatusType.CLOSED
            )
        )
    }
}

