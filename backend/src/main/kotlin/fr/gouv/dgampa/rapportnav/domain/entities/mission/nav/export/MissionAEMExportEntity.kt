package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export

data class MissionAEMExportEntity(
    private val fileName: String,
    private val fileContent: String
)
