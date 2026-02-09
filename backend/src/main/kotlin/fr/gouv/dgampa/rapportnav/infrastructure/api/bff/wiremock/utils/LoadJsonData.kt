package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock.utils

//import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Mission
//import tools.jackson.databind.json.JsonMapper


object LoadJsonData {
    fun load(path: String): String? {
       return object {}.javaClass.getResource("/wiremock/$path")?.readText()
    }

    /*fun loadToMission(path: String): Mission? {
        val text = object {}.javaClass.getResource("/wiremock/$path")?.readText()
        return text?.let { JsonMapper().readValue(it, Mission::class.java) }
    }*/
}
