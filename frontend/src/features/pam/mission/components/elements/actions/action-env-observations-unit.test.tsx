import { describe, it, expect, vi, afterEach } from 'vitest'
import { render, screen, fireEvent } from '../../../../../../test-utils.tsx'
import ActionEnvObservationsUnit, { ActionEnvObservationsUnitProps } from './action-env-observations-unit.tsx'

const mutateMock = vi.fn()
const props = (observationsByUnit?: string): ActionEnvObservationsUnitProps => ({
  missionId: '1',
  actionId: '1',
  observationsByUnit,
  label: "Observations de l'unité"
})

describe('ActionEnvObservationsUnit', () => {
  afterEach(() => {
    vi.clearAllMocks()
  })

  it('renders correctly', () => {
    render(<ActionEnvObservationsUnit {...props()} />)
    expect(screen.getByText("Observations de l'unité")).toBeInTheDocument()
  })

  it('calls patch function on submit', async () => {
    vi.mock('./use-patch-action-env.tsx', () => ({
      default: () => [mutateMock, { error: undefined, loading: false }]
    }))
    render(<ActionEnvObservationsUnit {...props()} />)
    fireEvent.change(screen.getByTestId('observations-by-unit'), { target: { value: 'dummy text' } })

    await vi.waitFor(() => {
      expect(mutateMock).toHaveBeenCalledWith({
        variables: {
          action: {
            missionId: '1',
            actionId: '1',
            observationsByUnit: 'dummy text'
          }
        }
      })
    })
  })
})
