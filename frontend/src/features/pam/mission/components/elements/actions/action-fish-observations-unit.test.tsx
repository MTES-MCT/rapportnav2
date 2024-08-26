import { describe, it, expect, vi, afterEach } from 'vitest'
import { render, screen, fireEvent } from '../../../../../../test-utils.tsx'
import ActionFishObservationsUnit, { ActionFishObservationsUnitProps } from './action-fish-observations-unit.tsx'

const mutateMock = vi.fn()
const props = (observationsByUnit?: string): ActionFishObservationsUnitProps => ({
  missionId: '1',
  actionId: '1',
  observationsByUnit,
  label: "Observations de l'unité"
})

describe('ActionFishObservationsUnit', () => {
  afterEach(() => {
    vi.clearAllMocks()
  })

  it('renders correctly', () => {
    render(<ActionFishObservationsUnit {...props()} />)
    expect(screen.getByText("Observations de l'unité")).toBeInTheDocument()
  })

  it('calls patch function on submit', async () => {
    vi.mock('./use-patch-action-fish.tsx', () => ({
      default: () => [mutateMock, { error: undefined, loading: false }]
    }))
    render(<ActionFishObservationsUnit {...props()} />)
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
