import { FieldProps, FormikErrors } from 'formik'
import { isEmpty } from 'lodash'
import { AbstractFormikSubFormHook } from '../types/abstract-formik-hook'
import { useAbstractFormik } from './use-abstract-formik-form'

export function useAbstractFormikSubForm<T, M>(
  name: string,
  fieldFormik: FieldProps<T>,
  fromFieldValueToInput: (input: T) => M,
  fromInputToFieldValue: (value: M) => T,
  booleans?: string[]
): AbstractFormikSubFormHook<M> {
  const { errors, initValue, handleSubmit } = useAbstractFormik<T, M>(
    fieldFormik.field.value,
    fromFieldValueToInput,
    fromInputToFieldValue,
    booleans
  )
  const onSubmit = async (valueToSubmit?: T) => await fieldFormik.form.setFieldValue(name, valueToSubmit)
  const handleSubmitOverride = async (value?: M, errors?: FormikErrors<M>): Promise<void> => {
    if (!isEmpty(errors)) {
      fieldFormik.form.setErrors({ ...fieldFormik.form.errors, ...errors })
    }
    await handleSubmit(value, errors, onSubmit)
  }

  return {
    errors,
    initValue,
    handleSubmit: handleSubmitOverride
  }
}
