import { describe, it, expect, vi, beforeEach } from 'vitest'
import { FieldProps, FormikErrors } from 'formik'
import { ControlType } from '@common/types/control-types'
import { Control } from '../../../common/types/target-types'
import { useEnvControl, ControlEnvInput } from '../use-control-env.tsx'
import { useAbstractControl } from '../use-abstract-control'
import { useControlRegistry } from '../use-control-registry'

// Mock the dependencies
vi.mock('../use-abstract-control', () => ({
  useAbstractControl: vi.fn()
}))

vi.mock('../use-control-registry', () => ({
  useControlRegistry: vi.fn()
}))

describe('useEnvControl', () => {
  const mockGetControlType = vi.fn()
  const mockHandleSubmit = vi.fn()

  const mockControlValue: Control = {
    hasBeenDone: true,
    infractions: [],
    observations: 'Test observation',
    amountOfControls: 5
  }

  const mockFieldFormik: FieldProps<Control> = {
    field: {
      name: 'control',
      value: mockControlValue
    },
    form: {
      values: { control: mockControlValue },
      initialValues: { control: mockControlValue }
    }
  }

  beforeEach(() => {
    vi.clearAllMocks()

    // Setup default mock implementations
    vi.mocked(useControlRegistry).mockReturnValue({
      getRadios: vi.fn(),
      getEmptyValues: vi.fn(),
      getControlType: mockGetControlType,
      getControlMethod: vi.fn(),
      getControlResultOptions: vi.fn(),
      getDisabledControlTypes: vi.fn(),
      controlTypeOptions: [],
      controlResultOptions: [],
      controlResultOptionsExtra: []
    })

    vi.mocked(useAbstractControl).mockReturnValue({
      initValue: mockControlValue as ControlEnvInput,
      handleSubmit: mockHandleSubmit,
      errors: {}
    })

    mockGetControlType.mockReturnValue('Test Control Type')
  })

  describe('basic functionality', () => {
    it('should call useControlRegistry', () => {
      useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      expect(useControlRegistry).toHaveBeenCalledTimes(1)
    })

    it('should call useAbstractControl with correct parameters', () => {
      useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      expect(useAbstractControl).toHaveBeenCalledWith(
        'testControl',
        mockFieldFormik,
        expect.any(Function),
        expect.any(Function)
      )
    })
  })

  describe('return values from dependencies', () => {
    it('should return errors from useAbstractControl', () => {
      const mockErrors: FormikErrors<ControlEnvInput> = {
        amountOfControls: 'Too many controls'
      }

      vi.mocked(useAbstractControl).mockReturnValue({
        initValue: mockControlValue as ControlEnvInput,
        handleSubmit: mockHandleSubmit,
        errors: mockErrors
      })

      const result = useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      expect(result.errors).toBe(mockErrors)
      expect(result.errors).toEqual({ amountOfControls: 'Too many controls' })
    })

    it('should return empty errors object when no errors', () => {
      vi.mocked(useAbstractControl).mockReturnValue({
        initValue: mockControlValue as ControlEnvInput,
        handleSubmit: mockHandleSubmit,
        errors: {}
      })

      const result = useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      expect(result.errors).toEqual({})
    })

    it('should return initValue from useAbstractControl', () => {
      const mockInitValue: ControlEnvInput = {
        hasBeenDone: true,
        infractions: [{ id: '1' }],
        observations: 'Init value',
        amountOfControls: 10
      }

      vi.mocked(useAbstractControl).mockReturnValue({
        initValue: mockInitValue,
        handleSubmit: mockHandleSubmit,
        errors: {}
      })

      const result = useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      expect(result.initValue).toBe(mockInitValue)
    })

    it('should return handleSubmit from useAbstractControl', () => {
      const result = useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      expect(result.handleSubmit).toBe(mockHandleSubmit)
    })

    it('should call handleSubmit when invoked', async () => {
      mockHandleSubmit.mockResolvedValue(undefined)

      const result = useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      const valueToSubmit: ControlEnvInput = {
        hasBeenDone: true,
        amountOfControls: 3
      }

      await result.handleSubmit(valueToSubmit)

      expect(mockHandleSubmit).toHaveBeenCalledWith(valueToSubmit)
      expect(mockHandleSubmit).toHaveBeenCalledTimes(1)
    })
  })

  describe('getControlType integration', () => {
    it('should call getControlType with the control type', () => {
      useEnvControl('testControl', mockFieldFormik, ControlType.SECURITY)

      expect(mockGetControlType).toHaveBeenCalledWith(ControlType.SECURITY)
    })

    it('should return controlTypeLabel from getControlType', () => {
      mockGetControlType.mockReturnValue('Security Control Label')

      const result = useEnvControl('testControl', mockFieldFormik, ControlType.SECURITY)

      expect(result.controlTypeLabel).toBe('Security Control Label')
    })

    it('should handle different control types', () => {
      mockGetControlType.mockReturnValue('Administrative Control')

      const result = useEnvControl('testControl', mockFieldFormik, ControlType.ADMINISTRATIVE)

      expect(mockGetControlType).toHaveBeenCalledWith(ControlType.ADMINISTRATIVE)
      expect(result.controlTypeLabel).toBe('Administrative Control')
    })
  })

  describe('transformation functions', () => {
    it('should use identity function for fromFieldValueToInput', () => {
      useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      const fromFieldValueToInput = vi.mocked(useAbstractControl).mock.calls[0][2]
      const input: Control = {
        hasBeenDone: true,
        infractions: [],
        amountOfControls: 7
      }
      const result = fromFieldValueToInput(input)

      expect(result).toBe(input)
      expect(result).toEqual(input)
    })

    it('should use identity function for fromInputToFieldValue', () => {
      useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      const fromInputToFieldValue = vi.mocked(useAbstractControl).mock.calls[0][3]
      const input: ControlEnvInput = {
        hasBeenDone: false,
        observations: 'Test',
        amountOfControls: 3
      }
      const result = fromInputToFieldValue(input)

      expect(result).toBe(input)
      expect(result).toEqual(input)
    })

    it('should handle undefined value in fromInputToFieldValue', () => {
      useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      const fromInputToFieldValue = vi.mocked(useAbstractControl).mock.calls[0][3]
      const result = fromInputToFieldValue(undefined)

      expect(result).toBeUndefined()
    })

    it('should preserve all properties through transformations', () => {
      useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      const fromInputToFieldValue = vi.mocked(useAbstractControl).mock.calls[0][3]
      const input: ControlEnvInput = {
        hasBeenDone: true,
        infractions: [{ id: '1' }, { id: '2' }],
        observations: 'Multiple infractions',
        amountOfControls: 15,
        customField: 'custom' as any
      }
      const result = fromInputToFieldValue(input)

      expect(result).toEqual(input)
    })
  })

  describe('validation schema', () => {
    it('should create validation schema with maxControl parameter', async () => {
      const result = useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION, 10)

      expect(result.validationSchema).toBeDefined()

      // Test valid case
      const validData = { amountOfControls: 5 }
      await expect(result.validationSchema.validate(validData)).resolves.toEqual(validData)

      // Test invalid case (exceeds max)
      const invalidData = { amountOfControls: 15 }
      await expect(result.validationSchema.validate(invalidData)).rejects.toThrow()
    })

    it('should use maxControl value in validation schema', async () => {
      const result = useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION, 20)

      const validData = { amountOfControls: 20 }
      await expect(result.validationSchema.validate(validData)).resolves.toEqual(validData)

      const invalidData = { amountOfControls: 21 }
      await expect(result.validationSchema.validate(invalidData)).rejects.toThrow()
    })

    it('should default to max of 1 when maxControl is undefined', async () => {
      const result = useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION, undefined)

      const validData = { amountOfControls: 1 }
      await expect(result.validationSchema.validate(validData)).resolves.toEqual(validData)

      const invalidData = { amountOfControls: 2 }
      await expect(result.validationSchema.validate(invalidData)).rejects.toThrow()
    })

    it('should default to max of 1 when maxControl is not provided', async () => {
      const result = useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      const validData = { amountOfControls: 1 }
      await expect(result.validationSchema.validate(validData)).resolves.toEqual(validData)

      const invalidData = { amountOfControls: 2 }
      await expect(result.validationSchema.validate(invalidData)).rejects.toThrow()
    })

    it('should accept 0 as valid amountOfControls', async () => {
      const result = useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION, 5)

      const validData = { amountOfControls: 0 }
      await expect(result.validationSchema.validate(validData)).resolves.toEqual(validData)
    })

    it('should work with maxControl of 0', async () => {
      const result = useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION, 0)

      const validData = { amountOfControls: 0 }
      await expect(result.validationSchema.validate(validData)).resolves.toEqual(validData)

      const invalidData = { amountOfControls: 1 }
      await expect(result.validationSchema.validate(invalidData)).rejects.toThrow()
    })

    it('should allow undefined amountOfControls', async () => {
      const result = useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION, 10)

      const validData = { amountOfControls: undefined }
      await expect(result.validationSchema.validate(validData)).resolves.toEqual(validData)
    })
  })

  describe('return value structure', () => {
    it('should return all required properties', () => {
      const result = useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION, 10)

      expect(result).toHaveProperty('errors')
      expect(result).toHaveProperty('initValue')
      expect(result).toHaveProperty('handleSubmit')
      expect(result).toHaveProperty('validationSchema')
      expect(result).toHaveProperty('controlTypeLabel')
      expect(Object.keys(result)).toHaveLength(5)
    })

    it('should have correct types for all properties', () => {
      mockGetControlType.mockReturnValue('Control Label')

      const result = useEnvControl('testControl', mockFieldFormik, ControlType.ADMINISTRATIVE, 5)

      expect(typeof result.errors).toBe('object')
      expect(typeof result.handleSubmit).toBe('function')
      expect(typeof result.validationSchema).toBe('object')
      expect(typeof result.controlTypeLabel).toBe('string')
    })
  })

  describe('all control types', () => {
    it.each([ControlType.NAVIGATION, ControlType.ADMINISTRATIVE, ControlType.GENS_DE_MER, ControlType.SECURITY])(
      'should handle %s control type',
      controlType => {
        mockGetControlType.mockReturnValue(`Label for ${controlType}`)

        const result = useEnvControl('testControl', mockFieldFormik, controlType, 5)

        expect(mockGetControlType).toHaveBeenCalledWith(controlType)
        expect(result.controlTypeLabel).toBe(`Label for ${controlType}`)
      }
    )
  })

  describe('edge cases', () => {
    it('should handle null observations', () => {
      useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      const fromInputToFieldValue = vi.mocked(useAbstractControl).mock.calls[0][3]
      const input: ControlEnvInput = {
        hasBeenDone: true,
        observations: null as any,
        amountOfControls: 1
      }
      const result = fromInputToFieldValue(input)

      expect(result.observations).toBeNull()
    })

    it('should handle empty infractions array', () => {
      useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION)

      const fromInputToFieldValue = vi.mocked(useAbstractControl).mock.calls[0][3]
      const input: ControlEnvInput = {
        hasBeenDone: true,
        infractions: [],
        amountOfControls: 0
      }
      const result = fromInputToFieldValue(input)

      expect(result.infractions).toEqual([])
    })

    it('should handle negative maxControl values in validation', async () => {
      const result = useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION, -5)

      // Negative max means nothing is valid above -5
      const invalidData = { amountOfControls: 0 }
      await expect(result.validationSchema.validate(invalidData)).rejects.toThrow()
    })

    it('should handle decimal maxControl values', async () => {
      const result = useEnvControl('testControl', mockFieldFormik, ControlType.NAVIGATION, 5.5)

      const validData = { amountOfControls: 5 }
      await expect(result.validationSchema.validate(validData)).resolves.toEqual(validData)

      const invalidData = { amountOfControls: 6 }
      await expect(result.validationSchema.validate(invalidData)).rejects.toThrow()
    })
  })
})
