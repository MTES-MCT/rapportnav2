import { describe, it, expect, vi, beforeEach } from 'vitest'
import { renderHook } from '@testing-library/react'
import { useMissionCrewAbsenceForm, MissionCrewAbsenceInitialInput } from '../use-crew-absence.tsx'
import { MissionCrewAbsence, MissionCrewAbsenceReason } from '../../../common/types/crew-type.ts'
import { FieldProps } from 'formik'

// Capture the transformation functions passed to useAbstractFormikSubForm
let capturedFromFieldValueToInput: ((data: MissionCrewAbsence) => MissionCrewAbsenceInitialInput) | null = null
let capturedFromInputToFieldValue: ((value: MissionCrewAbsenceInitialInput) => MissionCrewAbsence) | null = null

const mockPreprocessDateForPicker = vi.fn((date: Date | undefined) => date)
const mockPostprocessDateFromPicker = vi.fn((date: Date | undefined) => date)
const mockHandleSubmit = vi.fn()

const mockMissionDates = vi.hoisted(() => ({
  startDate: undefined as string | undefined,
  endDate: undefined as string | undefined
}))

// Mock dependencies
vi.mock('../../../common/hooks/use-date.tsx', () => ({
  useDate: () => ({
    preprocessDateForPicker: mockPreprocessDateForPicker,
    postprocessDateFromPicker: mockPostprocessDateFromPicker
  })
}))

vi.mock('../../../common/hooks/use-mission-dates.tsx', () => ({
  useMissionDates: () => [mockMissionDates.startDate, mockMissionDates.endDate]
}))

vi.mock('../../../common/hooks/use-abstract-formik-sub-form.tsx', () => ({
  useAbstractFormikSubForm: (
    _name: string,
    _fieldFormik: FieldProps<MissionCrewAbsence>,
    fromFieldValueToInput: (data: MissionCrewAbsence) => MissionCrewAbsenceInitialInput,
    fromInputToFieldValue: (value: MissionCrewAbsenceInitialInput) => MissionCrewAbsence
  ) => {
    // Capture the transformation functions for testing
    capturedFromFieldValueToInput = fromFieldValueToInput
    capturedFromInputToFieldValue = fromInputToFieldValue
    return {
      initValue: {
        id: 1,
        startDate: new Date('2024-01-15'),
        endDate: new Date('2024-01-17'),
        reason: MissionCrewAbsenceReason.SICK_LEAVE,
        isAbsentFullMission: false,
        dates: [new Date('2024-01-15'), new Date('2024-01-17')]
      },
      handleSubmit: mockHandleSubmit,
      errors: { reason: 'Error message' }
    }
  }
}))

const createFieldFormik = (value: Partial<MissionCrewAbsence> = {}): FieldProps<MissionCrewAbsence> =>
  ({
    field: {
      name: 'crew.0.absences.0',
      value: {
        id: undefined,
        startDate: undefined,
        endDate: undefined,
        reason: undefined,
        isAbsentFullMission: false,
        ...value
      }
    },
    form: {
      setFieldValue: vi.fn(),
      values: {},
      errors: {},
      touched: {}
    },
    meta: {
      value: {},
      touched: false
    }
  }) as unknown as FieldProps<MissionCrewAbsence>

describe('useMissionCrewAbsenceForm', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    capturedFromFieldValueToInput = null
    capturedFromInputToFieldValue = null
    mockMissionDates.startDate = undefined
    mockMissionDates.endDate = undefined
  })

  describe('EMPTY_ABSENCE constant', () => {
    it('returns EMPTY_ABSENCE with all fields undefined except isAbsentFullMission', () => {
      const { result } = renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      expect(result.current.EMPTY_ABSENCE).toEqual({
        id: undefined,
        startDate: undefined,
        endDate: undefined,
        reason: undefined,
        isAbsentFullMission: false
      })
    })

    it('EMPTY_ABSENCE has isAbsentFullMission set to false by default', () => {
      const { result } = renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      expect(result.current.EMPTY_ABSENCE.isAbsentFullMission).toBe(false)
    })
  })

  describe('fromFieldValueToInput transformation', () => {
    it('transforms MissionCrewAbsence to input format with dates array', () => {
      renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      const inputData: MissionCrewAbsence = {
        id: 1,
        startDate: new Date('2024-01-15'),
        endDate: new Date('2024-01-17'),
        reason: MissionCrewAbsenceReason.TRAINING,
        isAbsentFullMission: false
      }

      const result = capturedFromFieldValueToInput!(inputData)

      expect(result.dates).toHaveLength(2)
      expect(result.id).toBe(1)
      expect(result.reason).toBe(MissionCrewAbsenceReason.TRAINING)
      expect(result.isAbsentFullMission).toBe(false)
    })

    it('calls preprocessDateForPicker for startDate', () => {
      renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      const startDate = new Date('2024-01-15')
      const inputData: MissionCrewAbsence = {
        startDate,
        endDate: new Date('2024-01-17')
      }

      capturedFromFieldValueToInput!(inputData)

      expect(mockPreprocessDateForPicker).toHaveBeenCalledWith(startDate)
    })

    it('calls preprocessDateForPicker for endDate', () => {
      renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      const endDate = new Date('2024-01-17')
      const inputData: MissionCrewAbsence = {
        startDate: new Date('2024-01-15'),
        endDate
      }

      capturedFromFieldValueToInput!(inputData)

      expect(mockPreprocessDateForPicker).toHaveBeenCalledWith(endDate)
    })

    it('handles undefined dates correctly', () => {
      renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      const inputData: MissionCrewAbsence = {
        startDate: undefined,
        endDate: undefined,
        reason: MissionCrewAbsenceReason.OTHER
      }

      const result = capturedFromFieldValueToInput!(inputData)

      expect(result.dates).toHaveLength(2)
      expect(mockPreprocessDateForPicker).toHaveBeenCalledWith(undefined)
    })

    it('preserves all original fields in the output', () => {
      renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      const inputData: MissionCrewAbsence = {
        id: 42,
        startDate: new Date('2024-02-01'),
        endDate: new Date('2024-02-05'),
        reason: MissionCrewAbsenceReason.HOLIDAYS,
        isAbsentFullMission: true
      }

      const result = capturedFromFieldValueToInput!(inputData)

      expect(result.id).toBe(42)
      expect(result.reason).toBe(MissionCrewAbsenceReason.HOLIDAYS)
      expect(result.isAbsentFullMission).toBe(true)
    })
  })

  describe('fromInputToFieldValue transformation', () => {
    it('transforms input format back to MissionCrewAbsence', () => {
      renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      const inputValue: MissionCrewAbsenceInitialInput = {
        id: 1,
        dates: [new Date('2024-01-15'), new Date('2024-01-17')],
        reason: MissionCrewAbsenceReason.MEETING,
        isAbsentFullMission: false
      }

      const result = capturedFromInputToFieldValue!(inputValue)

      expect(result.id).toBe(1)
      expect(result.reason).toBe(MissionCrewAbsenceReason.MEETING)
      expect(result.isAbsentFullMission).toBe(false)
      expect(result).not.toHaveProperty('dates')
    })

    it('calls postprocessDateFromPicker for startDate (dates[0])', () => {
      renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      const startDate = new Date('2024-01-15')
      const inputValue: MissionCrewAbsenceInitialInput = {
        dates: [startDate, new Date('2024-01-17')]
      }

      capturedFromInputToFieldValue!(inputValue)

      expect(mockPostprocessDateFromPicker).toHaveBeenCalledWith(startDate)
    })

    it('calls postprocessDateFromPicker for endDate (dates[1])', () => {
      renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      const endDate = new Date('2024-01-17')
      const inputValue: MissionCrewAbsenceInitialInput = {
        dates: [new Date('2024-01-15'), endDate]
      }

      capturedFromInputToFieldValue!(inputValue)

      expect(mockPostprocessDateFromPicker).toHaveBeenCalledWith(endDate)
    })

    it('handles undefined dates in array', () => {
      renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      const inputValue: MissionCrewAbsenceInitialInput = {
        dates: [undefined, undefined],
        reason: MissionCrewAbsenceReason.OTHER
      }

      const result = capturedFromInputToFieldValue!(inputValue)

      expect(mockPostprocessDateFromPicker).toHaveBeenCalledWith(undefined)
      expect(result.reason).toBe(MissionCrewAbsenceReason.OTHER)
    })

    it('removes dates property from output', () => {
      renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      const inputValue: MissionCrewAbsenceInitialInput = {
        dates: [new Date('2024-01-15'), new Date('2024-01-17')],
        id: 1,
        reason: MissionCrewAbsenceReason.SICK_LEAVE
      }

      const result = capturedFromInputToFieldValue!(inputValue)

      expect('dates' in result).toBe(false)
    })
  })

  describe('isAbsentFullMission computation in fromInputToFieldValue', () => {
    it('sets isAbsentFullMission to true when absence dates match mission dates', () => {
      // Use times that stay on the same day across all timezones (avoiding midnight UTC)
      mockMissionDates.startDate = '2024-01-15T10:00:00Z'
      mockMissionDates.endDate = '2024-01-20T18:00:00Z'

      renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik(), 'mission-123'))

      const inputValue: MissionCrewAbsenceInitialInput = {
        dates: [new Date('2024-01-15T14:00:00Z'), new Date('2024-01-20T16:00:00Z')],
        reason: MissionCrewAbsenceReason.SICK_LEAVE
      }

      const result = capturedFromInputToFieldValue!(inputValue)

      expect(result.isAbsentFullMission).toBe(true)
    })

    it('sets isAbsentFullMission to false when start date does not match mission start', () => {
      mockMissionDates.startDate = '2024-01-15T10:00:00Z'
      mockMissionDates.endDate = '2024-01-20T18:00:00Z'

      renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik(), 'mission-123'))

      const inputValue: MissionCrewAbsenceInitialInput = {
        dates: [new Date('2024-01-16T10:00:00Z'), new Date('2024-01-20T16:00:00Z')],
        reason: MissionCrewAbsenceReason.SICK_LEAVE
      }

      const result = capturedFromInputToFieldValue!(inputValue)

      expect(result.isAbsentFullMission).toBe(false)
    })

    it('sets isAbsentFullMission to false when end date does not match mission end', () => {
      mockMissionDates.startDate = '2024-01-15T10:00:00Z'
      mockMissionDates.endDate = '2024-01-20T18:00:00Z'

      renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik(), 'mission-123'))

      const inputValue: MissionCrewAbsenceInitialInput = {
        dates: [new Date('2024-01-15T14:00:00Z'), new Date('2024-01-19T16:00:00Z')],
        reason: MissionCrewAbsenceReason.SICK_LEAVE
      }

      const result = capturedFromInputToFieldValue!(inputValue)

      expect(result.isAbsentFullMission).toBe(false)
    })

    it('sets isAbsentFullMission to false when mission dates are undefined', () => {
      mockMissionDates.startDate = undefined
      mockMissionDates.endDate = undefined

      renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      const inputValue: MissionCrewAbsenceInitialInput = {
        dates: [new Date('2024-01-15T08:00:00Z'), new Date('2024-01-20T18:00:00Z')],
        reason: MissionCrewAbsenceReason.SICK_LEAVE
      }

      const result = capturedFromInputToFieldValue!(inputValue)

      expect(result.isAbsentFullMission).toBe(false)
    })

    it('sets isAbsentFullMission to false when absence dates are undefined', () => {
      mockMissionDates.startDate = '2024-01-15T00:00:00Z'
      mockMissionDates.endDate = '2024-01-20T23:59:59Z'

      renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik(), 'mission-123'))

      const inputValue: MissionCrewAbsenceInitialInput = {
        dates: [undefined, undefined],
        reason: MissionCrewAbsenceReason.SICK_LEAVE
      }

      const result = capturedFromInputToFieldValue!(inputValue)

      expect(result.isAbsentFullMission).toBe(false)
    })

    it('sets isAbsentFullMission to false when only start date is undefined', () => {
      mockMissionDates.startDate = '2024-01-15T00:00:00Z'
      mockMissionDates.endDate = '2024-01-20T23:59:59Z'

      renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik(), 'mission-123'))

      const inputValue: MissionCrewAbsenceInitialInput = {
        dates: [undefined, new Date('2024-01-20T18:00:00Z')],
        reason: MissionCrewAbsenceReason.SICK_LEAVE
      }

      const result = capturedFromInputToFieldValue!(inputValue)

      expect(result.isAbsentFullMission).toBe(false)
    })

    it('sets isAbsentFullMission to false when only end date is undefined', () => {
      mockMissionDates.startDate = '2024-01-15T00:00:00Z'
      mockMissionDates.endDate = '2024-01-20T23:59:59Z'

      renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik(), 'mission-123'))

      const inputValue: MissionCrewAbsenceInitialInput = {
        dates: [new Date('2024-01-15T08:00:00Z'), undefined],
        reason: MissionCrewAbsenceReason.SICK_LEAVE
      }

      const result = capturedFromInputToFieldValue!(inputValue)

      expect(result.isAbsentFullMission).toBe(false)
    })

    it('uses isSameDay comparison (different times on same day should match)', () => {
      mockMissionDates.startDate = '2024-01-15T06:00:00Z'
      mockMissionDates.endDate = '2024-01-20T22:00:00Z'

      renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik(), 'mission-123'))

      const inputValue: MissionCrewAbsenceInitialInput = {
        dates: [new Date('2024-01-15T14:30:00Z'), new Date('2024-01-20T08:15:00Z')],
        reason: MissionCrewAbsenceReason.TRAINING
      }

      const result = capturedFromInputToFieldValue!(inputValue)

      expect(result.isAbsentFullMission).toBe(true)
    })
  })

  describe('useAbstractFormikSubForm integration', () => {
    it('returns initValue from useAbstractFormikSubForm', () => {
      const { result } = renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      expect(result.current.initValue).toBeDefined()
      expect(result.current.initValue?.id).toBe(1)
      expect(result.current.initValue?.reason).toBe(MissionCrewAbsenceReason.SICK_LEAVE)
    })

    it('returns handleSubmit function from useAbstractFormikSubForm', () => {
      const { result } = renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      expect(result.current.handleSubmit).toBe(mockHandleSubmit)
    })

    it('returns errors from useAbstractFormikSubForm', () => {
      const { result } = renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      expect(result.current.errors).toEqual({ reason: 'Error message' })
    })
  })

  describe('validationSchema', () => {
    it('returns a yup validation schema object', () => {
      const { result } = renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      expect(result.current.validationSchema).toBeDefined()
      expect(typeof result.current.validationSchema.isValid).toBe('function')
      expect(typeof result.current.validationSchema.validate).toBe('function')
    })

    it('validates valid absence reason SICK_LEAVE', async () => {
      const { result } = renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))
      const isValid = await result.current.validationSchema.isValid({ reason: MissionCrewAbsenceReason.SICK_LEAVE })
      expect(isValid).toBe(true)
    })

    it('rejects invalid absence reason string', async () => {
      const { result } = renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))
      const isValid = await result.current.validationSchema.isValid({ reason: 'INVALID_REASON' })
      expect(isValid).toBe(false)
    })

    it('rejects invalid absence reason number', async () => {
      const { result } = renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))
      const isValid = await result.current.validationSchema.isValid({ reason: 123 })
      expect(isValid).toBe(false)
    })

    it('accepts isAbsentFullMission as true', async () => {
      const { result } = renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))
      const isValid = await result.current.validationSchema.isValid({
        isAbsentFullMission: true,
        reason: MissionCrewAbsenceReason.SICK_LEAVE
      })
      expect(isValid).toBe(true)
    })

    it('accepts isAbsentFullMission as false', async () => {
      const { result } = renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))
      const isValid = await result.current.validationSchema.isValid({
        isAbsentFullMission: false,
        reason: MissionCrewAbsenceReason.SICK_LEAVE
      })
      expect(isValid).toBe(true)
    })

    it('accepts missing isAbsentFullMission (optional)', async () => {
      const { result } = renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))
      const isValid = await result.current.validationSchema.isValid({ reason: MissionCrewAbsenceReason.SICK_LEAVE })
      expect(isValid).toBe(true)
    })

    it('accepts complete valid absence data', async () => {
      const { result } = renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      const validData = {
        reason: MissionCrewAbsenceReason.SICK_LEAVE,
        isAbsentFullMission: true
      }

      const isValid = await result.current.validationSchema.isValid(validData)
      expect(isValid).toBe(true)
    })

    it('validationSchema is memoized (same reference on re-render)', () => {
      const { result, rerender } = renderHook(() => useMissionCrewAbsenceForm('crew.0.absences.0', createFieldFormik()))

      const firstSchema = result.current.validationSchema
      rerender()
      const secondSchema = result.current.validationSchema

      expect(firstSchema).toBe(secondSchema)
    })
  })
})
