package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock.utils.LoadJsonData
import tools.jackson.databind.json.JsonMapper
import tools.jackson.databind.node.ArrayNode
import tools.jackson.databind.node.ObjectNode

object ControlUnitResourcesStubs {
    fun configureStubs(wireMockServer: WireMockServer) {

        val json = LoadJsonData.load("env/control_unit_resources.json")

        wireMockServer.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/api/v1/control_unit_resources"))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)
                )
        )

        // MonitorEnv GET /api/v2/control_units/{id}, derived from the same stub files so there is a single source.
        fullControlUnitsById(json, LoadJsonData.load("env/control_units.json")).forEach { (controlUnitId, body) ->
            wireMockServer.stubFor(
                WireMock.get(WireMock.urlPathEqualTo("/api/v2/control_units/$controlUnitId"))
                    .willReturn(
                        WireMock.aResponse()
                            .withHeader("Content-Type", "application/json")
                            .withBody(body)
                    )
            )
        }
    }

    /**
     * Groups the resources by control unit into the subset of MonitorEnv's FullControlUnitDataOutput we read:
     * `{ id, name, isArchived, administrationId, administration, controlUnitResourceIds, controlUnitResources }`.
     * The administration name comes from the v1 control units file, when the unit is there.
     */
    fun fullControlUnitsById(resourcesJson: String?, controlUnitsJson: String? = null): Map<Int, String> {
        if (resourcesJson == null) return mapOf()
        val mapper = JsonMapper()
        val administrationByUnitId = controlUnitsJson?.let { mapper.readTree(it) }?.let { units ->
            (0 until units.size()).map { units.get(it) }
                .associate { it.get("id").asInt() to it.get("administration")?.asString() }
        }.orEmpty()
        val resources = mapper.readTree(resourcesJson)
        return (0 until resources.size()).map { resources.get(it) }
            .groupBy { it.get("controlUnitId").asInt() }
            .mapValues { (controlUnitId, unitResources) ->
                val controlUnit = unitResources.first().get("controlUnit")
                val unit: ObjectNode = mapper.createObjectNode()
                unit.put("id", controlUnitId)
                unit.put("name", controlUnit?.get("name")?.asString())
                unit.put("isArchived", controlUnit?.get("isArchived")?.asBoolean() ?: false)
                controlUnit?.get("administrationId")?.let { unit.put("administrationId", it.asInt()) }
                val administration: ObjectNode = unit.putObject("administration")
                controlUnit?.get("administrationId")?.let { administration.put("id", it.asInt()) }
                administration.put("name", administrationByUnitId[controlUnitId])
                val ids: ArrayNode = unit.putArray("controlUnitResourceIds")
                val list: ArrayNode = unit.putArray("controlUnitResources")
                unitResources.forEach { resource ->
                    ids.add(resource.get("id").asInt())
                    // Same shape as the v1 resource (controlUnit, station…), which is what MonitorEnv returns here.
                    list.add(resource)
                }
                mapper.writeValueAsString(unit)
            }
    }
}
