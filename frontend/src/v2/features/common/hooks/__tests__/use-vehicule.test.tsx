import { VehicleTypeEnum } from '@common/types/env-mission-types'
import { renderHook } from '@testing-library/react'
import { useVehicule } from '../use-vehicule'

describe('useVehicule', () => {
  it('should return vehicle type options', () => {
    const { result } = renderHook(() => useVehicule())
    expect(result.current.vehiculeTypeOptions.length).toEqual(4)
  })

  it('should return all vehicule type name', () => {
    const { result } = renderHook(() => useVehicule())
    Object.keys(VehicleTypeEnum).forEach(type => {
      expect(result.current.getVehiculeType(type as VehicleTypeEnum)).toBeDefined()
    })
  })
})
