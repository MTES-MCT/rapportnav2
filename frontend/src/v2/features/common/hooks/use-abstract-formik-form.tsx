import { FormikErrors } from 'formik'
import { isEmpty, isEqual, isNull, mapValues, omitBy, pick } from 'lodash'
import { useEffect, useState } from 'react'
import { AbstractFormikHook } from '../types/abstract-formik-hook'

export function useAbstractFormik<T, M>(
  value: T,
  fromFieldValueToInput: (value: T) => M,
  fromInputToFieldValue: (input: M) => T,
  booleans?: string[]
): AbstractFormikHook<T, M> {
  const [initValue, setInitValue] = useState<M>()
  const [errors, setErrors] = useState<FormikErrors<M> | undefined>(undefined)

  const beforeInitValue = (value?: T): M | undefined => {
    const b = booleans ? mapValues(pick(value, booleans), o => !!o) : {}
    const fieldValue = omitBy(
      {
        ...value,
        ...b
      },
      isNull
    )
    return fromFieldValueToInput(fieldValue as T)
  }

  useEffect(() => {
    const valueToInit = beforeInitValue(value)
    setInitValue(valueToInit)
  }, [value])

  const beforeSubmit = (value?: M, errors?: FormikErrors<M>): T | undefined => {
    if (!value) return

    // Don't submit if there are validation errors
    if (!isEmpty(errors)) {
      console.log('Submission blocked due to validation errors:', errors)
      return
    }

    // Don't submit if values haven't changed
    if (isEqual(value, initValue)) return

    return fromInputToFieldValue(value)
  }

  const handleSubmit = async (
    value?: M,
    errors?: FormikErrors<M>,
    onSubmit?: (valueToSubmit: T) => Promise<unknown>
  ) => {
    const valueToSubmit = beforeSubmit(value, errors)

    if (onSubmit && valueToSubmit) {
      try {
        await onSubmit(valueToSubmit)
        // Update initValue only after successful submission
        setInitValue(value)
        setErrors(undefined)
      } catch (error) {
        setErrors(errors)
        throw error // Re-throw to let Formik handle it
      }
    }
  }

  return {
    errors,
    initValue,
    handleSubmit,
    beforeSubmit,
    beforeInitValue
  }
}
