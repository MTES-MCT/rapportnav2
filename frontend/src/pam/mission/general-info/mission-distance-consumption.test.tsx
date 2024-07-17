import { render, screen, fireEvent } from '../../../test-utils'
import MissionDistanceAndConsumption from './mission-distance-consumption.tsx'
import { vi } from 'vitest'
import { MissionGeneralInfo } from '../../../types/mission-types.ts'

const mutateMock = vi.fn()

vi.mock('./use-add-update-distance-consumption.tsx', () => ({
  default: () => [mutateMock, { error: undefined }]
}))

const emptyMock = {}
const mock = {
  id: 1,
  distanceInNauticalMiles: 1,
  consumedGOInLiters: 1,
  consumedFuelInLiters: 1
}

const props = (info: MissionGeneralInfo = mock) => ({ info })

describe('MissionDistanceAndConsumption', () => {
  describe('Testing rendering', () => {
    it('should render', async () => {
      render(<MissionDistanceAndConsumption {...props(emptyMock)} />)
      expect(screen.getByText('Distance et consommation')).toBeInTheDocument()
    })
    it('should render with prefilled values', async () => {
      render(<MissionDistanceAndConsumption {...props(mock)} />)
      expect(screen.getByRole('distanceInNauticalMiles').value).toEqual('1')
      expect(screen.getByRole('consumedGOInLiters').value).toEqual('1')
      expect(screen.getByRole('consumedFuelInLiters').value).toEqual('1')
    })
  })

  describe('The update mutation', () => {
    beforeEach(() => {
      mutateMock.mockReset()
    })
    it('should be called when changing distanceInNauticalMiles', async () => {
      render(<MissionDistanceAndConsumption {...props(mock)} />)
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
      render(<MissionDistanceAndConsumption {...props(mock)} />)
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
      render(<MissionDistanceAndConsumption {...props(mock)} />)
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
