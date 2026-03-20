import { renderHook } from '@testing-library/react'
import { FieldInputProps, FieldMetaProps, FieldProps, FormikProps } from 'formik'
import { vi } from 'vitest'
import { useAbstractFormikSubForm } from '../use-abstract-formik-sub-form'

type FormValue = {}
type FormInput = {}

const setFieldValue = vi.fn()

describe('useAbstractFormikSubForm', () => {
  it('should call setFieldValue', () => {
    const fieldProps: FieldProps<any> = {
      field: {} as FieldInputProps<any>,
      meta: {} as FieldMetaProps<any>,
      form: { setFieldValue } as unknown as FormikProps<any>
    }
    const { result } = renderHook(() =>
      useAbstractFormikSubForm<FormValue, FormInput>(
        'myField',
        fieldProps,
        () => ({}) as FormValue,
        () => ({}) as FormInput
      )
    )
    result.current.handleSubmit({ test: '' })
    expect(setFieldValue).toHaveBeenCalledTimes(1)
  })
})
