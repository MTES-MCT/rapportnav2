import { renderHook } from '@testing-library/react'
import { useSector } from '../use-sector'
import { SectorEstablishmentSubType } from '../../types/sati'
import { SectorFishingType, SectorPleasureType } from '../../types/sector-types'

describe('useSector', () => {
  it('should return fishing establishment types', () => {
    const { result } = renderHook(() => useSector())
    const options = result.current.sectorFishingTypeOptions

    expect(options).toContainEqual({ value: SectorFishingType.GMS, label: 'GMS' })
    expect(options).toContainEqual({ value: SectorFishingType.FISH_AUCTION, label: 'Criée / Halle à marée' })
    expect(options).toContainEqual({ value: SectorFishingType.LANDING_SITE, label: 'Site de débarquement' })
    expect(options).toContainEqual({ value: SectorFishingType.RESTAURANT, label: 'Restaurant' })
    expect(options).toContainEqual({ value: SectorFishingType.MOBILE_FISHMONGER, label: 'Poissonnerie ambulante' })
    expect(options).toContainEqual({ value: SectorFishingType.SEDENTARY_FISHMONGER, label: 'Poissonnerie sédentaire' })
    expect(options).toContainEqual({ value: SectorFishingType.FISHMONGER, label: 'Mareyeur' })
    expect(options).toContainEqual({ value: SectorFishingType.OTHERS, label: 'Autre' })
  })

  it('should return all 8 fishing types, without the roadside inspection', () => {
    const { result } = renderHook(() => useSector())
    const options = result.current.sectorFishingTypeOptions

    expect(options).toHaveLength(8)
    expect(options.map(option => option.value as string)).not.toContain('ROADSIDE_INSPECTION')
  })

  it('should return pleasure establishment types', () => {
    const { result } = renderHook(() => useSector())
    const options = result.current.sectorPleasureTypeOptions

    expect(options).toContainEqual({
      value: SectorPleasureType.PLEASURE_MARKET,
      label: 'Marché de la plaisance (vente, location)'
    })
    expect(options).toContainEqual({
      value: SectorPleasureType.SEA_DRIVING_LESSON,
      label: 'Formation à la conduite mer et eaux internes'
    })
    expect(options).toContainEqual({ value: SectorPleasureType.OTHERS, label: 'Autre' })
  })

  it('should return all 3 pleasure types', () => {
    const { result } = renderHook(() => useSector())

    expect(result.current.sectorPleasureTypeOptions).toHaveLength(3)
  })

  it('should return the GMS sub types with a label for each', () => {
    const { result } = renderHook(() => useSector())
    const options = result.current.sectorEstablishmenSubTypeOptions

    expect(options).toContainEqual({
      value: SectorEstablishmentSubType.GMS_FISHING,
      label: 'GMS avec un rayon marée (poissonnerie)'
    })
    expect(options).toContainEqual({
      value: SectorEstablishmentSubType.GMS_TRANSFORMATION,
      label: 'GMS avec transformation sur place exclusivement (mise en barquette)'
    })
    expect(options).toContainEqual({
      value: SectorEstablishmentSubType.GMS_ONLY,
      label: 'Absence de rayon marée (poissonnerie) et sans transformation sur place'
    })
  })

  it('should return exactly the 3 GMS sub types, none without a label', () => {
    const { result } = renderHook(() => useSector())
    const options = result.current.sectorEstablishmenSubTypeOptions

    expect(options).toHaveLength(3)
    expect(options.every(option => typeof option.value === 'string' && !!option.label)).toBe(true)
  })
})
