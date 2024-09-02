import * as usePatchModule from '@features/pam/mission/hooks/use-patch-mission-env'
import { vi } from 'vitest'
import { fireEvent, render, screen, waitFor } from '../../../../../test-utils.tsx'
import MissionObservationByUnit from './mission-observations-unit.tsx'

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

  it('should trigger 5 secondes after typing', () => {
    vi.useFakeTimers({ shouldAdvanceTime: true })
    const observationsByUnit = 'my observations!!!!!'
    const wrapper = render(<MissionObservationByUnit missionId={1} observationsByUnit={'My beautiful observation'} />)
    const element = wrapper.getByTestId('mission-general-observation')
    fireEvent.change(element, {
      target: { value: observationsByUnit }
    })
    expect(patchMock).not.toHaveBeenCalled()
    vi.advanceTimersByTime(5000)
    expect(patchMock).toHaveBeenCalled()
    expect(patchMock).toHaveBeenCalledWith({
      variables: { mission: expect.objectContaining({ observationsByUnit }) }
    })
  })

  it('should call update observations event empty', async () => {
    vi.useFakeTimers({ shouldAdvanceTime: true })
    const wrapper = render(<MissionObservationByUnit missionId={1} observationsByUnit={'My beautiful observation'} />)
    const element = wrapper.getByTestId('mission-general-observation')
    fireEvent.change(element, {
      target: { value: '' }
    })
    vi.advanceTimersByTime(5000)
    expect(patchMock).toHaveBeenCalled()
    expect(patchMock).toHaveBeenCalledWith({
      variables: { mission: expect.objectContaining({ observationsByUnit: '' }) }
    })
  })
})
