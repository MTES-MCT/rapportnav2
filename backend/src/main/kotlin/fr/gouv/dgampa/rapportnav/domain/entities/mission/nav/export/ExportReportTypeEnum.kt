package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export

// if you update this enum, update it too in the API and frontend
enum class ExportReportTypeEnum {
    ALL,       // all at once
    AEM,       // aka tableaux AEM
    PATROL     // aka rapport de patrouille
}
