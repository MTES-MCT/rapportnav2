import { skipToken, useQuery } from '@tanstack/react-query'
import axios from 'axios'
import { Establishment } from '../types/etablishment.ts'

const url = 'https://recherche-entreprises.api.gouv.fr/search?q='

interface SearchEstablishment {
  siren: string
  nom_complet: string
  nom_raison_sociale: string
  date_fermeture?: string | null
  siege: {
    adresse: string
    siret: string
    geo_adresse: string
    code_postal: string
    libelle_commune: string
    est_siege?: boolean
  }
  matching_etablissements: {
    adresse: string
    siret: string
    geo_adresse: string
    code_postal: string
    libelle_commune: string
    est_siege?: boolean
  }[]
}

const filter = (value: SearchEstablishment) => {
  return value.date_fermeture === null || value.date_fermeture === undefined
}

const transform = (value: SearchEstablishment): Establishment[] => {
  const etablissements = value.matching_etablissements?.length > 0 ? value.matching_etablissements : [value.siege]
  return etablissements
    .map(etablissement => ({
      country: 'France',
      siren: value?.siren,
      siret: etablissement.siret,
      city: etablissement.libelle_commune,
      zipcode: etablissement.code_postal,
      address: etablissement.geo_adresse ?? etablissement.adresse,
      name: value.nom_raison_sociale ?? value.nom_complet,
      isHeadquarter: etablissement.est_siege
    }))
    .sort((a, b) => (b.isHeadquarter ? 1 : 0) - (a.isHeadquarter ? 1 : 0))
}

export const useEstablishmentListQuery = (search?: string) => {
  const fetchEtablishments = (): Promise<SearchEstablishment[]> =>
    axios.get(`${url}${search}&page=1&per_page=10`).then(response => response.data.results)

  const query = useQuery<Establishment[]>({
    queryKey: ['establishments', search],
    queryFn:
      search && search.length >= 3
        ? () =>
            fetchEtablishments().then(data => data.filter(value => filter(value)).flatMap(value => transform(value)))
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
            .then(data => data.flatMap(value => transform(value)))
            .then(data => (data.length > 0 ? data[0] : undefined)) as Promise<Establishment>
      : skipToken
  })
  return query
}
