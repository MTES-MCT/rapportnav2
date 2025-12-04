import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, waitFor } from '../../../../../../test-utils'
import MissionTargetList from '../mission-target-list'
import { ActionTargetTypeEnum } from '@common/types/env-mission-types'
import { TargetType } from '../../../../common/types/target-types.ts'

// Mock useTarget hook
const mockIsDefaultTarget = vi.fn()
const mockGetTargetType = vi.fn()
const mockGetAvailableControlTypes = vi.fn()

vi.mock('../../../hooks/use-target', () => ({
  useTarget: () => ({
    isDefaultTarget: mockIsDefaultTarget,
    getTargetType: mockGetTargetType,
    getAvailableControlTypes: mockGetAvailableControlTypes
  })
}))

const createMockFieldArray = (targets = []) => ({
  form: {
    values: { targets },
    setFieldValue: vi.fn()
  },
  remove: vi.fn(),
  push: vi.fn()
})

describe('MissionTargetList', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    mockGetTargetType.mockReturnValue(TargetType.VEHICLE)
  })

  it('renders non-default targets', () => {
    mockIsDefaultTarget.mockReturnValue(false)

    const targets = [
      { id: '1', vesselName: 'Vessel 1' },
      { id: '2', vesselName: 'Vessel 2' }
    ]

    const fieldArray = createMockFieldArray(targets)

    render(
      <MissionTargetList name="targets" fieldArray={fieldArray} actionNumberOfControls={5} controlsToComplete={[]} />,
      { formikValues: { targets } }
    )

    expect(screen.getAllByTestId('mission-target-item').length).toBe(2)
  })

  it('does not render default targets', () => {
    mockIsDefaultTarget.mockReturnValue(true)

    const targets = [{ id: '1', vesselName: 'Default Vessel' }]

    const fieldArray = createMockFieldArray(targets)

    render(
      <MissionTargetList name="targets" fieldArray={fieldArray} actionNumberOfControls={5} controlsToComplete={[]} />,
      { formikValues: { targets } }
    )

    expect(screen.queryByTestId('mission-target-item')).not.toBeInTheDocument()
  })

  it('renders mix of default and non-default targets correctly', () => {
    const targets = [
      { id: '1', vesselName: 'Default Vessel' },
      { id: '2', vesselName: 'Regular Vessel' },
      { id: '3', vesselName: 'Another Default' }
    ]

    mockIsDefaultTarget.mockImplementation(target => target.vesselName.includes('Default'))

    const fieldArray = createMockFieldArray(targets)

    render(
      <MissionTargetList name="targets" fieldArray={fieldArray} actionNumberOfControls={5} controlsToComplete={[]} />,
      { formikValues: { targets } }
    )

    // Should only render 1 non-default target
    expect(screen.getAllByTestId('mission-target-item').length).toBe(1)
  })

  it('removes correct target by index', async () => {
    mockIsDefaultTarget.mockReturnValue(false)

    const targets = [
      { id: '1', vesselName: 'Vessel 1' },
      { id: '2', vesselName: 'Vessel 2' },
      { id: '3', vesselName: 'Vessel 3' }
    ]

    const fieldArray = createMockFieldArray(targets)

    render(
      <MissionTargetList name="targets" fieldArray={fieldArray} actionNumberOfControls={5} controlsToComplete={[]} />,
      { formikValues: { targets } }
    )

    const deleteButtons = screen.getAllByTestId('delete-target')

    // Delete the second target
    deleteButtons[1].click()

    await waitFor(() => {
      expect(fieldArray.remove).toHaveBeenCalledWith(1)
    })
  })

  it('calls getTargetType with actionTargetType', () => {
    mockIsDefaultTarget.mockReturnValue(false)

    const targets = [{ id: '1', vesselName: 'Vessel 1' }]

    const fieldArray = createMockFieldArray(targets)

    render(
      <MissionTargetList
        name="targets"
        fieldArray={fieldArray}
        actionTargetType={ActionTargetTypeEnum.VEHICLE}
        actionNumberOfControls={5}
        controlsToComplete={[]}
      />,
      { formikValues: { targets } }
    )

    expect(mockGetTargetType).toHaveBeenCalledWith(ActionTargetTypeEnum.VEHICLE)
  })

  it('renders empty list when no targets', () => {
    const targets = []

    const fieldArray = createMockFieldArray(targets)

    render(
      <MissionTargetList name="targets" fieldArray={fieldArray} actionNumberOfControls={5} controlsToComplete={[]} />,
      { formikValues: { targets } }
    )

    expect(screen.queryByTestId('mission-target-item')).not.toBeInTheDocument()
  })

  it('renders empty list when all targets are default', () => {
    mockIsDefaultTarget.mockReturnValue(true)

    const targets = [
      { id: '1', vesselName: 'Default Vessel 1' },
      { id: '2', vesselName: 'Default Vessel 2' },
      { id: '3', vesselName: 'Default Vessel 3' }
    ]

    const fieldArray = createMockFieldArray(targets)

    render(
      <MissionTargetList name="targets" fieldArray={fieldArray} actionNumberOfControls={5} controlsToComplete={[]} />,
      { formikValues: { targets } }
    )

    expect(screen.queryByTestId('mission-target-item')).not.toBeInTheDocument()
  })

  it('handles targets array being undefined', () => {
    const fieldArray = {
      form: {
        values: {},
        setFieldValue: vi.fn()
      },
      remove: vi.fn(),
      push: vi.fn()
    }

    render(
      <MissionTargetList name="targets" fieldArray={fieldArray} actionNumberOfControls={5} controlsToComplete={[]} />,
      { formikValues: {} }
    )

    expect(screen.queryByTestId('mission-target-item')).not.toBeInTheDocument()
  })
})
