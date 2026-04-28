import { describe, expect, it } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import { ActionFishControlInput } from '../../../types/action-type'
import FishControlPolpeche from '../fish-control-polpeche'

const defaultValues = {
  fishActionType: 'SEA_CONTROL',
  fishInfractions: [
    {
      natinf: 12345,
      threat: 'Dissimulation'
    },
    {
      natinf: 22182,
      threat: 'Entrave à la justice'
    }
  ],
  userTrigram: 'ABC'
} as ActionFishControlInput

describe('FishControlPolpeche Component', () => {
  it('renders the main fish control sections', () => {
    render(<FishControlPolpeche values={defaultValues} />)
    expect(screen.getByTestId('action-control-nav')).toBeInTheDocument()
    expect(screen.getByText('Infractions')).toBeInTheDocument()
    expect(screen.getByText('Saisi par')).toBeInTheDocument()
  })

  it('renders the user trigram when provided', () => {
    render(<FishControlPolpeche values={{ ...defaultValues, userTrigram: 'XYZ' }} />)
    expect(screen.getByText('XYZ')).toBeInTheDocument()
  })

  it('renders fish infractions with natinfs', () => {
    render(<FishControlPolpeche values={defaultValues} />)
    expect(screen.getByText('NATINF : 12345', { exact: false })).toBeInTheDocument()
    expect(screen.getByText('Infraction 1 : Dissimulation', { exact: false })).toBeInTheDocument()
    expect(screen.getByText('NATINF : 22182', { exact: false })).toBeInTheDocument()
    expect(screen.getByText('Infraction 2 : Entrave à la justice', { exact: false })).toBeInTheDocument()
  })
})
