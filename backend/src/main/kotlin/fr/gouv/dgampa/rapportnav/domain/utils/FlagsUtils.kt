package fr.gouv.dgampa.rapportnav.domain.utils

import com.neovisionaries.i18n.CountryCode

class FlagsUtils {
    fun isEuMember(countryCode: CountryCode): Boolean {
        val euCountries = setOf(
            CountryCode.AT, CountryCode.BE, CountryCode.BG, CountryCode.CY, CountryCode.CZ,
            CountryCode.DE, CountryCode.DK, CountryCode.EE, CountryCode.ES, CountryCode.FI,
            CountryCode.FR, CountryCode.GR, CountryCode.HR, CountryCode.HU, CountryCode.IE,
            CountryCode.IT, CountryCode.LT, CountryCode.LU, CountryCode.LV, CountryCode.MT,
            CountryCode.NL, CountryCode.PL, CountryCode.PT, CountryCode.RO, CountryCode.SE,
            CountryCode.SI, CountryCode.SK
        )
        return euCountries.contains(countryCode)
    }
}
