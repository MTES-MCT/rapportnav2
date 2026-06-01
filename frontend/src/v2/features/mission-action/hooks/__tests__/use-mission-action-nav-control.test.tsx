import { VesselSizeEnum } from '@common/types/env-mission-types'
import { renderHook } from '@testing-library/react'
import { vi, describe, it, expect, beforeEach } from 'vitest'
import { MissionAction, MissionNavActionData } from '../../../common/types/mission-action'
import { ActionNavControlInput } from '../../types/action-type'
import { useMissionActionNavControl } from '../use-mission-action-nav-control'

// --- Mocks ---

const mockPreprocessDateForPicker = vi.fn((v?: string) => (v ? new Date(v) : undefined))
const mockPostprocessDateFromPicker = vi.fn((v?: Date) => (v ? v.toISOString() : undefined))
vi.mock('../../../common/hooks/use-date', () => ({
  useDate: () => ({
    preprocessDateForPicker: mockPreprocessDateForPicker,
    postprocessDateFromPicker: mockPostprocessDateFromPicker
  })
}))

const mockGetCoords = vi.fn((lat?: number, lng?: number) => [lat, lng])
vi.mock('../../../common/hooks/use-coordinate', () => ({
  useCoordinate: () => ({
    getCoords: mockGetCoords
  })
}))

let mockIsMissionFinished = false
vi.mock('../../../common/hooks/use-mission-finished.tsx', () => ({
  useMissionFinished: () => mockIsMissionFinished
}))

const mockHandleSubmit = vi.fn()
const mockInitValue = { dates: [new Date(), new Date()], geoCoords: [48.5, -3.2] }
vi.mock('../../../common/hooks/use-abstract-formik-form', () => ({
  useAbstractFormik: (_value: any, _fromField: any, _fromInput: any) => ({
    initValue: mockInitValue,
    handleSubmit: mockHandleSubmit
  })
}))

// --- Test data ---

const mockNavActionData: MissionNavActionData = {
  startDateTimeUtc: '2024-01-01T10:00:00Z',
  endDateTimeUtc: '2024-01-01T12:00:00Z',
  latitude: 48.5,
  longitude: -3.2,
  observations: 'test observation',
  vesselSize: VesselSizeEnum.FROM_12_TO_24m,
  vesselIdentifier: 'ABC123',
  identityControlledPerson: 'John Doe',
  targets: []
}

const mockAction: MissionAction = {
  id: '1234',
  missionId: 'mission-1',
  actionType: 'CONTROL' as any,
  source: 'RAPPORTNAV' as any,
  controlsToComplete: [],
  completenessForStats: {} as any,
  sourcesOfMissingDataForStats: [],
  data: mockNavActionData
}

describe('useMissionActionNavControl', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    mockIsMissionFinished = false
  })

  describe('return values', () => {
    it('should return initValue, validationSchema, and handleSubmit', () => {
      const { result } = renderHook(() => useMissionActionNavControl(mockAction, vi.fn()))
      expect(result.current.initValue).toBeDefined()
      expect(result.current.validationSchema).toBeDefined()
      expect(result.current.handleSubmit).toBeDefined()
    })
  })

  describe('handleSubmit', () => {
    it('should call useAbstractFormik handleSubmit when invoked', async () => {
      const { result } = renderHook(() => useMissionActionNavControl(mockAction, vi.fn()))
      const input = { dates: [new Date(), new Date()] } as unknown as ActionNavControlInput
      await result.current.handleSubmit(input)
      expect(mockHandleSubmit).toHaveBeenCalledWith(input, expect.any(Function))
    })

    it('should not call onChange when submitted value is undefined', async () => {
      const onChange = vi.fn()
      // Extract the onSubmit callback by capturing what handleSubmit is called with
      mockHandleSubmit.mockImplementation(async (value, onSubmit) => {
        if (onSubmit) await onSubmit(undefined)
      })
      const { result } = renderHook(() => useMissionActionNavControl(mockAction, onChange))
      await result.current.handleSubmit({} as ActionNavControlInput)
      expect(onChange).not.toHaveBeenCalled()
    })

    it('should call onChange with merged action data when submitted value is provided', async () => {
      const onChange = vi.fn()
      const submittedData = { observations: 'updated' } as MissionNavActionData
      mockHandleSubmit.mockImplementation(async (value, onSubmit) => {
        if (onSubmit) await onSubmit(submittedData)
      })
      const { result } = renderHook(() => useMissionActionNavControl(mockAction, onChange))
      await result.current.handleSubmit({} as ActionNavControlInput)
      expect(onChange).toHaveBeenCalledWith({ ...mockAction, data: submittedData })
    })
  })

  describe('validationSchema', () => {
    it('should not require vesselSize when mission is not finished', async () => {
      mockIsMissionFinished = false
      const { result } = renderHook(() => useMissionActionNavControl(mockAction, vi.fn()))
      const schema = result.current.validationSchema!

      await expect(schema.validateAt('vesselSize', { vesselSize: null })).resolves.not.toThrow()
    })

    it('should require vesselSize when mission is finished', async () => {
      mockIsMissionFinished = true
      const { result } = renderHook(() => useMissionActionNavControl(mockAction, vi.fn()))
      const schema = result.current.validationSchema!

      await expect(schema.validateAt('vesselSize', { vesselSize: null })).rejects.toThrow()
    })

    it('should not require vesselIdentifier when mission is not finished', async () => {
      mockIsMissionFinished = false
      const { result } = renderHook(() => useMissionActionNavControl(mockAction, vi.fn()))
      const schema = result.current.validationSchema!

      await expect(schema.validateAt('vesselIdentifier', { vesselIdentifier: null })).resolves.not.toThrow()
    })

    it('should require vesselIdentifier when mission is finished', async () => {
      mockIsMissionFinished = true
      const { result } = renderHook(() => useMissionActionNavControl(mockAction, vi.fn()))
      const schema = result.current.validationSchema!

      await expect(schema.validateAt('vesselIdentifier', { vesselIdentifier: null })).rejects.toThrow()
    })

    it('should not require identityControlledPerson when mission is not finished', async () => {
      mockIsMissionFinished = false
      const { result } = renderHook(() => useMissionActionNavControl(mockAction, vi.fn()))
      const schema = result.current.validationSchema!

      await expect(
        schema.validateAt('identityControlledPerson', { identityControlledPerson: null })
      ).resolves.not.toThrow()
    })

    it('should require identityControlledPerson when mission is finished', async () => {
      mockIsMissionFinished = true
      const { result } = renderHook(() => useMissionActionNavControl(mockAction, vi.fn()))
      const schema = result.current.validationSchema!

      await expect(schema.validateAt('identityControlledPerson', { identityControlledPerson: null })).rejects.toThrow()
    })

    it('should accept valid vesselSize enum value when mission is finished', async () => {
      mockIsMissionFinished = true
      const { result } = renderHook(() => useMissionActionNavControl(mockAction, vi.fn()))
      const schema = result.current.validationSchema!

      await expect(
        schema.validateAt('vesselSize', { vesselSize: VesselSizeEnum.FROM_12_TO_24m })
      ).resolves.not.toThrow()
    })
  })
})
