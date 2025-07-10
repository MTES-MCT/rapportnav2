import { render, screen, fireEvent } from '../../../../../../test-utils.tsx'
import MissionDistanceAndConsumption from './mission-distance-consumption.tsx'
import { vi } from 'vitest'
import { MissionGeneralInfo } from '@common/types/mission-types.ts'
import * as useAddModule from '@features/pam/mission/hooks/use-add-update-distance-consumption'
import * as useIsMissionFinished from '@features/pam/mission/hooks/use-is-mission-finished.tsx'
import { THEME } from '@mtes-mct/monitor-ui'

const mutateMock = vi.fn()

const emptyMock = {}
const mock = (data: MissionGeneralInfo | any) => ({
  id: 1,
  distanceInNauticalMiles: 1,
  consumedGOInLiters: 1,
  consumedFuelInLiters: 1,
  ...data
})

const props = (info: MissionGeneralInfo = mock({})) => ({ info })

describe('MissionDistanceAndConsumption', () => {
  describe('Testing rendering', () => {
    it('should render', async () => {
      render(<MissionDistanceAndConsumption {...props(emptyMock)} />)
      expect(screen.getByText('Distance et consommation')).toBeInTheDocument()
    })
    it('should render with prefilled values', async () => {
      render(<MissionDistanceAndConsumption {...props(mock({}))} />)
      expect(screen.getByRole('distanceInNauticalMiles').value).toEqual('1')
      expect(screen.getByRole('consumedGOInLiters').value).toEqual('1')
      expect(screen.getByRole('consumedFuelInLiters').value).toEqual('1')
    })
  })

  describe('the validation when the mission is finished', () => {
    // const validationColor = THEME.color.maximumRed.toLowerCase()
    const validationColor = 'rgb(225, 0, 15)'
    beforeEach(() => {
      vi.spyOn(useIsMissionFinished, 'default').mockReturnValue({
        data: true,
        loading: false,
        error: null
      })
    })
    it('should be shown when fields are undefined', () => {
      render(
        <MissionDistanceAndConsumption
          {...props(
            mock({
              distanceInNauticalMiles: undefined,
              consumedGOInLiters: undefined,
              consumedFuelInLiters: undefined
            })
          )}
        />
      )
      expect(getComputedStyle(screen.getByRole('distanceInNauticalMiles')).borderColor).toBe(validationColor)
      expect(getComputedStyle(screen.getByRole('consumedGOInLiters')).borderColor).toBe(validationColor)
      expect(getComputedStyle(screen.getByRole('consumedFuelInLiters')).borderColor).toBe(validationColor)
    })
    it('should be shown when fields are null', () => {
      render(
        <MissionDistanceAndConsumption
          {...props(
            mock({
              distanceInNauticalMiles: null,
              consumedGOInLiters: null,
              consumedFuelInLiters: null
            })
          )}
        />
      )
      expect(getComputedStyle(screen.getByRole('distanceInNauticalMiles')).borderColor).toBe(validationColor)
      expect(getComputedStyle(screen.getByRole('consumedGOInLiters')).borderColor).toBe(validationColor)
      expect(getComputedStyle(screen.getByRole('consumedFuelInLiters')).borderColor).toBe(validationColor)
    })
    it('should not be shown when fields are 0', () => {
      render(
        <MissionDistanceAndConsumption
          {...props(
            mock({
              distanceInNauticalMiles: 0,
              consumedGOInLiters: 0,
              consumedFuelInLiters: 0
            })
          )}
        />
      )
      expect(getComputedStyle(screen.getByRole('distanceInNauticalMiles')).borderColor).not.toBe(validationColor)
      expect(getComputedStyle(screen.getByRole('consumedGOInLiters')).borderColor).not.toBe(validationColor)
      expect(getComputedStyle(screen.getByRole('consumedFuelInLiters')).borderColor).not.toBe(validationColor)
    })
  })
  describe('The update mutation', () => {
    beforeEach(() => {
      mutateMock.mockReset()
    })
    it('should be called when changing distanceInNauticalMiles', async () => {
      vi.spyOn(useAddModule, 'default').mockReturnValue([mutateMock, { error: undefined }])
      render(<MissionDistanceAndConsumption {...props(mock({}))} />)
      const field = screen.getByRole('distanceInNauticalMiles')
      const value = 2
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)
      expect(mutateMock).toHaveBeenCalledWith({
        variables: {
          info: {
            consumedFuelInLiters: 1,
            consumedGOInLiters: 1,
            distanceInNauticalMiles: value,
            id: 1,
            missionId: undefined
          }
        }
      })
    })
    it('should be called when changing consumedGOInLiters', async () => {
      render(<MissionDistanceAndConsumption {...props(mock({}))} />)
      const field = screen.getByRole('consumedGOInLiters')
      const value = 2
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)
      expect(mutateMock).toHaveBeenCalledWith({
        variables: {
          info: {
            consumedFuelInLiters: 1,
            consumedGOInLiters: value,
            distanceInNauticalMiles: 1,
            id: 1,
            missionId: undefined
          }
        }
      })
    })
    it('should be called when changing consumedFuelInLiters', async () => {
      render(<MissionDistanceAndConsumption {...props(mock({}))} />)
      const field = screen.getByRole('consumedFuelInLiters')
      const value = 2
      fireEvent.change(field, { target: { value } })
      fireEvent.blur(field)
      expect(mutateMock).toHaveBeenCalledWith({
        variables: {
          info: {
            consumedFuelInLiters: value,
            consumedGOInLiters: 1,
            distanceInNauticalMiles: 1,
            id: 1,
            missionId: undefined
          }
        }
      })
    })
  })
})
