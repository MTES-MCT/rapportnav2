package fr.gouv.dgampa.rapportnav.domain.repositories.office

interface IOfficeConverter {

    /**
     * Converts an office document (e.g. docx -> odt, xlsx -> ods), the target format being
     * deduced from the destination extension. Returns the destination path.
     */
    fun convert(sourcePath: String, destinationPath: String): String
}
