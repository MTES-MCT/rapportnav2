import { vi } from 'vitest'
import { fireEvent, render, screen, waitFor } from '../../../../../test-utils.tsx'
import MissionObservationByUnit from './mission-observations-unit.tsx'
import * as usePatchModule from '@features/pam/mission/hooks/use-patch-mission-env'

const patchMock = vi.fn()

describe('MissionObservation', () => {
  beforeEach(() => {
    vi.spyOn(usePatchModule, 'default').mockReturnValue([patchMock, { error: undefined }])
  })
  afterEach(() => {
    vi.clearAllMocks()
    vi.resetAllMocks()
  })

  it('should render observation', () => {
    render(<MissionObservationByUnit missionId={1} observationsByUnit={'My beautiful observation'} />)
    expect(patchMock).not.toHaveBeenCalled()
    expect(screen.getByText(`Observation générale à l'échelle de la mission (remarques, résumé)`)).toBeInTheDocument()
    expect(screen.getByText('My beautiful observation')).toBeInTheDocument()
  })

  it('should call update observation', () => {
    const wrapper = render(<MissionObservationByUnit missionId={1} observationsByUnit={'My beautiful observation'} />)
    const element = wrapper.getByTestId('mission-general-observation')
    fireEvent.change(element, {
      target: { value: 'my new observations' }
    })
    waitFor(() => {
      expect(patchMock).toHaveBeenCalled()
    })
  })

  it('should not call update observation under 4', () => {
    const wrapper = render(<MissionObservationByUnit missionId={1} observationsByUnit={'My beautiful observation'} />)
    const element = wrapper.getByTestId('mission-general-observation')
    fireEvent.change(element, {
      target: { value: 'my' }
    })
    expect(patchMock).not.toHaveBeenCalled()
  })
})
