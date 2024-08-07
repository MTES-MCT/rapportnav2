import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import ActionEnvObservationsUnit, { ActionEnvObservationsUnitProps } from './action-env-observations-unit.tsx'
import usePatchActionEnv from './use-patch-action-env'
import { beforeEach } from 'node:test'
import { mockQueryResult } from '../../../test-utils.tsx'

// Mock the custom hook
const patchObservationMock = vi.fn()

vi.mock('./use-patch-action-env', () => ({
  default: () => [patchObservationMock, { error: undefined }]
}))

// Mock the logSoftError function
// vi.mock('@mtes-mct/monitor-ui', () => ({
//   logSoftError: vi.fn()
// }))

const props = (observationsByUnit?: string): ActionEnvObservationsUnitProps => ({
  missionId: '1',
  actionId: '1',
  observationsByUnit,
  label: "Observations de l'unité"
})

describe('ActionEnvObservationsUnit', () => {
  beforeEach(() => {
    ;(usePatchActionEnv as any).mockReturnValue(mockQueryResult({ id: 'myId' }))
  })
  afterEach(() => {
    vi.clearAllMocks()
    vi.resetAllMocks()
  })

  it('renders correctly', () => {
    render(<ActionEnvObservationsUnit {...props()} />)
    expect(screen.getByText("Observations de l'unité")).toBeInTheDocument()
  })

  it('calls patch function on submit', async () => {
    const mockPatch = vi.fn().mockResolvedValue({ data: { patchActionEnv: true } })
    ;(usePatchActionEnv as jest.Mock).mockReturnValue([mockPatch])

    render(
      <ActionEnvObservationsUnit
        label="Test Label"
        missionId="123"
        actionId="456"
        observationsByUnit="Initial observations"
      />
    )

    // You'll need to simulate the submit action here
    // This depends on how your PatchableMonitorObservations component works
    // For example:
    fireEvent.change(screen.getByTestId('observations-by-unit'), { target: { value: 'dummy text' } })

    expect(mockPatch).toHaveBeenCalledWith({
      variables: {
        action: {
          missionId: '123',
          actionId: '456',
          observationsByUnit: 'Initial observations'
        }
      }
    })
  })

  // Add more tests as needed
})
