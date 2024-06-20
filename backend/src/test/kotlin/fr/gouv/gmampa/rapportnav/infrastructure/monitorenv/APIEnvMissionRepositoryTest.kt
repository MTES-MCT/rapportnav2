package fr.gouv.gmampa.rapportnav.infrastructure.monitorenv

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.http.message.BasicHttpResponse
import com.google.gson.Gson
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.APIEnvMissionRepository
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [APIEnvMissionRepository::class])
class APIEnvMissionRepositoryTest {

    @MockBean
    private lateinit var objectMapper: ObjectMapper;

    @Mock
    private val httpClient: HttpClient = HttpClient.newBuilder().build();

    @Test
    fun `execute should update mission env with patch and observationByUnit`() {
        val mission = EnvMissionMock.create(observationsByUnit = "observationsByUnit");
        Mockito.`when`(httpClient.send(Mockito.any<HttpRequest>(), Mockito.any<HttpResponse.BodyHandler<String>>())).then {}
            .thenReturn(null);

        val envRepo = APIEnvMissionRepository(mapper = objectMapper)
        val response = envRepo.updateMission(missionId = 761, MissionEnvEntity(observationsByUnit = "MyObservations"))
        assertThat(response).isNotNull();
    }
}
