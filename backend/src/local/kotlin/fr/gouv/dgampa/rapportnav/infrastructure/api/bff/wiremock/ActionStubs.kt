package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock

object ActionStubs {

    fun configureStubs(wireMockServer: WireMockServer) {
        wireMockServer.stubFor(
            WireMock.patch(WireMock.urlMatching("/api/v1/actions/.*"))
                .willReturn(
                    WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withTransformers("response-template")
                        .withBody(
                            """
                            {
                                "id": "{{request.pathSegments.[3]}}",
                                "actionType": "CONTROL",
                                "observationsByUnit": {{#if (jsonPath request.body '$.observationsByUnit')}}"{{jsonPath request.body '$.observationsByUnit'}}"{{else}}null{{/if}},
                                "actionStartDateTimeUtc": {{#if (jsonPath request.body '$.actionStartDateTimeUtc')}}"{{jsonPath request.body '$.actionStartDateTimeUtc'}}"{{else}}null{{/if}},
                                "actionEndDateTimeUtc": {{#if (jsonPath request.body '$.actionEndDateTimeUtc')}}"{{jsonPath request.body '$.actionEndDateTimeUtc'}}"{{else}}null{{/if}},
                                "hasDivingDuringOperation": {{jsonPath request.body '$.hasDivingDuringOperation' default='null'}},
                                "incidentDuringOperation": {{jsonPath request.body '$.incidentDuringOperation' default='null'}}
                            }
                            """.trimIndent()
                        )
                )
        )
    }
}