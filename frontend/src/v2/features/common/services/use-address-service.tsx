import { skipToken, useQuery } from '@tanstack/react-query'
import axios from 'axios'
import { Address } from '../types/address.ts'

// https://adresse.data.gouv.fr/outils/api-doc/adresse
const url = 'https://data.geopf.fr/geocodage/completion/'

interface AddressRequest {
  text: string
  type?: 'PositionOfInterest' | 'StreetAddress'
  poiType?: string
  lonlat?: number[]
  maximumResponses?: number // between 1 and 15
}

interface AddressResult {
  x: number
  y: number
  country: string
  city: string
  oldcity: string
  kind: string
  zipcode: string
  metropole: boolean
  fulltext: string
  classification: number
  names?: string[]
}

interface AddressResponse {
  status: string
  results: AddressResult[]
}

const transform = (result: AddressResult): Address => ({
  street: result.fulltext,
  zipcode: result.zipcode,
  town: result.names?.[0] ?? result.city,
  lat: result.y,
  lng: result.x,
  country: 'FR'
})

export const useAddressListQuery = (search?: string) => {
  const fetchAddresses = async (): Promise<AddressResult[]> => {
    const query = search ?? ''
    const isNumeric = /^\d+$/.test(query)
    const params: AddressRequest = isNumeric
      ? { text: query, type: 'StreetAddress', maximumResponses: 10 }
      : { text: query, type: 'PositionOfInterest', poiType: 'commune', maximumResponses: 10 }
    const response = await axios.get<AddressResponse>(url, { params })
    const results = response.data.results
    return isNumeric ? results.filter(r => r.kind === 'municipality') : results
  }

  return useQuery<Address[]>({
    queryKey: ['address', search],
    queryFn: search && search.length >= 3 ? () => fetchAddresses().then(results => results.map(transform)) : skipToken
  })
}
