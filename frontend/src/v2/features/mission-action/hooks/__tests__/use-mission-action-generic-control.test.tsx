import { beforeEach, describe, expect, it, vi } from 'vitest'
import { renderHook } from '../../../../../test-utils.tsx'
import { MissionAction, MissionNavActionData } from '../../../common/types/mission-action'
import { ActionControlInput } from '../../types/action-type'
import { useMissionActionGenericControl } from '../use-mission-action-generic-control'

vi.mock('../../../common/hooks/use-date', () => ({
  useDate: () => ({
    getDateRangeForInput: () => [],
    getDateRangeFromInput: () => ({ startDateTimeUtc: 'start', endDateTimeUtc: 'end' })
  })
}))
vi.mock('../../../common/hooks/use-coordinate', () => ({
  useCoordinate: () => ({ getCoordRounded: () => [] })
}))
vi.mock('../../../common/hooks/use-mission-finished.tsx', () => ({ useMissionFinished: () => false }))
vi.mock('../../../common/hooks/use-mission-dates.tsx', () => ({ useMissionDates: () => ({}) }))

// capture the mapper the hook gives to useAbstractFormik
let fromInputToFieldValue: (value: ActionControlInput) => MissionNavActionData
vi.mock('../../../common/hooks/use-abstract-formik-form', () => ({
  useAbstractFormik: (_data: unknown, _from: unknown, fromInput: typeof fromInputToFieldValue) => {
    fromInputToFieldValue = fromInput
    return { initValue: undefined, handleSubmit: vi.fn() }
  }
}))

const action = { id: 'a', ownerId: 'mission-id', data: {} } as unknown as MissionAction
const input = { observations: 'obs', dates: [], geoCoords: [48, -3] } as unknown as ActionControlInput

describe('useMissionActionGenericControl transformFieldValue', () => {
  beforeEach(() => vi.clearAllMocks())

  it('should return the plain mapped data when no transform is given', () => {
    renderHook(() => useMissionActionGenericControl(action, vi.fn()))

    expect(fromInputToFieldValue(input)).toEqual({
      observations: 'obs',
      latitude: 48,
      longitude: -3,
      startDateTimeUtc: 'start',
      endDateTimeUtc: 'end'
    })
  })

  it('should apply the transform to the mapped data', () => {
    const transform = vi.fn((data: MissionNavActionData) => ({ ...data, observations: 'changed' }))
    renderHook(() => useMissionActionGenericControl(action, vi.fn(), undefined, false, undefined, transform))

    const result = fromInputToFieldValue(input)

    expect(transform).toHaveBeenCalledWith(
      expect.objectContaining({ observations: 'obs', latitude: 48, startDateTimeUtc: 'start' })
    )
    expect(result).toMatchObject({ observations: 'changed', latitude: 48, endDateTimeUtc: 'end' })
  })
})
