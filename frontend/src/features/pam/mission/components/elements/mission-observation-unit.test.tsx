import * as usePatchModule from '@features/pam/mission/hooks/use-patch-mission-env'
import { vi } from 'vitest'
import { fireEvent, render, screen, waitFor } from '../../../../../test-utils.tsx'
import MissionObservationByUnit from './mission-observations-unit.tsx'
import { THEME } from '@mtes-mct/monitor-ui'
import * as useIsMissionFinishedModule from '@features/pam/mission/hooks/use-is-mission-finished.tsx'

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
    const element = screen.getByLabelText("Observation générale à l'échelle de la mission (remarques, résumé)")
    expect(element).toBeInTheDocument()
    expect(screen.getByText('My beautiful observation')).toBeInTheDocument()
  })

  describe('Validation', () => {
    it('should show error validation when mission is finished but no observations ', () => {
      vi.spyOn(useIsMissionFinishedModule, 'default').mockReturnValue(true)
      render(<MissionObservationByUnit missionId={1} observationsByUnit={undefined} />)
      const element = screen.getByLabelText("Observation générale à l'échelle de la mission (remarques, résumé)")
      expect(getComputedStyle(element).borderColor).toBe(THEME.color.maximumRed.toLowerCase())
    })
    it('should not show error validation when mission is finished but observations ', () => {
      vi.spyOn(useIsMissionFinishedModule, 'default').mockReturnValue(true)
      render(<MissionObservationByUnit missionId={1} observationsByUnit={'dummy'} />)
      const element = screen.getByLabelText("Observation générale à l'échelle de la mission (remarques, résumé)")
      expect(getComputedStyle(element).borderColor).not.toBe(THEME.color.maximumRed.toLowerCase())
    })
    it('should not show error validation when mission is not finished but observations ', () => {
      vi.spyOn(useIsMissionFinishedModule, 'default').mockReturnValue(false)
      render(<MissionObservationByUnit missionId={1} observationsByUnit={'dummy'} />)
      const element = screen.getByLabelText("Observation générale à l'échelle de la mission (remarques, résumé)")
      expect(getComputedStyle(element).borderColor).not.toBe(THEME.color.maximumRed.toLowerCase())
    })
    it('should not show error validation when mission is not finished and no observations ', () => {
      vi.spyOn(useIsMissionFinishedModule, 'default').mockReturnValue(false)
      render(<MissionObservationByUnit missionId={1} observationsByUnit={undefined} />)
      const element = screen.getByLabelText("Observation générale à l'échelle de la mission (remarques, résumé)")
      expect(getComputedStyle(element).borderColor).not.toBe(THEME.color.maximumRed.toLowerCase())
    })
  })

  describe('Updating the data', () => {
    it('should call update observation on change event', () => {
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
})
