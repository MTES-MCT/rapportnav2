import { renderHook } from '@testing-library/react'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { useMissionFinished } from '../../../common/hooks/use-mission-finished'
import { MissionAction, MissionNavActionData } from '../../../common/types/mission-action'
import { SectorFishingType } from '../../../common/types/sector-types'
import { useMissionActionControlSectorFishing } from '../use-mission-action-control-sector-fishing'
import { useMissionActionGenericControl } from '../use-mission-action-generic-control'

vi.mock('../../../common/hooks/use-mission-finished', () => ({ useMissionFinished: vi.fn() }))
vi.mock('../use-mission-action-generic-control', () => ({ useMissionActionGenericControl: vi.fn() }))
vi.mock('../../validation-schema/control-sector-fishing', () => ({
  getControlSectorFishingSchema: vi.fn((finished: boolean) => ({ schema: finished }))
}))

const action = { id: 'a', ownerId: 'mission-id' } as unknown as MissionAction

const getTransform = () => {
  renderHook(() => useMissionActionControlSectorFishing(action, vi.fn()))
  return vi.mocked(useMissionActionGenericControl).mock.calls[0][5] as (
    data: MissionNavActionData
  ) => MissionNavActionData
}

describe('useMissionActionControlSectorFishing', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.mocked(useMissionFinished).mockReturnValue(true)
    vi.mocked(useMissionActionGenericControl).mockReturnValue({ initValue: undefined } as never)
  })

  it('should configure the generic control hook for the fishing sector form', () => {
    const onChange = vi.fn()
    const { result } = renderHook(() => useMissionActionControlSectorFishing(action, onChange))

    expect(useMissionFinished).toHaveBeenCalledWith('mission-id')
    expect(useMissionActionGenericControl).toHaveBeenCalledWith(
      action,
      onChange,
      { schema: true },
      false,
      [
        'incidentDuringOperation',
        'isControlDuringSecurityDay',
        'isWithinDepartment',
        'hasDivingDuringOperation',
        'isSeizureSleepingFishingGear',
        'signature',
        'isOutOfUnit'
      ],
      expect.any(Function)
    )
    expect(result.current).toEqual({ initValue: undefined })
  })

  describe('transformFieldValue', () => {
    const company = { id: '13', name: 'FOE', siren: '513691477' }

    const buildData = (overrides: Record<string, unknown> = {}) =>
      ({
        sectorEstablishmentType: SectorFishingType.FISH_AUCTION,
        establishment: company,
        sati: {
          actionId: 'a',
          establishment: { id: 3, type: SectorFishingType.GMS, owner: { id: 7 }, data: { id: '1', name: 'stale' } }
        },
        ...overrides
      }) as unknown as MissionNavActionData

    it('should copy the action establishment into sati.establishment.data', () => {
      const result = getTransform()(buildData())

      expect(result.sati?.establishment.data).toEqual(company)
    })

    it('should use the sector establishment type as the sati establishment type', () => {
      const result = getTransform()(buildData())

      expect(result.sati?.establishment.type).toBe(SectorFishingType.FISH_AUCTION)
    })

    it('should keep the existing type when there is no sector establishment type', () => {
      const result = getTransform()(buildData({ sectorEstablishmentType: undefined }))

      expect(result.sati?.establishment.type).toBe(SectorFishingType.GMS)
    })

    it('should keep the sati establishment own id and its other fields', () => {
      const result = getTransform()(buildData())

      expect(result.sati?.establishment.id).toBe(3)
      expect(result.sati?.establishment.owner).toEqual({ id: 7 })
      expect(result.sati?.actionId).toBe('a')
    })

    it('should clear the company copy when the action has no establishment', () => {
      const result = getTransform()(buildData({ establishment: undefined }))

      expect(result.sati?.establishment.data).toBeUndefined()
    })

    it('should not mutate the given data', () => {
      const data = buildData()
      const copy = structuredClone(data)

      getTransform()(data)

      expect(data).toEqual(copy)
    })

    it('should return the data untouched when there is no sati', () => {
      const data = buildData({ sati: undefined })

      expect(getTransform()(data)).toBe(data)
    })

    it('should return the data untouched when the sati has no establishment', () => {
      const data = buildData({ sati: { actionId: 'a' } })

      expect(getTransform()(data)).toBe(data)
    })
  })
})
