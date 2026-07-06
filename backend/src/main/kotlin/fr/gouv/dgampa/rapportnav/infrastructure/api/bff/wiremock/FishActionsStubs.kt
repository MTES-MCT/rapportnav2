package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock.utils.LoadJsonData

object FishActionsStubs {

    fun configureStubs(wireMockServer: WireMockServer) {
        val json = LoadJsonData.load("fish/actions.json")

        wireMockServer.stubFor(
            WireMock.get(WireMock.urlPathMatching("/api/v1/mission_actions"))
                .withQueryParam("missionId", WireMock.matching(".*"))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)
                )
        )

        wireMockServer.stubFor(
            WireMock.patch(WireMock.urlMatching("/api/v1/mission_actions/.*"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withTransformers("response-template")
                        .withBody(
                            """
                            {
                                "id": "{{request.pathSegments.[3]}}",
                                "flagState": "FR",
                                "missionId": 0,
                                "actionType": "SEA_CONTROL",
                                "actionDatetimeUtc": {{#if (jsonPath request.body '$.actionDatetimeUtc')}}"{{jsonPath request.body '$.actionDatetimeUtc'}}"{{else}}"2024-01-01T00:00:00Z"{{/if}},
                                "actionEndDatetimeUtc": {{#if (jsonPath request.body '$.actionEndDatetimeUtc')}}"{{jsonPath request.body '$.actionEndDatetimeUtc'}}"{{else}}null{{/if}},
                                "userTrigram": "ABC",
                                "hasSomeGearsSeized": false,
                                "hasSomeSpeciesSeized": false,
                                "completion": "COMPLETED",
                                "isFromPoseidon": false,
                                "isDeleted": false,
                                "observationsByUnit": {{#if (jsonPath request.body '$.observationsByUnit')}}"{{jsonPath request.body '$.observationsByUnit'}}"{{else}}null{{/if}},
                                "hasDivingDuringOperation": {{jsonPath request.body '$.hasDivingDuringOperation' default='null'}},
                                "incidentDuringOperation": {{jsonPath request.body '$.incidentDuringOperation' default='null'}}
                            }
                            """.trimIndent()
                        )
                )
        )
    }
}
