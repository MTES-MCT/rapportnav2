package fr.gouv.dgampa.rapportnav.domain.validation.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.AddressEntity
import fr.gouv.dgampa.rapportnav.domain.validation.RequiredFieldsValidator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AddressValidationRulesTest {

    private fun validate(address: AddressEntity) =
        RequiredFieldsValidator.validate(address, mapOf(AddressEntity::class.java to AddressValidationRules.rules))
            .map { it.field }

    private fun address(
        id: Int? = 1,
        street: String? = null,
        fullAddress: String? = null,
        zipcode: String? = null,
        town: String? = null,
        country: String? = null
    ) = AddressEntity(id = id, street = street, fullAddress = fullAddress, zipcode = zipcode, town = town, country = country)

    @Test
    fun `should require id regardless of fullAddress or the structured fields`() {
        val violations = validate(address(id = null, fullAddress = "1 rue de la Paix, 75000 Paris"))

        assertThat(violations).contains("id")
    }

    @Test
    fun `should be satisfied by fullAddress alone, with no structured fields`() {
        val violations = validate(address(fullAddress = "1 rue de la Paix, 75000 Paris"))

        assertThat(violations).doesNotContain("fullAddress", "street", "zipcode", "town", "country")
    }

    @Test
    fun `should be satisfied by the four structured fields alone, with no fullAddress`() {
        val violations = validate(address(street = "1 rue de la Paix", zipcode = "75000", town = "Paris", country = "FR"))

        assertThat(violations).doesNotContain("fullAddress", "street", "zipcode", "town", "country")
    }

    @Test
    fun `should require fullAddress when any one of the structured fields is missing`() {
        val violations = validate(address(street = "1 rue de la Paix", zipcode = "75000", town = "Paris", country = null))

        assertThat(violations).contains("fullAddress", "country")
        assertThat(violations).doesNotContain("street", "zipcode", "town")
    }

    @Test
    fun `should require all four structured fields when fullAddress is absent`() {
        val violations = validate(address())

        assertThat(violations).contains("fullAddress", "street", "zipcode", "town", "country")
    }

    @Test
    fun `should require nothing extra when both fullAddress and the structured fields are present`() {
        val violations = validate(
            address(
                fullAddress = "1 rue de la Paix, 75000 Paris",
                street = "1 rue de la Paix",
                zipcode = "75000",
                town = "Paris",
                country = "FR"
            )
        )

        assertThat(violations).doesNotContain("fullAddress", "street", "zipcode", "town", "country")
    }
}
