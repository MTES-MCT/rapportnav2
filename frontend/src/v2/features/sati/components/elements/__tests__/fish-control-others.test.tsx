import { ControlType } from '@common/types/control-types'
import { describe, expect, it } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import { ActionFishControlInput } from '../../../../mission-action/types/action-type'
import FishControlOthers from '../fish-control-others'

const defaultValues = {
  fishActionType: 'SEA_CONTROL',
  userTrigram: 'ABC'
} as ActionFishControlInput

describe('FishControlOthers Component', () => {
  it('renders the target section label', () => {
    render(<FishControlOthers values={{} as ActionFishControlInput} controlsToComplete={[]} />, {
      formikValues: { targets: [] }
    })

    expect(screen.getByText('Autre(s) contrôle(s) effectué(s) par l’unité sur le navire')).toBeInTheDocument()
  })

  it('shows the incomplete control tag when controlsToComplete is provided', () => {
    render(<FishControlOthers values={{} as ActionFishControlInput} controlsToComplete={[ControlType.NAVIGATION]} />, {
      formikValues: { targets: [] }
    })

    expect(screen.getByTestId('controls-to-complete-tag')).toBeInTheDocument()
  })

  it('does not render the incomplete control tag when there are no controls to complete', () => {
    render(<FishControlOthers values={{} as ActionFishControlInput} controlsToComplete={[]} />, {
      formikValues: { targets: [] }
    })

    expect(screen.queryByTestId('controls-to-complete-tag')).toBeNull()
  })
})
