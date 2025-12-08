import { describe, it, expect, vi, beforeEach } from 'vitest'
import { FieldProps, FormikErrors } from 'formik'
import { useAbstractControl } from '../use-abstract-control.tsx'
import { useAbstractFormikSubForm } from '../../../common/hooks/use-abstract-formik-sub-form'

// Mock the useAbstractFormikSubForm hook
vi.mock('../../../common/hooks/use-abstract-formik-sub-form', () => ({
  useAbstractFormikSubForm: vi.fn()
}))

describe('useAbstractControl', () => {
  // Type definitions for test
  type FieldValue = { id: number; data: string }
  type InputValue = { formattedId: string; formattedData: string }

  const mockFromFieldValueToInput = (input: FieldValue): InputValue => ({
    formattedId: input.id.toString(),
    formattedData: input.data.toUpperCase()
  })

  const mockFromInputToFieldValue = (value?: InputValue): FieldValue => ({
    id: value ? parseInt(value.formattedId) : 0,
    data: value ? value.formattedData.toLowerCase() : ''
  })

  const mockFieldFormik: FieldProps<FieldValue> = {
    field: {
      name: 'testField',
      value: { id: 1, data: 'test' }
    },
    form: {
      values: { testField: { id: 1, data: 'test' } },
      errors: {},
      initialValues: { testField: { id: 1, data: 'test' } }
    }
  }

  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('should call useAbstractFormikSubForm with correct parameters', () => {
    const mockErrors: FormikErrors<InputValue> = {}
    const mockInitValue: InputValue = { formattedId: '1', formattedData: 'TEST' }
    const mockHandleSubmit = vi.fn()

    vi.mocked(useAbstractFormikSubForm).mockReturnValue({
      errors: mockErrors,
      initValue: mockInitValue,
      handleSubmit: mockHandleSubmit
    })

    useAbstractControl<FieldValue, InputValue>(
      'testControl',
      mockFieldFormik,
      mockFromFieldValueToInput,
      mockFromInputToFieldValue
    )

    expect(useAbstractFormikSubForm).toHaveBeenCalledWith(
      'testControl',
      mockFieldFormik,
      mockFromFieldValueToInput,
      mockFromInputToFieldValue
    )
    expect(useAbstractFormikSubForm).toHaveBeenCalledTimes(1)
  })

  it('should return errors from useAbstractFormikSubForm', () => {
    const mockErrors: FormikErrors<InputValue> = {
      formattedId: 'Invalid ID',
      formattedData: 'Data is required'
    }
    const mockInitValue: InputValue = { formattedId: '1', formattedData: 'TEST' }
    const mockHandleSubmit = vi.fn()

    vi.mocked(useAbstractFormikSubForm).mockReturnValue({
      errors: mockErrors,
      initValue: mockInitValue,
      handleSubmit: mockHandleSubmit
    })

    const result = useAbstractControl<FieldValue, InputValue>(
      'testControl',
      mockFieldFormik,
      mockFromFieldValueToInput,
      mockFromInputToFieldValue
    )

    expect(result.errors).toBe(mockErrors)
    expect(result.errors).toEqual({
      formattedId: 'Invalid ID',
      formattedData: 'Data is required'
    })
  })

  it('should return empty errors object when no errors exist', () => {
    const mockErrors: FormikErrors<InputValue> = {}
    const mockInitValue: InputValue = { formattedId: '1', formattedData: 'TEST' }
    const mockHandleSubmit = vi.fn()

    vi.mocked(useAbstractFormikSubForm).mockReturnValue({
      errors: mockErrors,
      initValue: mockInitValue,
      handleSubmit: mockHandleSubmit
    })

    const result = useAbstractControl<FieldValue, InputValue>(
      'testControl',
      mockFieldFormik,
      mockFromFieldValueToInput,
      mockFromInputToFieldValue
    )

    expect(result.errors).toEqual({})
  })

  it('should return initValue from useAbstractFormikSubForm', () => {
    const mockErrors: FormikErrors<InputValue> = {}
    const mockInitValue: InputValue = { formattedId: '42', formattedData: 'INITIAL' }
    const mockHandleSubmit = vi.fn()

    vi.mocked(useAbstractFormikSubForm).mockReturnValue({
      errors: mockErrors,
      initValue: mockInitValue,
      handleSubmit: mockHandleSubmit
    })

    const result = useAbstractControl<FieldValue, InputValue>(
      'testControl',
      mockFieldFormik,
      mockFromFieldValueToInput,
      mockFromInputToFieldValue
    )

    expect(result.initValue).toBe(mockInitValue)
    expect(result.initValue).toEqual({ formattedId: '42', formattedData: 'INITIAL' })
  })

  it('should return handleSubmit function from useAbstractFormikSubForm', () => {
    const mockErrors: FormikErrors<InputValue> = {}
    const mockInitValue: InputValue = { formattedId: '1', formattedData: 'TEST' }
    const mockHandleSubmit = vi.fn()

    vi.mocked(useAbstractFormikSubForm).mockReturnValue({
      errors: mockErrors,
      initValue: mockInitValue,
      handleSubmit: mockHandleSubmit
    })

    const result = useAbstractControl<FieldValue, InputValue>(
      'testControl',
      mockFieldFormik,
      mockFromFieldValueToInput,
      mockFromInputToFieldValue
    )

    expect(result.handleSubmit).toBe(mockHandleSubmit)
  })

  it('should call handleSubmit when returned handleSubmit is invoked', async () => {
    const mockErrors: FormikErrors<InputValue> = {}
    const mockInitValue: InputValue = { formattedId: '1', formattedData: 'TEST' }
    const mockHandleSubmit = vi.fn().mockResolvedValue(undefined)

    vi.mocked(useAbstractFormikSubForm).mockReturnValue({
      errors: mockErrors,
      initValue: mockInitValue,
      handleSubmit: mockHandleSubmit
    })

    const result = useAbstractControl<FieldValue, InputValue>(
      'testControl',
      mockFieldFormik,
      mockFromFieldValueToInput,
      mockFromInputToFieldValue
    )

    const valueToSubmit: InputValue = { formattedId: '99', formattedData: 'SUBMITTED' }
    await result.handleSubmit(valueToSubmit)

    expect(mockHandleSubmit).toHaveBeenCalledWith(valueToSubmit)
    expect(mockHandleSubmit).toHaveBeenCalledTimes(1)
  })

  it('should handle different field names correctly', () => {
    const mockErrors: FormikErrors<InputValue> = {}
    const mockInitValue: InputValue = { formattedId: '1', formattedData: 'TEST' }
    const mockHandleSubmit = vi.fn()

    vi.mocked(useAbstractFormikSubForm).mockReturnValue({
      errors: mockErrors,
      initValue: mockInitValue,
      handleSubmit: mockHandleSubmit
    })

    useAbstractControl<FieldValue, InputValue>(
      'differentControlName',
      mockFieldFormik,
      mockFromFieldValueToInput,
      mockFromInputToFieldValue
    )

    expect(useAbstractFormikSubForm).toHaveBeenCalledWith(
      'differentControlName',
      mockFieldFormik,
      mockFromFieldValueToInput,
      mockFromInputToFieldValue
    )
  })

  it('should work with different transformation functions', () => {
    const alternativeFromFieldToInput = (input: FieldValue): InputValue => ({
      formattedId: `ID-${input.id}`,
      formattedData: input.data
    })

    const alternativeFromInputToField = (value?: InputValue): FieldValue => ({
      id: value ? parseInt(value.formattedId.replace('ID-', '')) : -1,
      data: value?.formattedData || 'default'
    })

    const mockErrors: FormikErrors<InputValue> = {}
    const mockInitValue: InputValue = { formattedId: 'ID-1', formattedData: 'test' }
    const mockHandleSubmit = vi.fn()

    vi.mocked(useAbstractFormikSubForm).mockReturnValue({
      errors: mockErrors,
      initValue: mockInitValue,
      handleSubmit: mockHandleSubmit
    })

    useAbstractControl<FieldValue, InputValue>(
      'testControl',
      mockFieldFormik,
      alternativeFromFieldToInput,
      alternativeFromInputToField
    )

    expect(useAbstractFormikSubForm).toHaveBeenCalledWith(
      'testControl',
      mockFieldFormik,
      alternativeFromFieldToInput,
      alternativeFromInputToField
    )
  })

  it('should return all three properties in the correct structure', () => {
    const mockErrors: FormikErrors<InputValue> = { formattedId: 'Error' }
    const mockInitValue: InputValue = { formattedId: '1', formattedData: 'TEST' }
    const mockHandleSubmit = vi.fn()

    vi.mocked(useAbstractFormikSubForm).mockReturnValue({
      errors: mockErrors,
      initValue: mockInitValue,
      handleSubmit: mockHandleSubmit
    })

    const result = useAbstractControl<FieldValue, InputValue>(
      'testControl',
      mockFieldFormik,
      mockFromFieldValueToInput,
      mockFromInputToFieldValue
    )

    expect(result).toHaveProperty('errors')
    expect(result).toHaveProperty('initValue')
    expect(result).toHaveProperty('handleSubmit')
    expect(Object.keys(result)).toHaveLength(3)
  })

  it('should handle undefined initValue', () => {
    const mockErrors: FormikErrors<InputValue> = {}
    const mockInitValue = undefined as any
    const mockHandleSubmit = vi.fn()

    vi.mocked(useAbstractFormikSubForm).mockReturnValue({
      errors: mockErrors,
      initValue: mockInitValue,
      handleSubmit: mockHandleSubmit
    })

    const result = useAbstractControl<FieldValue, InputValue>(
      'testControl',
      mockFieldFormik,
      mockFromFieldValueToInput,
      mockFromInputToFieldValue
    )

    expect(result.initValue).toBeUndefined()
  })

  it('should pass through handleSubmit with errors parameter', async () => {
    const mockErrors: FormikErrors<InputValue> = {}
    const mockInitValue: InputValue = { formattedId: '1', formattedData: 'TEST' }
    const mockHandleSubmit = vi.fn().mockResolvedValue(undefined)

    vi.mocked(useAbstractFormikSubForm).mockReturnValue({
      errors: mockErrors,
      initValue: mockInitValue,
      handleSubmit: mockHandleSubmit
    })

    const result = useAbstractControl<FieldValue, InputValue>(
      'testControl',
      mockFieldFormik,
      mockFromFieldValueToInput,
      mockFromInputToFieldValue
    )

    const valueToSubmit: InputValue = { formattedId: '99', formattedData: 'SUBMITTED' }
    const errorsToSubmit: FormikErrors<InputValue> = { formattedData: 'Validation error' }

    await result.handleSubmit(valueToSubmit, errorsToSubmit)

    expect(mockHandleSubmit).toHaveBeenCalledWith(valueToSubmit, errorsToSubmit)
  })
})
