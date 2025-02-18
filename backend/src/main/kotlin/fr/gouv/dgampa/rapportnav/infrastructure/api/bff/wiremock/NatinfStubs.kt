package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock

object NatinfStubs {

    fun configureStubs(wireMockServer: WireMockServer) {

        val json = "[{\n" +
            "        \"natinfCode\": 0,\n" +
            "        \"regulation\": null,\n" +
            "        \"infractionCategory\": \"Environnement\",\n" +
            "        \"infraction\": \"Autre natinf\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"natinfCode\": 1,\n" +
            "        \"regulation\": null,\n" +
            "        \"infractionCategory\": \"Environnement\",\n" +
            "        \"infraction\": \"Cgv - contravention de grande voirie\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"natinfCode\": 10000,\n" +
            "        \"regulation\": \"C.PENAL\",\n" +
            "        \"infractionCategory\": \"Environnement\",\n" +
            "        \"infraction\": \"Degradation ou deterioration legere d'un bien par inscription, signe ou dessin\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"natinfCode\": 10038,\n" +
            "        \"regulation\": \"parc national\",\n" +
            "        \"infractionCategory\": \"Environnement\",\n" +
            "        \"infraction\": \"Circulation, divagation non autorisee d'animaux au coeur d'un parc national - c/3\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"natinfCode\": 10041,\n" +
            "        \"regulation\": \"parc national\",\n" +
            "        \"infractionCategory\": \"Environnement\",\n" +
            "        \"infraction\": \"Circulation, divagation non autorisee d'animaux dans une reserve integrale au coeur d'un parc national - c/5\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"natinfCode\": 10054,\n" +
            "        \"regulation\": \"parc national\",\n" +
            "        \"infractionCategory\": \"Environnement\",\n" +
            "        \"infraction\": \"Enlevement non autorise de vegetaux non cultives du coeur d'un parc national - c/5\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"natinfCode\": 10055,\n" +
            "        \"regulation\": \"parc national\",\n" +
            "        \"infractionCategory\": \"Environnement\",\n" +
            "        \"infraction\": \"Transport non autorise de vegetaux non cultives provenant du coeur d'un parc national - c/4\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"natinfCode\": 10057,\n" +
            "        \"regulation\": \"parc national\",\n" +
            "        \"infractionCategory\": \"Environnement\",\n" +
            "        \"infraction\": \"Vente, mise en vente non autorisee de vegetaux non cultives provenant du coeur d'un parc national - c/5\"\n" +
            "    }]"

        wireMockServer.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/bff/v1/natinfs"))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)
                )
        )
    }
}
