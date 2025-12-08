import { describe, it, expect, vi, beforeEach } from 'vitest'
import { renderHook } from '@testing-library/react'
import { usePassengerForm } from '../use-passenger-form.tsx'
import { MissionPassenger } from '../../../common/types/passenger-type.ts'

const mockPreprocessDateForPicker = vi.fn((value?: string | null) => (value ? new Date(value) : undefined))
const mockPostprocessDateFromPicker = vi.fn((value?: Date | null) => (value ? value.toISOString() : undefined))

const mockMissionDates = vi.hoisted(() => ({
  startDate: undefined as string | undefined,
  endDate: undefined as string | undefined
}))

const mockIsMissionFinished = vi.hoisted(() => ({ value: false }))

vi.mock('../../../common/hooks/use-date.tsx', () => ({
  useDate: () => ({
    preprocessDateForPicker: mockPreprocessDateForPicker,
    postprocessDateFromPicker: mockPostprocessDateFromPicker
  })
}))

vi.mock('../../../common/hooks/use-mission-dates.tsx', () => ({
  useMissionDates: () => ({ startDateTimeUtc: mockMissionDates.startDate, endDateTimeUtc: mockMissionDates.endDate })
}))

vi.mock('../../../common/hooks/use-mission-finished.tsx', () => ({
  useMissionFinished: () => mockIsMissionFinished.value
}))

describe('usePassengerForm', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    mockMissionDates.startDate = undefined
    mockMissionDates.endDate = undefined
    mockIsMissionFinished.value = false
  })

  describe('EMPTY_PASSENGER', () => {
    it('returns an empty passenger with default values', () => {
      const { result } = renderHook(() => usePassengerForm())

      expect(result.current.EMPTY_PASSENGER).toEqual({
        fullName: '',
        organization: undefined,
        isIntern: false,
        startDate: '',
        endDate: ''
      })
    })
  })

  describe('getInitValue', () => {
    it('returns input with dates array from an existing passenger', () => {
      const { result } = renderHook(() => usePassengerForm())

      const passenger: MissionPassenger = {
        id: '1',
        fullName: 'John Doe',
        organization: 'AFF_MAR',
        isIntern: false,
        startDate: '2024-01-15T00:00:00Z',
        endDate: '2024-01-20T00:00:00Z'
      }

      const initValue = result.current.getInitValue(passenger)

      expect(initValue.fullName).toBe('John Doe')
      expect(initValue.organization).toBe('AFF_MAR')
      expect(initValue.isIntern).toBe(false)
      expect(initValue.dates).toHaveLength(2)
      expect(mockPreprocessDateForPicker).toHaveBeenCalledWith('2024-01-15T00:00:00Z')
      expect(mockPreprocessDateForPicker).toHaveBeenCalledWith('2024-01-20T00:00:00Z')
    })

    it('returns input from EMPTY_PASSENGER when no passenger is provided', () => {
      const { result } = renderHook(() => usePassengerForm())

      const initValue = result.current.getInitValue()

      expect(initValue.fullName).toBe('')
      expect(initValue.organization).toBeUndefined()
      expect(initValue.isIntern).toBe(false)
      expect(initValue.dates).toHaveLength(2)
      expect(mockPreprocessDateForPicker).toHaveBeenCalledWith('')
    })

    it('includes a dates array in the returned input', () => {
      const { result } = renderHook(() => usePassengerForm())

      const initValue = result.current.getInitValue()

      expect(initValue).toHaveProperty('dates')
      expect(initValue.dates).toHaveLength(2)
    })
  })

  describe('fromInputToPassenger', () => {
    it('converts input with dates array back to MissionPassenger', () => {
      const { result } = renderHook(() => usePassengerForm())

      const input = {
        id: '1' as string | undefined,
        fullName: 'Jane Doe' as String,
        organization: 'ESPMER',
        isIntern: true,
        dates: [new Date('2024-02-01T00:00:00Z'), new Date('2024-02-10T00:00:00Z')] as (Date | undefined)[]
      }

      const passenger = result.current.fromInputToPassenger(input)

      expect(passenger.fullName).toBe('Jane Doe')
      expect(passenger.organization).toBe('ESPMER')
      expect(passenger.isIntern).toBe(true)
      expect(passenger).not.toHaveProperty('dates')
      expect(mockPostprocessDateFromPicker).toHaveBeenCalledWith(new Date('2024-02-01T00:00:00Z'))
      expect(mockPostprocessDateFromPicker).toHaveBeenCalledWith(new Date('2024-02-10T00:00:00Z'))
    })

    it('returns empty strings for dates when postprocess returns undefined', () => {
      mockPostprocessDateFromPicker.mockReturnValue(undefined)
      const { result } = renderHook(() => usePassengerForm())

      const input = {
        fullName: 'Test' as String,
        dates: [undefined, undefined] as (Date | undefined)[]
      }

      const passenger = result.current.fromInputToPassenger(input)

      expect(passenger.startDate).toBe('')
      expect(passenger.endDate).toBe('')
    })

    it('removes the dates property from the output', () => {
      const { result } = renderHook(() => usePassengerForm())

      const input = {
        fullName: 'Test' as String,
        organization: 'OTHER',
        dates: [new Date('2024-01-01'), new Date('2024-01-05')] as (Date | undefined)[]
      }

      const passenger = result.current.fromInputToPassenger(input)

      expect('dates' in passenger).toBe(false)
      expect(passenger).toHaveProperty('startDate')
      expect(passenger).toHaveProperty('endDate')
    })
  })

  describe('validationSchema', () => {
    it('returns a yup validation schema', () => {
      const { result } = renderHook(() => usePassengerForm())

      expect(result.current.validationSchema).toBeDefined()
      expect(typeof result.current.validationSchema.isValid).toBe('function')
      expect(typeof result.current.validationSchema.validate).toBe('function')
    })

    it('rejects when fullName is missing', async () => {
      const { result } = renderHook(() => usePassengerForm())

      const isValid = await result.current.validationSchema.isValid({
        organization: 'AFF_MAR',
        dates: [new Date(), new Date()]
      })
      expect(isValid).toBe(false)
    })

    it('rejects when organization is missing', async () => {
      const { result } = renderHook(() => usePassengerForm())

      const isValid = await result.current.validationSchema.isValid({
        fullName: 'John Doe',
        dates: [new Date(), new Date()]
      })
      expect(isValid).toBe(false)
    })

    it('accepts valid complete data', async () => {
      const { result } = renderHook(() => usePassengerForm())

      const isValid = await result.current.validationSchema.isValid({
        fullName: 'John Doe',
        organization: 'AFF_MAR',
        isIntern: false,
        dates: [new Date(), new Date()]
      })
      expect(isValid).toBe(true)
    })

    it('accepts when isIntern is omitted (optional)', async () => {
      const { result } = renderHook(() => usePassengerForm())

      const isValid = await result.current.validationSchema.isValid({
        fullName: 'John Doe',
        organization: 'AFF_MAR',
        dates: [new Date(), new Date()]
      })
      expect(isValid).toBe(true)
    })

    it('rejects when dates are required and mission is finished', async () => {
      mockIsMissionFinished.value = true
      const { result } = renderHook(() => usePassengerForm())

      const isValid = await result.current.validationSchema.isValid({
        fullName: 'John Doe',
        organization: 'AFF_MAR',
        dates: [undefined, undefined]
      })
      expect(isValid).toBe(false)
    })

    it('accepts nullable dates when mission is not finished', async () => {
      mockIsMissionFinished.value = false
      const { result } = renderHook(() => usePassengerForm())

      const isValid = await result.current.validationSchema.isValid({
        fullName: 'John Doe',
        organization: 'AFF_MAR'
      })
      expect(isValid).toBe(true)
    })

    it('is memoized (same reference on re-render)', () => {
      const { result, rerender } = renderHook(() => usePassengerForm())

      const firstSchema = result.current.validationSchema
      rerender()
      const secondSchema = result.current.validationSchema

      expect(firstSchema).toBe(secondSchema)
    })
  })
})
