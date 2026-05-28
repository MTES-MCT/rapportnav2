import { vi, describe, it, expect, beforeEach } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import MissionActionItemEnvControl from '../mission-action-item-env-control'
import { MissionAction } from '../../../../common/types/mission-action'
import { ControlType } from '@common/types/control-types'
import { ActionTargetTypeEnum, VehicleTypeEnum } from '@common/types/env-mission-types'
import { ActionEnvControlInput } from '../../../types/action-type'

const mockHandleSubmit = vi.fn()
const mockInitValue = vi.fn()

vi.mock('../../../hooks/use-mission-action-env-control', () => ({
  useMissionActionEnvControl: () => ({
    initValue: mockInitValue(),
    handleSubmit: mockHandleSubmit
  })
}))

vi.mock('../../../../mission-target/hooks/use-target', () => ({
  useTarget: () => ({
    getAvailableEnvControlTypes: vi.fn().mockReturnValue([])
  })
}))

const mockAction = {
  id: '1234'
} as MissionAction

const defaultInitValue: ActionEnvControlInput = {
  dates: [new Date('2024-01-01'), new Date('2024-01-02')],
  geoCoords: [48.5, -3.2],
  observationsByUnit: 'some observation',
  actionNumberOfControls: 5,
  actionTargetType: ActionTargetTypeEnum.VEHICLE,
  vehicleType: VehicleTypeEnum.VESSEL,
  controlsToComplete: [],
  availableControlTypesForInfraction: [],
  themes: [{ id: 1, theme: 'Theme 1', subThemes: [] }],
  targets: [],
  infractions: [],
  tags: []
} as unknown as ActionEnvControlInput

describe('MissionActionItemEnvControl', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('renders nothing inside the form when initValue is null', () => {
    mockInitValue.mockReturnValue(null)
    render(<MissionActionItemEnvControl action={mockAction} onChange={vi.fn()} />)
    expect(screen.queryByTestId('action-control-nav')).not.toBeInTheDocument()
  })

  it('renders form elements when initValue is provided', () => {
    mockInitValue.mockReturnValue(defaultInitValue)
    render(<MissionActionItemEnvControl action={mockAction} onChange={vi.fn()} />)

    expect(screen.getByTestId('action-control-nav')).toBeInTheDocument()
    // themes
    expect(screen.getByTestId('theme')).toBeInTheDocument()
    // observations textarea
    expect(screen.getByText("Observation de l'unité sur le contrôle")).toBeInTheDocument()
    // diving checkbox
    expect(screen.getByText('Plongée au cours de l\'opération')).toBeInTheDocument()
    // incident checkbox
    expect(screen.getByText("L'opération à donné lieu à un incident (utilisation d'arme(s)/menottage)")).toBeInTheDocument()
  })

  it('shows incomplete control tag when controlsToComplete has items', () => {
    mockInitValue.mockReturnValue({
      ...defaultInitValue,
      controlsToComplete: [ControlType.SECURITY, ControlType.NAVIGATION]
    })
    render(<MissionActionItemEnvControl action={mockAction} onChange={vi.fn()} />)

    expect(screen.getByTestId('controls-to-complete-tag')).toBeInTheDocument()
    expect(screen.getByText("2")).toBeInTheDocument()
    expect(screen.getByText("types de contrôles à compléter")).toBeInTheDocument()
  })

  it('hides incomplete control tag when controlsToComplete is empty', () => {
    mockInitValue.mockReturnValue({
      ...defaultInitValue,
      controlsToComplete: []
    })
    render(<MissionActionItemEnvControl action={mockAction} onChange={vi.fn()} />)

    expect(screen.queryByTestId('controls-to-complete-tag')).not.toBeInTheDocument()
  })

  it('calls handleSubmit when FormikEffect triggers onChange', () => {
    mockInitValue.mockReturnValue(defaultInitValue)
    render(<MissionActionItemEnvControl action={mockAction} onChange={vi.fn()} />)

    // FormikEffect fires onChange on mount with initial values
    expect(mockHandleSubmit).toHaveBeenCalled()
  })
})
