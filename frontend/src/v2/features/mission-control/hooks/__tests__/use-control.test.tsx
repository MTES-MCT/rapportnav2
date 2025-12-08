import { describe, it, expect, vi, beforeEach } from 'vitest'
import { FieldProps } from 'formik'
import { ControlType } from '@common/types/control-types'
import { Control } from '../../../common/types/target-types'
import { useControl, ControlInput } from '../use-control'
import { useAbstractControl } from '../use-abstract-control'
import { useControlRegistry } from '../use-control-registry'

// Mock the dependencies
vi.mock('../use-abstract-control', () => ({
  useAbstractControl: vi.fn()
}))

vi.mock('../use-control-registry', () => ({
  useControlRegistry: vi.fn()
}))

describe('useControl', () => {
  const mockGetRadios = vi.fn()
  const mockGetEmptyValues = vi.fn()
  const mockGetControlType = vi.fn()
  const mockHandleSubmit = vi.fn()

  const mockControlValue: Control = {
    hasBeenDone: true,
    infractions: [],
    observations: 'Test observation'
  }

  const mockFieldFormik: FieldProps<Control> = {
    field: {
      name: 'control',
      value: mockControlValue
    },
    form: {
      values: { control: mockControlValue },
      errors: {},
      initialValues: { control: mockControlValue }
    }
  }

  beforeEach(() => {
    vi.clearAllMocks()

    // Setup default mock implementations
    vi.mocked(useControlRegistry).mockReturnValue({
      getRadios: mockGetRadios,
      getEmptyValues: mockGetEmptyValues,
      getControlType: mockGetControlType,
      getControlMethod: vi.fn(),
      getControlResultOptions: vi.fn(),
      getDisabledControlTypes: vi.fn(),
      controlTypeOptions: [],
      controlResultOptions: [],
      controlResultOptionsExtra: []
    })

    vi.mocked(useAbstractControl).mockReturnValue({
      initValue: mockControlValue as ControlInput,
      handleSubmit: mockHandleSubmit,
      errors: {}
    })

    mockGetRadios.mockReturnValue([])
    mockGetEmptyValues.mockReturnValue({ infractions: [], observations: undefined })
    mockGetControlType.mockReturnValue('Test Control Type')
  })

  describe('basic functionality', () => {
    it('should call useControlRegistry', () => {
      useControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      expect(useControlRegistry).toHaveBeenCalledTimes(1)
    })

    it('should call useAbstractControl with correct parameters', () => {
      useControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      expect(useAbstractControl).toHaveBeenCalledWith(
        'testControl',
        mockFieldFormik,
        expect.any(Function),
        expect.any(Function)
      )
    })

    it('should return initValue from useAbstractControl', () => {
      const mockInitValue: ControlInput = {
        hasBeenDone: true,
        infractions: [{ id: '1' }],
        observations: 'Init value'
      }

      vi.mocked(useAbstractControl).mockReturnValue({
        initValue: mockInitValue,
        handleSubmit: mockHandleSubmit,
        errors: {}
      })

      const result = useControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      expect(result.initValue).toBe(mockInitValue)
    })

    it('should return handleSubmit from useAbstractControl', () => {
      const result = useControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      expect(result.handleSubmit).toBe(mockHandleSubmit)
    })
  })

  describe('controlType-specific behavior', () => {
    it('should set withRadios to true for GENS_DE_MER control type', () => {
      const result = useControl('testControl', mockFieldFormik, ControlType.GENS_DE_MER)

      expect(result.withRadios).toBe(true)
    })

    it('should set withRadios to true for ADMINISTRATIVE control type', () => {
      const result = useControl('testControl', mockFieldFormik, ControlType.ADMINISTRATIVE)

      expect(result.withRadios).toBe(true)
    })

    it('should set withRadios to false for NAVIGATION control type', () => {
      const result = useControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      expect(result.withRadios).toBe(false)
    })

    it('should set withRadios to false for SECURITY control type', () => {
      const result = useControl('testControl', mockFieldFormik, ControlType.SECURITY)

      expect(result.withRadios).toBe(false)
    })
  })

  describe('getRadios integration', () => {
    it('should call getRadios with the control type', () => {
      useControl('testControl', mockFieldFormik, ControlType.ADMINISTRATIVE)

      expect(mockGetRadios).toHaveBeenCalledWith(ControlType.ADMINISTRATIVE)
    })

    it('should return radios from getRadios', () => {
      const mockRadios = [
        { name: 'radio1', label: 'Radio 1' },
        { name: 'radio2', label: 'Radio 2', extra: true }
      ]
      mockGetRadios.mockReturnValue(mockRadios)

      const result = useControl('testControl', mockFieldFormik, ControlType.ADMINISTRATIVE)

      expect(result.radios).toBe(mockRadios)
      expect(result.radios).toEqual(mockRadios)
    })

    it('should handle empty radios array', () => {
      mockGetRadios.mockReturnValue([])

      const result = useControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      expect(result.radios).toEqual([])
    })
  })

  describe('getControlType integration', () => {
    it('should call getControlType with the control type', () => {
      useControl('testControl', mockFieldFormik, ControlType.SECURITY)

      expect(mockGetControlType).toHaveBeenCalledWith(ControlType.SECURITY)
    })

    it('should return controlTypeLabel from getControlType', () => {
      mockGetControlType.mockReturnValue('Security Control Label')

      const result = useControl('testControl', mockFieldFormik, ControlType.SECURITY)

      expect(result.controlTypeLabel).toBe('Security Control Label')
    })
  })

  describe('transformation functions', () => {
    it('should use identity function for fromFieldValueToInput', () => {
      useControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      const fromFieldValueToInput = vi.mocked(useAbstractControl).mock.calls[0][2]
      const input: Control = { hasBeenDone: true, infractions: [] }
      const result = fromFieldValueToInput(input)

      expect(result).toBe(input)
    })

    describe('fromInputToFieldValue', () => {
      it('should return undefined when value is undefined', () => {
        useControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

        const fromInputToFieldValue = vi.mocked(useAbstractControl).mock.calls[0][3]
        const result = fromInputToFieldValue(undefined)

        expect(result).toBeUndefined()
      })

      it('should return value as-is when hasBeenDone is true', () => {
        mockGetEmptyValues.mockReturnValue({
          infractions: [],
          observations: undefined
        })

        useControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

        const fromInputToFieldValue = vi.mocked(useAbstractControl).mock.calls[0][3]
        const input: ControlInput = {
          hasBeenDone: true,
          infractions: [{ id: '1' }],
          observations: 'Test'
        }
        const result = fromInputToFieldValue(input)

        expect(result).toEqual(input)
        expect(mockGetEmptyValues).not.toHaveBeenCalled()
      })

      it('should merge with empty values when hasBeenDone is false', () => {
        const emptyValues = {
          infractions: [],
          observations: undefined
        }
        mockGetEmptyValues.mockReturnValue(emptyValues)

        useControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

        const fromInputToFieldValue = vi.mocked(useAbstractControl).mock.calls[0][3]
        const input: ControlInput = {
          hasBeenDone: false,
          observations: 'Should be overridden'
        }
        const result = fromInputToFieldValue(input)

        expect(mockGetEmptyValues).toHaveBeenCalledWith(ControlType.NAVIGATION)
        expect(result).toEqual({
          hasBeenDone: false,
          observations: undefined,
          infractions: []
        })
      })

      it('should merge with empty values when hasBeenDone is undefined', () => {
        const emptyValues = {
          infractions: [],
          observations: undefined,
          compliantOperatingPermit: undefined
        }
        mockGetEmptyValues.mockReturnValue(emptyValues)

        useControl('testControl', mockFieldFormik, ControlType.ADMINISTRATIVE)

        const fromInputToFieldValue = vi.mocked(useAbstractControl).mock.calls[0][3]
        const input: ControlInput = {
          observations: 'Test',
          compliantOperatingPermit: true
        }
        const result = fromInputToFieldValue(input)

        expect(mockGetEmptyValues).toHaveBeenCalledWith(ControlType.ADMINISTRATIVE)
        expect(result).toEqual({
          observations: undefined,
          infractions: [],
          compliantOperatingPermit: undefined
        })
      })

      it('should call getEmptyValues with correct control type for GENS_DE_MER', () => {
        const emptyValues = {
          infractions: [],
          observations: undefined,
          staffOutnumbered: undefined
        }
        mockGetEmptyValues.mockReturnValue(emptyValues)

        useControl('testControl', mockFieldFormik, ControlType.GENS_DE_MER)

        const fromInputToFieldValue = vi.mocked(useAbstractControl).mock.calls[0][3]
        const input: ControlInput = { hasBeenDone: false }
        fromInputToFieldValue(input)

        expect(mockGetEmptyValues).toHaveBeenCalledWith(ControlType.GENS_DE_MER)
      })

      it('should preserve other properties when merging with empty values', () => {
        const emptyValues = {
          infractions: [],
          observations: undefined
        }
        mockGetEmptyValues.mockReturnValue(emptyValues)

        useControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

        const fromInputToFieldValue = vi.mocked(useAbstractControl).mock.calls[0][3]
        const input: ControlInput = {
          hasBeenDone: false,
          customProperty: 'should be preserved'
        } as any
        const result = fromInputToFieldValue(input)

        expect(result).toEqual({
          hasBeenDone: false,
          customProperty: 'should be preserved',
          infractions: [],
          observations: undefined
        })
      })
    })
  })

  describe('return value structure', () => {
    it('should return all required properties', () => {
      const result = useControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      expect(result).toHaveProperty('initValue')
      expect(result).toHaveProperty('handleSubmit')
      expect(result).toHaveProperty('radios')
      expect(result).toHaveProperty('controlTypeLabel')
      expect(result).toHaveProperty('withRadios')
      expect(Object.keys(result)).toHaveLength(5)
    })

    it('should have correct types for all properties', () => {
      mockGetRadios.mockReturnValue([{ name: 'test', label: 'Test' }])
      mockGetControlType.mockReturnValue('Control Label')

      const result = useControl('testControl', mockFieldFormik, ControlType.ADMINISTRATIVE)

      expect(typeof result.handleSubmit).toBe('function')
      expect(Array.isArray(result.radios)).toBe(true)
      expect(typeof result.controlTypeLabel).toBe('string')
      expect(typeof result.withRadios).toBe('boolean')
    })
  })

  describe('edge cases', () => {
    it('should handle null/undefined observations', () => {
      useControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      const fromInputToFieldValue = vi.mocked(useAbstractControl).mock.calls[0][3]
      const input: ControlInput = {
        hasBeenDone: true,
        observations: null as any
      }
      const result = fromInputToFieldValue(input)

      expect(result.observations).toBeNull()
    })

    it('should handle empty infractions array', () => {
      useControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      const fromInputToFieldValue = vi.mocked(useAbstractControl).mock.calls[0][3]
      const input: ControlInput = {
        hasBeenDone: true,
        infractions: []
      }
      const result = fromInputToFieldValue(input)

      expect(result.infractions).toEqual([])
    })

    it('should work with different control names', () => {
      useControl('differentName', mockFieldFormik, ControlType.SECURITY)

      expect(useAbstractControl).toHaveBeenCalledWith(
        'differentName',
        mockFieldFormik,
        expect.any(Function),
        expect.any(Function)
      )
    })
  })

  describe('all control types', () => {
    it.each([
      [ControlType.NAVIGATION, false],
      [ControlType.ADMINISTRATIVE, true],
      [ControlType.GENS_DE_MER, true],
      [ControlType.SECURITY, false]
    ])('should handle %s control type with withRadios=%s', (controlType, expectedWithRadios) => {
      const result = useControl('testControl', mockFieldFormik, controlType)

      expect(mockGetRadios).toHaveBeenCalledWith(controlType)
      expect(mockGetControlType).toHaveBeenCalledWith(controlType)
      expect(result.withRadios).toBe(expectedWithRadios)
    })
  })
})
