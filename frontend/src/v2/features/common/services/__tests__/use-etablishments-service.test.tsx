import { renderHook, waitFor } from '../../../../../test-utils.tsx'
import { useEstablishmentListQuery, useLazyEstablishmentQuery } from '../use-etablishments-service.tsx'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import React from 'react'
import { afterEach, beforeEach, vi } from 'vitest'
import axios from 'axios'

vi.mock('axios')
const mockedAxios = vi.mocked(axios)

describe('useEstablishmentListQuery', () => {
  let queryClient: QueryClient
  let wrapper: React.FC<{ children: React.ReactNode }>

  beforeEach(() => {
    queryClient = new QueryClient({
      defaultOptions: { queries: { retry: false } }
    })
    wrapper = ({ children }) => <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
    vi.clearAllMocks()
  })

  afterEach(() => {
    queryClient.clear()
  })

  it('should not fetch when search is undefined', () => {
    renderHook(() => useEstablishmentListQuery(undefined), { wrapper })
    expect(mockedAxios.get).not.toHaveBeenCalled()
  })

  it('should not fetch when search is less than 3 characters', () => {
    renderHook(() => useEstablishmentListQuery('ab'), { wrapper })
    expect(mockedAxios.get).not.toHaveBeenCalled()
  })

  it('should fetch and transform matching_etablissements', async () => {
    const apiResponse = {
      data: {
        results: [
          {
            siren: '123456789',
            nom_complet: 'Company Full',
            nom_raison_sociale: 'Company RS',
            siege: {
              adresse: '1 rue test',
              siret: '12345678900001',
              geo_adresse: '1 rue test 75001 Paris',
              code_postal: '75001',
              libelle_commune: 'Paris'
            },
            matching_etablissements: [
              {
                adresse: '2 rue match',
                siret: '12345678900002',
                geo_adresse: '2 rue match 69001 Lyon',
                code_postal: '69001',
                libelle_commune: 'Lyon',
                est_siege: false
              }
            ]
          }
        ]
      }
    }
    mockedAxios.get.mockResolvedValueOnce(apiResponse)

    const { result } = renderHook(() => useEstablishmentListQuery('company'), { wrapper })

    await waitFor(() => expect(result.current.isSuccess).toBe(true))

    expect(mockedAxios.get).toHaveBeenCalledWith(
      'https://recherche-entreprises.api.gouv.fr/search?q=company&page=1&per_page=10'
    )
    expect(result.current.data).toEqual([
      {
        country: 'France',
        siren: '123456789',
        siret: '12345678900002',
        city: 'Lyon',
        zipcode: '69001',
        address: '2 rue match 69001 Lyon',
        name: 'Company RS',
        isHeadquarter: false
      }
    ])
  })

  it('should flatmap multiple matching_etablissements across results', async () => {
    const apiResponse = {
      data: {
        results: [
          {
            siren: '111111111',
            nom_complet: 'Multi Co',
            nom_raison_sociale: 'Multi RS',
            siege: {
              adresse: 'siege addr',
              siret: '11111111100000',
              geo_adresse: 'siege geo',
              code_postal: '75001',
              libelle_commune: 'Paris'
            },
            matching_etablissements: [
              {
                adresse: 'addr1',
                siret: '11111111100001',
                geo_adresse: 'geo1',
                code_postal: '75001',
                libelle_commune: 'Paris',
                est_siege: false
              },
              {
                adresse: 'addr2',
                siret: '11111111100002',
                geo_adresse: 'geo2',
                code_postal: '69001',
                libelle_commune: 'Lyon',
                est_siege: false
              }
            ]
          }
        ]
      }
    }
    mockedAxios.get.mockResolvedValueOnce(apiResponse)

    const { result } = renderHook(() => useEstablishmentListQuery('multi'), { wrapper })

    await waitFor(() => expect(result.current.isSuccess).toBe(true))

    expect(result.current.data).toHaveLength(2)
    expect(result.current.data?.[0]?.siret).toBe('11111111100001')
    expect(result.current.data?.[1]?.siret).toBe('11111111100002')
  })

  it('should sort headquarters first within matching_etablissements', async () => {
    const apiResponse = {
      data: {
        results: [
          {
            siren: '222222222',
            nom_complet: 'Sort Co',
            nom_raison_sociale: 'Sort RS',
            siege: {
              adresse: 'siege',
              siret: '22222222200000',
              geo_adresse: 'siege geo',
              code_postal: '75001',
              libelle_commune: 'Paris'
            },
            matching_etablissements: [
              {
                adresse: 'branch',
                siret: '22222222200001',
                geo_adresse: 'branch geo',
                code_postal: '69001',
                libelle_commune: 'Lyon',
                est_siege: false
              },
              {
                adresse: 'hq',
                siret: '22222222200002',
                geo_adresse: 'hq geo',
                code_postal: '75001',
                libelle_commune: 'Paris',
                est_siege: true
              }
            ]
          }
        ]
      }
    }
    mockedAxios.get.mockResolvedValueOnce(apiResponse)

    const { result } = renderHook(() => useEstablishmentListQuery('sort'), { wrapper })

    await waitFor(() => expect(result.current.isSuccess).toBe(true))

    expect(result.current.data?.[0]?.isHeadquarter).toBe(true)
    expect(result.current.data?.[0]?.siret).toBe('22222222200002')
    expect(result.current.data?.[1]?.isHeadquarter).toBe(false)
    expect(result.current.data?.[1]?.siret).toBe('22222222200001')
  })

  it('should fallback to adresse when geo_adresse is null', async () => {
    const apiResponse = {
      data: {
        results: [
          {
            siren: '333333333',
            nom_complet: 'Fallback Co',
            nom_raison_sociale: null,
            siege: {
              adresse: 'siege',
              siret: '33333333300000',
              geo_adresse: 'siege geo',
              code_postal: '75001',
              libelle_commune: 'Paris'
            },
            matching_etablissements: [
              {
                adresse: '5 avenue fallback',
                siret: '33333333300001',
                geo_adresse: null,
                code_postal: '69001',
                libelle_commune: 'Lyon',
                est_siege: false
              }
            ]
          }
        ]
      }
    }
    mockedAxios.get.mockResolvedValueOnce(apiResponse)

    const { result } = renderHook(() => useEstablishmentListQuery('fallback'), { wrapper })

    await waitFor(() => expect(result.current.isSuccess).toBe(true))

    expect(result.current.data).toEqual([
      {
        country: 'France',
        siren: '333333333',
        siret: '33333333300001',
        city: 'Lyon',
        zipcode: '69001',
        address: '5 avenue fallback',
        name: 'Fallback Co',
        isHeadquarter: false
      }
    ])
  })

  it('should fallback to siege when matching_etablissements is empty', async () => {
    const apiResponse = {
      data: {
        results: [
          {
            siren: '555555555',
            nom_complet: 'Siren Co',
            nom_raison_sociale: 'Siren RS',
            siege: {
              adresse: '10 rue siege',
              siret: '55555555500001',
              geo_adresse: '10 rue siege 75001 Paris',
              code_postal: '75001',
              libelle_commune: 'Paris'
            },
            matching_etablissements: []
          }
        ]
      }
    }
    mockedAxios.get.mockResolvedValueOnce(apiResponse)

    const { result } = renderHook(() => useEstablishmentListQuery('siren'), { wrapper })

    await waitFor(() => expect(result.current.isSuccess).toBe(true))

    expect(result.current.data).toEqual([
      {
        country: 'France',
        siren: '555555555',
        siret: '55555555500001',
        city: 'Paris',
        zipcode: '75001',
        address: '10 rue siege 75001 Paris',
        name: 'Siren RS',
        isHeadquarter: undefined
      }
    ])
  })

  it('should filter out closed establishments', async () => {
    const apiResponse = {
      data: {
        results: [
          {
            siren: '444444444',
            nom_complet: 'Closed Co',
            nom_raison_sociale: 'Closed RS',
            date_fermeture: '2023-01-01',
            siege: {
              adresse: 'closed',
              siret: '44444444400000',
              geo_adresse: 'closed geo',
              code_postal: '75001',
              libelle_commune: 'Paris'
            },
            matching_etablissements: [
              {
                adresse: 'closed branch',
                siret: '44444444400001',
                geo_adresse: 'closed branch geo',
                code_postal: '75001',
                libelle_commune: 'Paris',
                est_siege: false
              }
            ]
          }
        ]
      }
    }
    mockedAxios.get.mockResolvedValueOnce(apiResponse)

    const { result } = renderHook(() => useEstablishmentListQuery('closed'), { wrapper })

    await waitFor(() => expect(result.current.isSuccess).toBe(true))

    expect(result.current.data).toEqual([])
  })
})

describe('useLazyEstablishmentQuery', () => {
  let queryClient: QueryClient
  let wrapper: React.FC<{ children: React.ReactNode }>

  beforeEach(() => {
    queryClient = new QueryClient({
      defaultOptions: { queries: { retry: false } }
    })
    wrapper = ({ children }) => <QueryClientProvider client={queryClient}>{children}</QueryClientProvider>
    vi.clearAllMocks()
  })

  afterEach(() => {
    queryClient.clear()
  })

  it('should not fetch when siren is undefined', () => {
    renderHook(() => useLazyEstablishmentQuery(undefined), { wrapper })
    expect(mockedAxios.get).not.toHaveBeenCalled()
  })

  it('should fetch and return the first result from matching_etablissements', async () => {
    const apiResponse = {
      data: {
        results: [
          {
            siren: '987654321',
            nom_complet: 'Single Co',
            nom_raison_sociale: 'Single RS',
            siege: {
              adresse: '10 blvd test',
              siret: '98765432100000',
              geo_adresse: '10 blvd test 13001 Marseille',
              code_postal: '13001',
              libelle_commune: 'Marseille'
            },
            matching_etablissements: [
              {
                adresse: '20 blvd match',
                siret: '98765432100001',
                geo_adresse: '20 blvd match 13001 Marseille',
                code_postal: '13001',
                libelle_commune: 'Marseille',
                est_siege: true
              }
            ]
          }
        ]
      }
    }
    mockedAxios.get.mockResolvedValueOnce(apiResponse)

    const { result } = renderHook(() => useLazyEstablishmentQuery('987654321'), { wrapper })

    await waitFor(() => expect(result.current.isSuccess).toBe(true))

    expect(mockedAxios.get).toHaveBeenCalledWith(
      'https://recherche-entreprises.api.gouv.fr/search?q=987654321&page=1&per_page=1'
    )
    expect(result.current.data).toEqual({
      country: 'France',
      siren: '987654321',
      siret: '98765432100001',
      city: 'Marseille',
      zipcode: '13001',
      address: '20 blvd match 13001 Marseille',
      name: 'Single RS',
      isHeadquarter: true
    })
  })

  it('should error when results are empty (undefined is not valid query data)', async () => {
    mockedAxios.get.mockResolvedValueOnce({ data: { results: [] } })

    const { result } = renderHook(() => useLazyEstablishmentQuery('000000000'), { wrapper })

    await waitFor(() => expect(result.current.isError).toBe(true))
  })
})
