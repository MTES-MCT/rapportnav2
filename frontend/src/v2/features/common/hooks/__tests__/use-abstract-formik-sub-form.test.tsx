import { renderHook } from '@testing-library/react'
import { FieldInputProps, FieldMetaProps, FieldProps, FormikProps } from 'formik'
import { vi } from 'vitest'
import { useAbstractFormikSubForm } from '../use-abstract-formik-sub-form'

type FormValue = {}
type FormInput = {}

const setErrors = vi.fn()
const setFieldValue = vi.fn()

describe('useAbstractFormikSubForm', () => {
  it('should call setFieldValue', () => {
    const fieldProps: FieldProps<any> = {
      field: {} as FieldInputProps<any>,
      meta: {} as FieldMetaProps<any>,
      form: { setErrors, setFieldValue } as unknown as FormikProps<any>
    }
    const { result } = renderHook(() =>
      useAbstractFormikSubForm<FormValue, FormInput>(
        'myField',
        fieldProps,
        () => ({}) as FormValue,
        () => ({}) as FormInput
      )
    )
    result.current.handleSubmit({ test: '' }, {})
    expect(setErrors).not.toHaveBeenCalled()
    expect(setFieldValue).toHaveBeenCalledTimes(1)
  })

  it('should call setErrors and SetField value', () => {
    const fieldProps: FieldProps<any> = {
      field: {} as FieldInputProps<any>,
      meta: {} as FieldMetaProps<any>,
      form: { setErrors, setFieldValue } as unknown as FormikProps<any>
    }
    const { result } = renderHook(() =>
      useAbstractFormikSubForm<FormValue, FormInput>(
        'myField',
        fieldProps,
        () => ({}) as FormValue,
        () => ({}) as FormInput
      )
    )
    result.current.handleSubmit({ test: '' }, { test: 'Failed to be empty' })
    expect(setErrors).toHaveBeenCalledTimes(1)
    expect(setFieldValue).toHaveBeenCalledTimes(2)
  })
})
