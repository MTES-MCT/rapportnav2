package fr.gouv.gmampa.rapportnav.infrastructure.bff.adapters

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CountryEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.CountryMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CountryMapperTest {

    private fun buildEntity(
        iso2: String? = "FR",
        iso3: String? = "FRA",
        name: String? = "France",
        flag: String? = "🇫🇷"
    ) = CountryEntity(iso2 = iso2, iso3 = iso3, name = name, flag = flag)

    @Nested
    inner class ToCountry {

        @Test
        fun `should map all fields`() {
            val entity = buildEntity()
            val result = CountryMapper.toCountry(entity)

            assertThat(result.iso2).isEqualTo("FR")
            assertThat(result.iso3).isEqualTo("FRA")
            assertThat(result.name).isEqualTo("France")
            assertThat(result.flag).isEqualTo("🇫🇷")
        }

        @Test
        fun `should map null fields`() {
            val entity = buildEntity(iso2 = null, iso3 = null, name = null, flag = null)
            val result = CountryMapper.toCountry(entity)

            assertThat(result.iso2).isNull()
            assertThat(result.iso3).isNull()
            assertThat(result.name).isNull()
            assertThat(result.flag).isNull()
        }

        @Test
        fun `should map partial fields`() {
            val entity = buildEntity(iso2 = "DE", iso3 = "DEU", name = null, flag = null)
            val result = CountryMapper.toCountry(entity)

            assertThat(result.iso2).isEqualTo("DE")
            assertThat(result.iso3).isEqualTo("DEU")
            assertThat(result.name).isNull()
            assertThat(result.flag).isNull()
        }
    }

    @Nested
    inner class ToCountries {

        @Test
        fun `should map empty list`() {
            val result = CountryMapper.toCountries(emptyList())
            assertThat(result).isEmpty()
        }

        @Test
        fun `should map single entity`() {
            val result = CountryMapper.toCountries(listOf(buildEntity()))
            assertThat(result).hasSize(1)
            assertThat(result[0].iso3).isEqualTo("FRA")
        }

        @Test
        fun `should map multiple entities preserving order`() {
            val entities = listOf(
                buildEntity(iso2 = "FR", iso3 = "FRA", name = "France"),
                buildEntity(iso2 = "DE", iso3 = "DEU", name = "Allemagne"),
                buildEntity(iso2 = "ES", iso3 = "ESP", name = "Espagne"),
            )
            val result = CountryMapper.toCountries(entities)

            assertThat(result).hasSize(3)
            assertThat(result[0].iso3).isEqualTo("FRA")
            assertThat(result[1].iso3).isEqualTo("DEU")
            assertThat(result[2].iso3).isEqualTo("ESP")
        }

        @Test
        fun `should map all fields for each entity`() {
            val entities = listOf(
                buildEntity(iso2 = "FR", iso3 = "FRA", name = "France", flag = "🇫🇷"),
                buildEntity(iso2 = "DE", iso3 = "DEU", name = "Allemagne", flag = "🇩🇪"),
            )
            val result = CountryMapper.toCountries(entities)

            assertThat(result[0].iso2).isEqualTo("FR")
            assertThat(result[0].flag).isEqualTo("🇫🇷")
            assertThat(result[1].iso2).isEqualTo("DE")
            assertThat(result[1].flag).isEqualTo("🇩🇪")
        }
    }
}
