import { render, screen } from '../../../../test-utils.tsx'
import { vi } from 'vitest'
import Missions from './missions.tsx'
import { GraphQLError } from 'graphql/error'
import * as useMissionsModule from '@features/pam/missions/hooks/use-missions'

const mission = {
  id: '123',
  agent: {
    id: 'abc',
    firstName: 'firstName',
    lastName: 'lastName',
    services: []
  },
  comment: undefined,
  role: undefined
}

describe('Missions', () => {
  describe('Testing rendering', () => {
    test('should render loading', () => {
      vi.spyOn(useMissionsModule, 'default').mockReturnValue({ data: undefined, loading: true, error: null })
      render(<Missions />)
      expect(screen.getByText('Missions en cours de chargement')).toBeInTheDocument()
    })

    test('should render error', () => {
      vi.spyOn(useMissionsModule, 'default').mockReturnValue({
        data: undefined,
        loading: false,
        error: new GraphQLError('Error!')
      })
      render(<Missions />)
      expect(screen.getByText('Erreur: Error!')).toBeInTheDocument()
    })

    test('should render content', () => {
      vi.spyOn(useMissionsModule, 'default').mockReturnValue({ data: [mission], loading: false, error: null })
      render(<Missions />)
      expect(screen.getByText('Mes rapports de mission')).toBeInTheDocument()
    })
  })
})
