package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export

// if you update this enum, update it too in the API and frontend
enum class ExportModeEnum {
    INDIVIDUAL_MISSION,      // Export one single mission into one document
    COMBINED_MISSIONS_IN_ONE,       // Export several missions combined into one mission into one document
    MULTIPLE_MISSIONS_ZIPPED        // Export several missions into several documents into a zip
}
