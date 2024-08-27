import { render, screen } from '../../../../../test-utils.tsx'
import NatinfsFullNameList from './natinfs-fullname-list.tsx'
import * as useNatinfModule from '@features/pam/mission/hooks/use-natinfs'
import { GraphQLError } from 'graphql/error'
import { vi } from 'vitest'

const mock = [
  {
    infraction: 'text1',
    natinfCode: '1'
  }
]

describe('NatinfsFullNameList', () => {
  describe('Testing rendering according to Query result', () => {
    test('renders null when no input natinf', async () => {
      vi.spyOn(useNatinfModule, 'default').mockReturnValue({ data: undefined, loading: false, error: null })
      const { container } = render(<NatinfsFullNameList natinfs={undefined} />)
      expect(container.firstChild).toBeNull()
    })
    test('renders loading state', async () => {
      vi.spyOn(useNatinfModule, 'default').mockReturnValue({ data: undefined, loading: true, error: null })
      const { container } = render(<NatinfsFullNameList natinfs={[]} />)
      expect(container.firstChild).toBeNull()
    })
    test('renders error state', async () => {
      vi.spyOn(useNatinfModule, 'default').mockReturnValue({
        data: undefined,
        loading: false,
        error: new GraphQLError('Error!')
      })
      const { container } = render(<NatinfsFullNameList natinfs={[]} />)
      expect(container.firstChild).toBeNull()
    })
    test('renders empty text when empty natinfs', async () => {
      vi.spyOn(useNatinfModule, 'default').mockReturnValue({ data: mock, loading: false, error: null })
      render(<NatinfsFullNameList natinfs={[]} />)
      expect(screen.getByText('--')).toBeInTheDocument()
    })
    test('renders empty text when the input natinf does not match', async () => {
      vi.spyOn(useNatinfModule, 'default').mockReturnValue({ data: mock, loading: false, error: null })
      render(<NatinfsFullNameList natinfs={['2']} />)
      expect(screen.getByText('--')).toBeInTheDocument()
    })
    test('renders the full natinf name when the input natinf matches', async () => {
      vi.spyOn(useNatinfModule, 'default').mockReturnValue({ data: mock, loading: false, error: null })
      render(<NatinfsFullNameList natinfs={['1']} />)
      expect(screen.getByText('1 - text1')).toBeInTheDocument()
    })
  })
})
