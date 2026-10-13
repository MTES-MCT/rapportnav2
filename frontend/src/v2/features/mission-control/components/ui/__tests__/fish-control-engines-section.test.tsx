import { GearControl, MissionActionType, WireType } from '@common/types/fish-mission-types'
import { render, screen } from '../../../../../../test-utils'
import { MissionFishActionData } from '../../../../common/types/mission-action'
import FishControlEnginesSection from '../fish-control-engines-section'

const baseAction: MissionFishActionData = {
  faoAreas: [],
  fishActionType: MissionActionType.SEA_CONTROL,
  userTrigram: 'ABC',
  fishInfractions: []
}

const buildGear = (overrides: Partial<GearControl> = {}): GearControl => ({
  averageWireThickness: undefined,
  comments: undefined,
  controlledMesh: undefined,
  declaredMesh: undefined,
  gearCode: 'OTB',
  gearMarkingIsCompliant: undefined,
  gearName: 'Chalut de fond',
  gearWasControlled: undefined,
  hasUncontrolledMesh: false,
  wireType: undefined,
  ...overrides
})

describe('FishControlEnginesSection', () => {
  it('renders the section title even without gear onboard', () => {
    render(<FishControlEnginesSection action={baseAction} />)
    expect(screen.getByText('Inspection des engins')).toBeInTheDocument()
  })

  it('renders a gear header and its measurements', () => {
    const gear = buildGear({
      declaredMesh: 60,
      controlledMesh: 58,
      averageWireThickness: 3,
      wireType: WireType.SINGLE
    })
    render(<FishControlEnginesSection action={{ ...baseAction, gearOnboard: [gear] }} />)
    expect(screen.getByText('OTB – Chalut de fond')).toBeInTheDocument()
    expect(screen.getByText('60 mm')).toBeInTheDocument()
    expect(screen.getByText('58 mm')).toBeInTheDocument()
    expect(screen.getByText('3 mm')).toBeInTheDocument()
    expect(screen.getByText('Simple')).toBeInTheDocument()
  })

  it('renders the wire type label for multiple wires', () => {
    const gear = buildGear({ wireType: WireType.MANY })
    render(<FishControlEnginesSection action={{ ...baseAction, gearOnboard: [gear] }} />)
    expect(screen.getByText('Multiple')).toBeInTheDocument()
  })

  it('shows the uncontrolled mesh checkbox only when flagged', () => {
    const gear = buildGear({ hasUncontrolledMesh: true })
    render(<FishControlEnginesSection action={{ ...baseAction, gearOnboard: [gear] }} />)
    expect(screen.getByText('Maillage non mesuré')).toBeInTheDocument()
  })

  it('does not show the uncontrolled mesh checkbox when not flagged', () => {
    const gear = buildGear({ hasUncontrolledMesh: false })
    render(<FishControlEnginesSection action={{ ...baseAction, gearOnboard: [gear] }} />)
    expect(screen.queryByText('Maillage non mesuré')).toBeNull()
  })

  it('renders gear comments when present', () => {
    const gear = buildGear({ comments: 'Filet endommagé' })
    render(<FishControlEnginesSection action={{ ...baseAction, gearOnboard: [gear] }} />)
    expect(screen.getByText('Filet endommagé')).toBeInTheDocument()
  })

  it('does not render a comments block when there are none', () => {
    const gear = buildGear({ comments: undefined })
    render(<FishControlEnginesSection action={{ ...baseAction, gearOnboard: [gear] }} />)
    expect(screen.queryByText(/autres mesures et dispositifs/)).toBeNull()
  })

  it('renders one block per gear onboard', () => {
    const gears = [buildGear({ gearCode: 'OTB' }), buildGear({ gearCode: 'GNS', gearName: 'Filet maillant' })]
    render(<FishControlEnginesSection action={{ ...baseAction, gearOnboard: gears }} />)
    expect(screen.getByText('OTB – Chalut de fond')).toBeInTheDocument()
    expect(screen.getByText('GNS – Filet maillant')).toBeInTheDocument()
  })
})
