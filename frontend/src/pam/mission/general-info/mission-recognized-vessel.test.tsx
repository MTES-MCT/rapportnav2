import { fireEvent, render, screen } from '@testing-library/react'
import { vi } from 'vitest'
import { MissionGeneralInfo } from '../../../types/mission-types.ts'
import UIThemeWrapper from '../../../ui/ui-theme-wrapper.tsx'
import MissionRecognizedVessel from './mission-recognized-vessel.tsx'

const info = {
  id: 3,
  distanceInNauticalMiles: 23,
  consumedGOInLiters: 0,
  consumedFuelInLiters: 0,
  serviceId: 5,
  nbrOfRecognizedVessel: 0
} as MissionGeneralInfo

const updateGeneralInfoMock = vi.fn()

vi.mock('./use-add-update-distance-consumption.tsx', () => ({
  default: () => [updateGeneralInfoMock, { error: undefined }]
}))

describe('MissionRecognizedVessel', () => {
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
    expect(updateGeneralInfoMock).not.toHaveBeenCalled()
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
    expect(updateGeneralInfoMock).toHaveBeenCalled()
  })
})
