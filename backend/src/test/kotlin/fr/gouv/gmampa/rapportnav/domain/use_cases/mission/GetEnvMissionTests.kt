package fr.gouv.gmampa.rapportnav.domain.use_cases.mission

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [RapportNavApplication::class])
class GetEnvMissionTests {

  @Autowired
  private lateinit var getEnvMissions: GetEnvMissions

  @Test
  fun `execute Should return a list of four mission from MonitorEnv API`() {
    val wireMockServer = WireMockServer()
    wireMockServer.start()

    configureFor("monitorenv.din.developpement-durable.gouv.fr", 80)
    stubFor(get(urlEqualTo("/api/v1/missions"))
      .willReturn(aResponse()
        .withHeader("Content-Type", "application/json")
        .withBody()));


  }
}
