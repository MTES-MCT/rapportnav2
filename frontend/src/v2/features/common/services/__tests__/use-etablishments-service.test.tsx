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

  it('should fetch and transform results when search is 3+ characters', async () => {
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
            }
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
        siret: '12345678900001',
        city: 'Paris',
        zipcode: '75001',
        address: '1 rue test 75001 Paris',
        name: 'Company RS'
      }
    ])
  })

  it('should fallback to adresse when geo_adresse is null', async () => {
    const apiResponse = {
      data: {
        results: [
          {
            siren: '111111111',
            nom_complet: 'Fallback Co',
            nom_raison_sociale: null,
            siege: {
              adresse: '5 avenue fallback',
              siret: '11111111100001',
              geo_adresse: null,
              code_postal: '69001',
              libelle_commune: 'Lyon'
            }
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
        siren: '111111111',
        siret: '11111111100001',
        city: 'Lyon',
        zipcode: '69001',
        address: '5 avenue fallback',
        name: 'Fallback Co'
      }
    ])
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

  it('should fetch and return the first result', async () => {
    const apiResponse = {
      data: {
        results: [
          {
            siren: '987654321',
            nom_complet: 'Single Co',
            nom_raison_sociale: 'Single RS',
            siege: {
              adresse: '10 blvd test',
              siret: '98765432100001',
              geo_adresse: '10 blvd test 13001 Marseille',
              code_postal: '13001',
              libelle_commune: 'Marseille'
            }
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
      address: '10 blvd test 13001 Marseille',
      name: 'Single RS'
    })
  })

  it('should error when results are empty (undefined is not valid query data)', async () => {
    mockedAxios.get.mockResolvedValueOnce({ data: { results: [] } })

    const { result } = renderHook(() => useLazyEstablishmentQuery('000000000'), { wrapper })

    await waitFor(() => expect(result.current.isError).toBe(true))
  })
})