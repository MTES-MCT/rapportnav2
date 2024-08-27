import { describe, it, expect, vi, afterEach } from 'vitest'
import { render, screen, fireEvent } from '../../../../../../test-utils.tsx'
import ActionEnvObservationsUnit, { ActionEnvObservationsUnitProps } from './action-env-observations-unit.tsx'
import * as usePatchModule from '@features/pam/mission/hooks/use-patch-action-env'

const patchMock = vi.fn()
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
    vi.spyOn(usePatchModule, 'default').mockReturnValue([patchMock, { error: undefined }])
    render(<ActionEnvObservationsUnit {...props()} />)
    fireEvent.change(screen.getByTestId('observations-by-unit'), { target: { value: 'dummy text' } })

    await vi.waitFor(() => {
      expect(patchMock).toHaveBeenCalledWith({
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
