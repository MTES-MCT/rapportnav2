import { MissionGeneralInfo } from '@common/types/mission-types.ts'
import * as useAddModule from '@features/pam/mission/hooks/use-add-update-distance-consumption'
import { vi } from 'vitest'
import { fireEvent, render, screen } from '../../../../../../test-utils.tsx'
import UIThemeWrapper from '../../../../../common/components/ui/ui-theme-wrapper.tsx'
import MissionRecognizedVessel from './mission-recognized-vessel.tsx'
import * as useIsMissionFinishedModule from '@features/pam/mission/hooks/use-is-mission-finished.tsx'
import { THEME } from '@mtes-mct/monitor-ui'

const info = {
  id: 3,
  distanceInNauticalMiles: 23,
  consumedGOInLiters: 0,
  consumedFuelInLiters: 0,
  serviceId: 5,
  nbrOfRecognizedVessel: 5
} as MissionGeneralInfo

const mutateMock = vi.fn()

describe('MissionRecognizedVessel', () => {
  beforeEach(() => {
    vi.spyOn(useAddModule, 'default').mockReturnValue([mutateMock, { error: undefined }])
  })
  afterEach(() => {
    vi.clearAllMocks()
    vi.resetAllMocks()
  })

  it('should render recognized vessel', () => {
    render(
      <UIThemeWrapper>
        <MissionRecognizedVessel missionId={1} generalInfo={info} />
      </UIThemeWrapper>
    )
    expect(mutateMock).not.toHaveBeenCalled()
    expect(screen.getByText(`Nombre total de navires reconnus dans les approches maritimes (ZEE)`)).toBeTruthy()
  })

  describe('Validation', () => {
    it('should show error validation when mission is finished but no info ', () => {
      vi.spyOn(useIsMissionFinishedModule, 'default').mockReturnValue(true)
      render(<MissionRecognizedVessel missionId={1} generalInfo={undefined} />)
      const element = screen.getByLabelText('Nombre total de navires reconnus dans les approches maritimes (ZEE)')
      expect(getComputedStyle(element).borderColor).toBe(THEME.color.maximumRed.toLowerCase())
    })
    it('should not show error validation when mission is finished but info ', () => {
      vi.spyOn(useIsMissionFinishedModule, 'default').mockReturnValue(true)
      render(<MissionRecognizedVessel missionId={1} generalInfo={info} />)
      const element = screen.getByLabelText('Nombre total de navires reconnus dans les approches maritimes (ZEE)')
      expect(getComputedStyle(element).borderColor).not.toBe(THEME.color.maximumRed.toLowerCase())
    })
    it('should not show error validation when mission is not finished but info ', () => {
      vi.spyOn(useIsMissionFinishedModule, 'default').mockReturnValue(false)
      render(<MissionRecognizedVessel missionId={1} generalInfo={info} />)
      const element = screen.getByLabelText('Nombre total de navires reconnus dans les approches maritimes (ZEE)')
      expect(getComputedStyle(element).borderColor).not.toBe(THEME.color.maximumRed.toLowerCase())
    })
    it('should not show error validation when mission is not finished and no info ', () => {
      vi.spyOn(useIsMissionFinishedModule, 'default').mockReturnValue(false)
      render(<MissionRecognizedVessel missionId={1} generalInfo={undefined} />)
      const element = screen.getByLabelText('Nombre total de navires reconnus dans les approches maritimes (ZEE)')
      expect(getComputedStyle(element).borderColor).not.toBe(THEME.color.maximumRed.toLowerCase())
    })
  })

  describe('Updating data', () => {
    it('should call update information general on change', () => {
      const nbrOfRecognizedVessel = 9
      vi.useFakeTimers({ shouldAdvanceTime: true })
      const wrapper = render(
        <UIThemeWrapper>
          <MissionRecognizedVessel missionId={1} generalInfo={info} />
        </UIThemeWrapper>
      )
      const element = wrapper.getByTestId('mission-information-general-recognized-vessel')
      fireEvent.change(element, {
        target: { value: nbrOfRecognizedVessel }
      })
      expect(mutateMock).not.toHaveBeenCalled()
      vi.advanceTimersByTime(5000)
      expect(mutateMock).toHaveBeenCalledTimes(1)
      expect(mutateMock).toHaveBeenCalledWith({
        variables: { info: expect.objectContaining({ nbrOfRecognizedVessel }) }
      })
    })
  })
})
