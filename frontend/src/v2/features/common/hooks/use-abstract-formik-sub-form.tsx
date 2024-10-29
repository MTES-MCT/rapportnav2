import { FieldProps, FormikErrors } from 'formik'
import { AbstractFormikSubFormHook } from '../types/abstract-formik-hook'
import { useAbstractFormik } from './use-abstract-formik-form'

export function useAbstractFormikSubForm<T, M>(
  name: string,
  fieldFormik: FieldProps<T>,
  fromFieldValueToInput: (input: T) => M,
  fromInputToFieldValue: (vaue: M) => T,
  booleans?: string[]
): AbstractFormikSubFormHook<M> {
  const { isError, initValue, handleSubmit } = useAbstractFormik<T, M>(
    fieldFormik.field.value,
    fromFieldValueToInput,
    fromInputToFieldValue,
    booleans
  )
  const onSubmit = async (valueToSubmit?: T) => await fieldFormik.form.setFieldValue(name, valueToSubmit)
  const handleSubmitOverride = async (value?: M, errors?: FormikErrors<M>): Promise<void> => {
    await handleSubmit(value, errors, onSubmit)
  }

  return {
    isError,
    initValue,
    handleSubmit: handleSubmitOverride
  }
}
