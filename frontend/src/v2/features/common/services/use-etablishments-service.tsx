import { skipToken, useQuery } from '@tanstack/react-query'
import axios from 'axios'
import { Establishment } from '../types/etablishment.ts'

const url = 'https://recherche-entreprises.api.gouv.fr/search?q='

interface SearchEstablishment {
  siren: string
  nom_complet: string
  nom_raison_sociale: string
  siege: {
    siret: string
    geo_adresse: string
    code_postal: string
    libelle_commune: string
  }
}

const transform = (value: SearchEstablishment) => ({
  country: 'France',
  siren: value?.siren,
  siret: value?.siege?.siret,
  city: value.siege.libelle_commune,
  zipcode: value?.siege?.code_postal,
  address: value?.siege?.geo_adresse,
  name: value.nom_raison_sociale ?? value.nom_complet
})

export const useEstablishmentListQuery = (search?: string) => {
  const fetchEtablishments = (): Promise<SearchEstablishment[]> =>
    axios.get(`${url}${search}&page=1&per_page=10`).then(response => response.data.results)

  const query = useQuery<Establishment[]>({
    queryKey: ['establishments', search],
    queryFn:
      search && search.length >= 3
        ? () => fetchEtablishments().then(data => data.map(value => transform(value)))
        : skipToken
  })
  return query
}

export const useLazyEstablishmentQuery = (siren?: string) => {
  const fetchEtablishment = (): Promise<SearchEstablishment[]> =>
    axios.get(`${url}${siren}&page=1&per_page=1`).then(response => response.data.results)

  const query = useQuery<Establishment>({
    queryKey: ['establishments', siren],
    queryFn: siren
      ? () =>
          fetchEtablishment()
            .then(data => data.map(value => transform(value)))
            .then(data => (data.length > 0 ? data[0] : undefined)) as Promise<Establishment>
      : skipToken
  })
  return query
}
