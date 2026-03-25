import { FieldProps } from 'formik'
import { AbstractFormikSubFormHook } from '../types/abstract-formik-hook'
import { useAbstractFormik } from './use-abstract-formik-form'

export function useAbstractFormikSubForm<T, M>(
  name: string,
  fieldFormik: FieldProps<T>,
  fromFieldValueToInput: (input: T) => M,
  fromInputToFieldValue: (value: M) => T,
  booleans?: string[]
): AbstractFormikSubFormHook<M> {
  const { initValue, handleSubmit } = useAbstractFormik<T, M>(
    fieldFormik.field.value,
    fromFieldValueToInput,
    fromInputToFieldValue,
    booleans
  )
  const onSubmit = async (valueToSubmit?: T) => await fieldFormik.form.setFieldValue(name, valueToSubmit)
  const handleSubmitOverride = async (value?: M): Promise<void> => {
    await handleSubmit(value, onSubmit)
  }

  return {
    initValue,
    handleSubmit: handleSubmitOverride
  }
}
