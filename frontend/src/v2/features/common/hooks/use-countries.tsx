import useCountryListQuery from '../services/use-country-service'
import { Country } from '../types/country-type'

interface CountryHook {
  getCountryName: (id?: string) => string
  countries: { label: string; value: string }[]
  getCountryFlag: (id?: string) => string | undefined
  getCountryById: (id?: string) => Country | string | undefined
  getCountries: (value?: string) => { label: string; value: string }[]
}

export function useCountry(): CountryHook {
  const { data: countries } = useCountryListQuery()

  const getCountryById = (id?: string): Country | string | undefined => {
    if (!id) return
    if (id.length > 3 || id.length < 2) return id
    return countries?.find(country => country.iso3 === id || country.iso2 === id)
  }

  const getCountryFlag = (id?: string): string | undefined => {
    if (!id || id.length > 3 || id.length < 2) return
    return countries?.find(country => country.iso3 === id || country.iso2 === id)?.flag
  }

  const getCountryName = (id?: string) => {
    if (!id) return 'Inconnu'
    const country = countries?.find(country => country.iso3 === id)
    return `${country?.iso3}  ${country?.name}`
  }

  const getCountries = (value?: string): { label: string; value: string }[] => {
    return (
      countries?.map(country => ({
        value: value?.length === 2 ? country?.iso2 : country.iso3,
        label: ` ${country.iso3} - ${country?.name}`
      })) ?? []
    )
  }
  return { getCountryById, getCountryFlag, getCountryName, getCountries, countries: getCountries() }
}
