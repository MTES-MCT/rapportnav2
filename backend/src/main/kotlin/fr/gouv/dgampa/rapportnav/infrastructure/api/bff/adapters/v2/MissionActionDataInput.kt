package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import java.time.Instant

open class MissionActionDataInput(
    open val startDateTimeUtc: Instant,
    open val endDateTimeUtc: Instant? = null,
    open val observation: String? = null,
    open val controlSecurity: ControlSecurityEntity? = null,
    open val controlGensDeMer: ControlGensDeMerEntity? = null,
    open val controlNavigation: ControlNavigationEntity? = null,
    open val controlAdministrative: ControlAdministrativeEntity? = null,
)
