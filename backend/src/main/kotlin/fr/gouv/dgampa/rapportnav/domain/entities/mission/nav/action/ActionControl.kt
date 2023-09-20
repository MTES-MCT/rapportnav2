package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlEquipmentAndSecurity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationRules
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlVesselAdministrative

data class ActionControl(
    val id: Int?,
    val actionId: Int,
    val controlsVesselAdministrative: ControlVesselAdministrative?,
    val controlsGensDeMer: ControlGensDeMer?,
    val controlsNavigationRules: ControlNavigationRules?,
    val controlsEquipmentAndSecurity: ControlEquipmentAndSecurity?
)
