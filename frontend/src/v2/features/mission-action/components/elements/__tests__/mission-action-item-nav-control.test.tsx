import { VesselTypeEnum } from '@common/types/mission-types'
import { ControlMethod } from '@common/types/control-types'
import { VesselSizeEnum } from '@common/types/env-mission-types'
import { vi, describe, it, expect, beforeEach } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import MissionActionItemNavControl from '../mission-action-item-nav-control'
import { MissionAction } from '../../../../common/types/mission-action'
import { ActionNavControlInput } from '../../../types/action-type'

const mockHandleSubmit = vi.fn()
const mockInitValue = vi.fn()

vi.mock('../../../hooks/use-mission-action-nav-control', () => ({
  useMissionActionNavControl: () => ({
    initValue: mockInitValue(),
    handleSubmit: mockHandleSubmit
  })
}))

const mockAction = {
  id: '1234',
  controlsToComplete: []
} as unknown as MissionAction

const defaultInitValue: ActionNavControlInput = {
  dates: [new Date('2024-01-01'), new Date('2024-01-02')],
  geoCoords: [48.5, -3.2],
  vesselSize: VesselSizeEnum.FROM_12_TO_24m,
  vesselIdentifier: 'ABC123',
  vesselType: VesselTypeEnum.FISHING,
  controlMethod: ControlMethod.SEA,
  identityControlledPerson: 'John Doe',
  observations: 'some observation',
  targets: []
} as unknown as ActionNavControlInput

describe('MissionActionItemNavControl', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('renders nothing inside the form when initValue is null', () => {
    mockInitValue.mockReturnValue(null)
    render(<MissionActionItemNavControl action={mockAction} onChange={vi.fn()} />)
    expect(screen.queryByTestId('action-control-nav')).not.toBeInTheDocument()
  })

  it('renders form elements when initValue is provided', () => {
    mockInitValue.mockReturnValue(defaultInitValue)
    render(<MissionActionItemNavControl action={mockAction} onChange={vi.fn()} />)

    expect(screen.getByTestId('action-control-nav')).toBeInTheDocument()
    // vessel fields
    expect(screen.getByText('Taille du navire')).toBeInTheDocument()
    expect(screen.getByText('Immatriculation')).toBeInTheDocument()
    expect(screen.getByText('Identité de la personne contrôlée')).toBeInTheDocument()
    // observations textarea
    expect(screen.getByText('Observations générales sur le contrôle')).toBeInTheDocument()
    // diving checkbox
    expect(screen.getByText("Plongée au cours de l'opération")).toBeInTheDocument()
    // incident checkbox
    expect(screen.getByText("L'opération à donné lieu à un incident (utilisation d'arme(s)/menottage)")).toBeInTheDocument()
  })

  it('calls handleSubmit when FormikEffect triggers onChange', () => {
    mockInitValue.mockReturnValue(defaultInitValue)
    render(<MissionActionItemNavControl action={mockAction} onChange={vi.fn()} />)

    // FormikEffect fires onChange on mount with initial values
    expect(mockHandleSubmit).toHaveBeenCalled()
  })
})
