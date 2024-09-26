import * as useMissionExcerptModule from '@features/pam/mission/hooks/use-mission-excerpt'
import { GraphQLError } from 'graphql/error'
import { useNavigate } from 'react-router-dom'
import { vi } from 'vitest'
import { fireEvent, render, screen, waitFor } from '../test-utils.tsx'
import MissionPage from './mission-page.tsx'

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

describe('MissionPage', () => {
  describe('testing rendering', () => {
    test('should render loading', () => {
      vi.spyOn(useMissionExcerptModule, 'default').mockReturnValue({ data: undefined, loading: true, error: null })

      render(<MissionPage />)
      expect(screen.getByText('Chargement...')).toBeInTheDocument()
    })

    test('should render error', () => {
      vi.spyOn(useMissionExcerptModule, 'default').mockReturnValue({
        data: undefined,
        loading: false,
        error: new GraphQLError('Error!')
      })
      render(<MissionPage />)
      expect(screen.getByText('Une erreur est survenue')).toBeInTheDocument()
    })

    test('should render content', () => {
      vi.spyOn(useMissionExcerptModule, 'default').mockReturnValue({ data: mock, loading: false, error: null })
      render(<MissionPage />)
      expect(screen.getByText('Mission #2024-01-01')).toBeInTheDocument()
    })
  })

  describe('testing the actions', () => {
    test('should reset store and navigate when clicking on the X', async () => {
      const mockNavigate = vi.fn()
      ;(useNavigate as jest.Mock).mockReturnValue(mockNavigate)
      vi.spyOn(useMissionExcerptModule, 'default').mockReturnValue({ data: mock, loading: false, error: null })
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
      vi.spyOn(useMissionExcerptModule, 'default').mockReturnValue({ data: mock, loading: false, error: null })
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
      vi.spyOn(useMissionExcerptModule, 'default').mockReturnValue({
        data: undefined,
        loading: false,
        error: new GraphQLError(errorMessage)
      })

      render(<MissionPage />)
      expect(screen.getByText(errorMessage)).toBeInTheDocument()
    })

    test('should redirect to the missions page when clicking the button', async () => {
      const mockNavigate = vi.fn()
      ;(useNavigate as jest.Mock).mockReturnValue(mockNavigate)
      vi.spyOn(useMissionExcerptModule, 'default').mockReturnValue({
        data: undefined,
        loading: false,
        error: new GraphQLError('errorMessage')
      })
      render(<MissionPage />)
      const button = screen.getByText("Retourner à l'accueil")
      fireEvent.click(button)
      await waitFor(() => {
        expect(mockNavigate).toHaveBeenCalledWith('/')
      })
    })
  })
})
