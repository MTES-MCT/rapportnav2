import { render, screen } from '../../../../../test-utils.tsx'
import NatinfsMultiSelect from './natinfs-multi-select.tsx'
import { vi } from 'vitest'
import { GraphQLError } from 'graphql/error'
import * as useNatinfModule from '@features/pam/mission/hooks/use-natinfs'

const mock = [
  {
    infraction: 'text1',
    natinfCode: '1'
  }
]

describe('NatinfsMultiSelect', () => {
  describe('Testing rendering according to Query result', () => {
    test('renders loading state', async () => {
      vi.spyOn(useNatinfModule, 'default').mockReturnValue({ data: undefined, loading: true, error: null })
      const { container } = render(<NatinfsMultiSelect selectedNatinfs={[]} onChange={vi.fn()} />)
      expect(container.firstChild).toBeNull()
    })

    test('renders error state', async () => {
      vi.spyOn(useNatinfModule, 'default').mockReturnValue({
        data: undefined,
        loading: false,
        error: new GraphQLError('Error!')
      })
      const { container } = render(<NatinfsMultiSelect selectedNatinfs={[]} onChange={vi.fn()} />)
      expect(container.firstChild).toBeNull()
    })

    test('renders data', async () => {
      vi.spyOn(useNatinfModule, 'default').mockReturnValue({ data: mock, loading: false, error: null })
      render(<NatinfsMultiSelect selectedNatinfs={[]} onChange={vi.fn()} />)
      expect(screen.getByText('NATINF')).toBeInTheDocument()
    })
  })
})
