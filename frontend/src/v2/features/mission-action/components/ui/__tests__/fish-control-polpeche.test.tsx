import { Infraction } from '@common/types/fish-mission-types'
import { describe, expect, it } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import { ActionFishControlInput } from '../../../types/action-type'
import FishControlPolpeche from '../fish-control-polpeche'

const defaultValues = {
  fishActionType: 'SEA_CONTROL',
  fishInfractions: [] as Infraction[],
  userTrigram: 'ABC'
} as ActionFishControlInput

describe('FishControlPolpeche Component', () => {
  it('renders the main fish control sections', () => {
    render(<FishControlPolpeche values={defaultValues} />)
    expect(screen.getByTestId('action-control-nav')).toBeInTheDocument()
    expect(screen.getByText('Saisi par')).toBeInTheDocument()
  })

  it('renders the user trigram when provided', () => {
    render(<FishControlPolpeche values={{ ...defaultValues, userTrigram: 'XYZ' }} />)
    expect(screen.getByText('XYZ')).toBeInTheDocument()
  })
})
