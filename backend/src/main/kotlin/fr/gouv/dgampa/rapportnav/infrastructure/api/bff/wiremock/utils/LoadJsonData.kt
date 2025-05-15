package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock.utils


object LoadJsonData {
    fun load(path: String): String? {
       return object {}.javaClass.getResource("/wiremock/$path")?.readText()
    }
}
