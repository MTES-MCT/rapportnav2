import { fireEvent, render, screen, waitFor } from '../../../../../../test-utils.tsx'
import { vi } from 'vitest'
import { MissionGeneralInfo } from '@common/types/mission-types.ts'
import UIThemeWrapper from '../../../../../common/components/ui/ui-theme-wrapper.tsx'
import MissionRecognizedVessel from './mission-recognized-vessel.tsx'
import * as useAddModule from '@features/pam/mission/hooks/use-add-update-distance-consumption'

const info = {
  id: 3,
  distanceInNauticalMiles: 23,
  consumedGOInLiters: 0,
  consumedFuelInLiters: 0,
  serviceId: 5,
  nbrOfRecognizedVessel: 0
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

  it('should call update information general', () => {
    const wrapper = render(
      <UIThemeWrapper>
        <MissionRecognizedVessel missionId={1} generalInfo={info} />
      </UIThemeWrapper>
    )
    const element = wrapper.getByTestId('mission-information-general-recognized-vessel')
    fireEvent.change(element, {
      target: { value: 9 }
    })
    expect(mutateMock).toHaveBeenCalled()
  })
})
