package fr.gouv.gmampa.rapportnav.infrastructure.utils

import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import fr.gouv.dgampa.rapportnav.config.JacksonConfig
import fr.gouv.dgampa.rapportnav.infrastructure.utils.APIGetCountryRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule
import java.net.http.HttpClient
import java.net.http.HttpResponse

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [APIGetCountryRepository::class])
@ContextConfiguration(classes = [JacksonConfig::class])
class APIGetCountryRepositoryTest {

    @MockitoBean
    private lateinit var mapper: JsonMapper

    @MockitoBean
    private var httpResponse: HttpResponse<String> = mock()

    @MockitoBean
    private lateinit var httpClientFactory: HttpClientFactory

    @MockitoBean
    private var httpClient: HttpClient = mock(HttpClient::class.java)

    private fun createRealMapper(): JsonMapper = JsonMapper.builder()
        .addModule(KotlinModule.Builder().build())
        .build()


    @Nested
    inner class GetCountries {

        @Test
        fun `should return list of countries from API`() {
            val realMapper = createRealMapper()
            val repository = APIGetCountryRepository<Any>(
                mapper = realMapper
            )

            val result = repository.getCountries()
            assertEquals(216, result.size)

            val france = result.find { it.iso2 == "FR" }

            assertNotNull(france?.flag)
            assertEquals("FRA", france?.iso3)
            assertEquals("France", france?.name)
        }
    }
}
