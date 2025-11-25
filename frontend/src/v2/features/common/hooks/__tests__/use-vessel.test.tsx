import { VesselSizeEnum } from '@common/types/env-mission-types'
import { VesselTypeEnum } from '@common/types/mission-types'
import { renderHook } from '@testing-library/react'
import { ModuleType } from '../../types/module-type'
import { useVessel } from '../use-vessel'
import { describe } from 'vitest'

describe('useVessel', () => {
  it('should have every vessel type for ULAM module ', () => {
    const { result } = renderHook(() => useVessel())
    expect(result.current.getVesselTypeByModule(ModuleType.ULAM).length).toEqual(Object.keys(VesselTypeEnum).length)
  })

  it('should not have every vessel type for PAM module ', () => {
    const { result } = renderHook(() => useVessel())
    const response = result.current.getVesselTypeByModule(ModuleType.PAM)
    expect(response.length).toEqual(5)
    expect(response.filter(t => [VesselTypeEnum.SCHOOL, VesselTypeEnum.PASSENGER].includes(t.key))).toEqual([])
  })

  it('should return the right name of a vessel', () => {
    const { result } = renderHook(() => useVessel())
    expect(result.current.getVesselTypeName()).toEqual('')
    expect(result.current.getVesselTypeName(VesselTypeEnum.SCHOOL)).toEqual('bateaux école')
    expect(result.current.getVesselTypeName(VesselTypeEnum.SAILING)).toEqual('Navire de plaisance professionnelle')
    expect(result.current.getVesselTypeName(VesselTypeEnum.MOTOR)).toEqual('Navire de services (travaux...)')
    expect(result.current.getVesselTypeName(VesselTypeEnum.FISHING)).toEqual('Navire de pêche professionnelle')
    expect(result.current.getVesselTypeName(VesselTypeEnum.PASSENGER)).toEqual(
      'Navires de passagers (navettes transports...)'
    )
    expect(result.current.getVesselTypeName(VesselTypeEnum.COMMERCIAL)).toEqual('Navire de commerce')
    expect(result.current.getVesselTypeName(VesselTypeEnum.SAILING_LEISURE)).toEqual('Navire de plaisance de loisir')
  })

  it('should return vessel name', () => {
    const { result } = renderHook(() => useVessel())
    expect(result.current.getVesselName()).toEqual('')
    expect(result.current.getVesselName('UNKNOWN')).toEqual('Navire inconnu')
    expect(result.current.getVesselName('my beautiful name')).toEqual('my beautiful name')
  })

  it('should return vessel type options', () => {
    const { result } = renderHook(() => useVessel())
    expect(result.current.vesselTypeOptions.length).toEqual(7)
  })

  it('should return vessel size options', () => {
    const { result } = renderHook(() => useVessel())
    expect(result.current.vesselSizeOptions.length).toEqual(4)
  })

  it('should return all vessel size  name', () => {
    const { result } = renderHook(() => useVessel())
    Object.keys(VesselSizeEnum).forEach(size => {
      expect(result.current.getVesselSize(size as VesselSizeEnum)).toBeDefined()
    })
  })

  it('should return all vessel type  name', () => {
    const { result } = renderHook(() => useVessel())
    Object.keys(VesselTypeEnum).forEach(type => {
      expect(result.current.getVesselType(type as VesselTypeEnum)).toBeDefined()
    })
  })

  describe('getFullVesselName', () => {
    it('should return full vessel name with only name', () => {
      const { result } = renderHook(() => useVessel())

      expect(result.current.getFullVesselName('Poseidon')).toEqual('Poseidon')
    })

    it('should return full vessel name with name and external reference number', () => {
      const { result } = renderHook(() => useVessel())

      expect(result.current.getFullVesselName('Poseidon', undefined, 'REF-123')).toEqual('Poseidon - REF-123')
    })

    it('should return full vessel name with name and flag state', () => {
      const { result } = renderHook(() => useVessel())

      expect(result.current.getFullVesselName('Poseidon', 'FR')).toEqual('Poseidon - FR')
    })

    it('should return full vessel name with all fields', () => {
      const { result } = renderHook(() => useVessel())

      expect(result.current.getFullVesselName('Poseidon', 'FR', 'REF-123')).toEqual('Poseidon - FR - REF-123')
    })

    it('should handle undefined name gracefully (delegated to getVesselName)', () => {
      const { result } = renderHook(() => useVessel())

      // getVesselName(undefined) returns ""
      expect(result.current.getFullVesselName()).toEqual('')
    })
  })
})
