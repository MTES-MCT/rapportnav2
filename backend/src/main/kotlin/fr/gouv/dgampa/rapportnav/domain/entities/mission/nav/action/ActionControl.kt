package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlEquipmentAndSecurity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationRules
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlVesselAdministrative
import java.time.ZonedDateTime
import java.util.*

data class ActionControl(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime?,
    val controlsVesselAdministrative: ControlVesselAdministrative?,
    val controlsGensDeMer: ControlGensDeMer?,
    val controlsNavigationRules: ControlNavigationRules?,
    val controlsEquipmentAndSecurity: ControlEquipmentAndSecurity?
)
