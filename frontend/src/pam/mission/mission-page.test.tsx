import { GraphQLError } from 'graphql/error'
import { useNavigate } from 'react-router-dom'
import { vi } from 'vitest'
import { fireEvent, render, screen, waitFor } from '../../test-utils.tsx'
import { Mission } from '../../types/mission-types.ts'
import useMissionExcerpt from './general-info/use-mission-excerpt'
import MissionPage from './mission-page.tsx'

vi.mock('@unleash/proxy-client-react', async importOriginal => {
  const actual = await importOriginal()
  return {
    ...actual,
    useFlag: vi.fn(),
    default: vi.fn()
  }
})

// Mock the useApolloClient hook
const mockApolloClient = {
  clearStore: vi.fn(),
  cache: {
    evict: vi.fn()
  }
}

vi.mock('@apollo/client', async importOriginal => {
  const actual = await importOriginal()
  return {
    ...actual,
    useApolloClient: vi.fn(() => mockApolloClient)
  }
})

vi.mock('react-router-dom', async importOriginal => {
  const actual = await importOriginal()
  return {
    ...actual,
    useNavigate: vi.fn()
  }
})

vi.mock('./general-info/use-mission-excerpt', async importOriginal => {
  const actual = await importOriginal()
  return {
    ...actual,
    default: vi.fn()
  }
})
vi.mock('../../shared/use-apollo-last-sync', async importOriginal => {
  const actual = await importOriginal()
  return {
    ...actual,
    default: () => new Date().getTime().toString()
  }
})

const mock = {
  id: 1,
  startDateTimeUtc: '2024-01-01T00:00:00Z',
  endDateTimeUtc: '2024-01-12T01:00:00Z',
  actions: []
}

const mockedQueryResult = (mission?: Mission, loading: boolean = false, error: any = undefined) => ({
  data: mission,
  loading,
  error
})

describe('MissionPage', () => {
  describe('testing rendering', () => {
    test('should render loading', () => {
      ;(useMissionExcerpt as any).mockReturnValue(mockedQueryResult(undefined, true))
      render(<MissionPage />)
      expect(screen.getByText('Chargement...')).toBeInTheDocument()
    })

    test('should render error', () => {
      ;(useMissionExcerpt as any).mockReturnValue(mockedQueryResult(undefined, false, new GraphQLError('Error!')))
      render(<MissionPage />)
      expect(screen.getByText('Une erreur est survenue')).toBeInTheDocument()
    })

    test('should render content', () => {
      ;(useMissionExcerpt as any).mockReturnValue(mockedQueryResult(mock))
      render(<MissionPage />)
      expect(screen.getByText('Mission #2024-01-01')).toBeInTheDocument()
    })
  })

  describe('testing the actions', () => {
    test('should reset store and navigate when clicking on the X', async () => {
      const mockNavigate = vi.fn()
      ;(useNavigate as jest.Mock).mockReturnValue(mockNavigate)
      ;(useMissionExcerpt as any).mockReturnValue(mockedQueryResult(mock))
      render(<MissionPage />)

      const button = screen.getByRole('quit-mission-cross')
      fireEvent.click(button)

      expect(mockApolloClient.clearStore).toHaveBeenCalled()
      await waitFor(() => {
        expect(mockApolloClient.cache.evict).toHaveBeenCalled()
        expect(mockNavigate).toHaveBeenCalledWith('..')
      })
    })
    test('should reset store and navigate when clicking on the quit button', async () => {
      const mockNavigate = vi.fn()
      ;(useNavigate as jest.Mock).mockReturnValue(mockNavigate)
      ;(useMissionExcerpt as any).mockReturnValue(mockedQueryResult(mock))
      render(<MissionPage />)

      const button = screen.getByText('Fermer la mission')
      fireEvent.click(button)

      expect(mockApolloClient.clearStore).toHaveBeenCalled()
      await waitFor(() => {
        expect(mockApolloClient.cache.evict).toHaveBeenCalled()
        expect(mockNavigate).toHaveBeenCalledWith('..')
      })
    })
  })

  describe('The error path', () => {
    test('should show the error message', () => {
      const errorMessage = 'errorMessage'
      ;(useMissionExcerpt as any).mockReturnValue(mockedQueryResult(undefined, false, new GraphQLError(errorMessage)))
      render(<MissionPage />)
      expect(screen.getByText(errorMessage)).toBeInTheDocument()
    })

    test('should redirect to the missions page when clicking the button', async () => {
      const mockNavigate = vi.fn()
      ;(useNavigate as jest.Mock).mockReturnValue(mockNavigate)
      ;(useMissionExcerpt as any).mockReturnValue(mockedQueryResult(undefined, false, new GraphQLError('errorMessage')))
      render(<MissionPage />)
      const button = screen.getByText("Retourner à l'accueil")
      fireEvent.click(button)
      await waitFor(() => {
        expect(mockNavigate).toHaveBeenCalledWith('/')
      })
    })
  })
})
