package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock.utils

import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Mission
import tools.jackson.databind.json.JsonMapper


object LoadJsonData {
    fun load(path: String): String? {
       return object {}.javaClass.getResource("/wiremock/$path")?.readText()
    }

    fun loadFromPath(path: String): String? {
        return  object {}.javaClass.getResource(path)?.readText()
    }

    fun loadMission(path: String): Mission? {
        val text = object {}.javaClass.getResource(path)?.readText()
        return text?.let { JsonMapper().readValue(it, Mission::class.java) }
    }
}
