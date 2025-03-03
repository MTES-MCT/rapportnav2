package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock

object ControlUnitResourcesStubs {
    fun configureStubs(wireMockServer: WireMockServer) {

        val json = "[\n" +
            "    {\n" +
            "        \"id\": 1002,\n" +
            "        \"controlUnit\": {\n" +
            "            \"id\": 10121,\n" +
            "            \"areaNote\": \"Atlantique\",\n" +
            "            \"administrationId\": 3,\n" +
            "            \"departmentAreaInseeCode\": null,\n" +
            "            \"isArchived\": false,\n" +
            "            \"name\": \"33F Lanvéoc - NH90 Caïman\",\n" +
            "            \"termsNote\": \"COM Atlantique\\nVérifier selon la zone de mission\"\n" +
            "        },\n" +
            "        \"controlUnitId\": 10121,\n" +
            "        \"isArchived\": false,\n" +
            "        \"name\": \"33F Lanvéoc\",\n" +
            "        \"note\": null,\n" +
            "        \"photo\": null,\n" +
            "        \"station\": {\n" +
            "            \"id\": 130,\n" +
            "            \"latitude\": 48.287047,\n" +
            "            \"longitude\": -4.461252,\n" +
            "            \"name\": \"Lanvéoc\"\n" +
            "        },\n" +
            "        \"stationId\": 130,\n" +
            "        \"type\": \"HELICOPTER\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": 276,\n" +
            "        \"controlUnit\": {\n" +
            "            \"id\": 10317,\n" +
            "            \"areaNote\": \"Calais jusqu’à Mont Saint-Michel (jusqu’à Brest pour des cas particulier)\",\n" +
            "            \"administrationId\": 2,\n" +
            "            \"departmentAreaInseeCode\": null,\n" +
            "            \"isArchived\": false,\n" +
            "            \"name\": \"BSAM Le Havre\",\n" +
            "            \"termsNote\": \"COD Manche Mer du Nord Atlantique\"\n" +
            "        },\n" +
            "        \"controlUnitId\": 10317,\n" +
            "        \"isArchived\": false,\n" +
            "        \"name\": \"507\",\n" +
            "        \"note\": null,\n" +
            "        \"photo\": null,\n" +
            "        \"station\": {\n" +
            "            \"id\": 136,\n" +
            "            \"latitude\": 49.49437,\n" +
            "            \"longitude\": 0.107929,\n" +
            "            \"name\": \"Le Havre\"\n" +
            "        },\n" +
            "        \"stationId\": 136,\n" +
            "        \"type\": \"AIRPLANE\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": 283,\n" +
            "        \"controlUnit\": {\n" +
            "            \"id\": 10317,\n" +
            "            \"areaNote\": \"Calais jusqu’à Mont Saint-Michel (jusqu’à Brest pour des cas particulier)\",\n" +
            "            \"administrationId\": 2,\n" +
            "            \"departmentAreaInseeCode\": null,\n" +
            "            \"isArchived\": false,\n" +
            "            \"name\": \"BSAM Le Havre\",\n" +
            "            \"termsNote\": \"COD Manche Mer du Nord Atlantique\"\n" +
            "        },\n" +
            "        \"controlUnitId\": 10317,\n" +
            "        \"isArchived\": false,\n" +
            "        \"name\": \"510\",\n" +
            "        \"note\": null,\n" +
            "        \"photo\": null,\n" +
            "        \"station\": {\n" +
            "            \"id\": 136,\n" +
            "            \"latitude\": 49.49437,\n" +
            "            \"longitude\": 0.107929,\n" +
            "            \"name\": \"Le Havre\"\n" +
            "        },\n" +
            "        \"stationId\": 136,\n" +
            "        \"type\": \"AIRPLANE\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": 1176,\n" +
            "        \"controlUnit\": {\n" +
            "            \"id\": 10335,\n" +
            "            \"areaNote\": \"Périmètre de la réserve naturelle des Bouches de Bonifacio\",\n" +
            "            \"administrationId\": 1004,\n" +
            "            \"departmentAreaInseeCode\": null,\n" +
            "            \"isArchived\": false,\n" +
            "            \"name\": \"Réserve Naturelle des Bouches de Bonifacio\",\n" +
            "            \"termsNote\": null\n" +
            "        },\n" +
            "        \"controlUnitId\": 10335,\n" +
            "        \"isArchived\": false,\n" +
            "        \"name\": \"Dacia Duster\",\n" +
            "        \"note\": null,\n" +
            "        \"photo\": null,\n" +
            "        \"station\": {\n" +
            "            \"id\": 185,\n" +
            "            \"latitude\": 41.491867,\n" +
            "            \"longitude\": 9.055084,\n" +
            "            \"name\": \"Pianottoli\"\n" +
            "        },\n" +
            "        \"stationId\": 185,\n" +
            "        \"type\": \"SEMI_RIGID\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": 9,\n" +
            "        \"controlUnit\": {\n" +
            "            \"id\": 10192,\n" +
            "            \"areaNote\": \"Haute-Corse (2B) bastia calvi santa maria poggio saint florent macinaggio centuri galeria l'ile rousse\",\n" +
            "            \"administrationId\": 2000,\n" +
            "            \"departmentAreaInseeCode\": null,\n" +
            "            \"isArchived\": false,\n" +
            "            \"name\": \"ULAM 2B\",\n" +
            "            \"termsNote\": null\n" +
            "        },\n" +
            "        \"controlUnitId\": 10192,\n" +
            "        \"isArchived\": false,\n" +
            "        \"name\": \"Acciolu – PM 451\",\n" +
            "        \"note\": \"N'existe plus\",\n" +
            "        \"photo\": null,\n" +
            "        \"station\": {\n" +
            "            \"id\": 213,\n" +
            "            \"latitude\": 42.681846,\n" +
            "            \"longitude\": 9.303704,\n" +
            "            \"name\": \"Saint-Florent\"\n" +
            "        },\n" +
            "        \"stationId\": 213,\n" +
            "        \"type\": \"SEMI_RIGID\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": 262,\n" +
            "        \"controlUnit\": {\n" +
            "            \"id\": 10269,\n" +
            "            \"areaNote\": \"Côtes-d’Armor (22) loguivy de la mer\",\n" +
            "            \"administrationId\": 6,\n" +
            "            \"departmentAreaInseeCode\": null,\n" +
            "            \"isArchived\": false,\n" +
            "            \"name\": \"BN Lezardrieux\",\n" +
            "            \"termsNote\": \"CORG Côtes-d’Armor (22)\"\n" +
            "        },\n" +
            "        \"controlUnitId\": 10269,\n" +
            "        \"isArchived\": false,\n" +
            "        \"name\": \"Actae – G1101\",\n" +
            "        \"note\": null,\n" +
            "        \"photo\": null,\n" +
            "        \"station\": {\n" +
            "            \"id\": 156,\n" +
            "            \"latitude\": 48.7864239,\n" +
            "            \"longitude\": -3.1049,\n" +
            "            \"name\": \"Lézardrieux\"\n" +
            "        },\n" +
            "        \"stationId\": 156,\n" +
            "        \"type\": \"FAST_BOAT\"\n" +
            "    }]"

        wireMockServer.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/api/v1/control_unit_resources"))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)
                )
        )
    }
}
