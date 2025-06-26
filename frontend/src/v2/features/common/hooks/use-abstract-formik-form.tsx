import { FormikErrors } from 'formik'
import { isEqual, isNull, mapValues, omitBy, pick } from 'lodash'
import { useEffect, useState } from 'react'
import { AbstractFormikHook } from '../types/abstract-formik-hook'

export function useAbstractFormik<T, M>(
  value: T,
  fromFieldValueToInput: (value: T) => M,
  fromInputToFieldValue: (input: M) => T,
  booleans?: string[]
): AbstractFormikHook<T, M> {
  const [initValue, setInitValue] = useState<M>()
  const [isError, setIsError] = useState<boolean>(false)
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

  const beforeSubmit = (value?: M): T | undefined => {
    if (!value) return
    if (isEqual(value, initValue)) return
    return fromInputToFieldValue(value)
  }

  const handleSubmit = async (
    value?: M,
    errors?: FormikErrors<M>,
    onSubmit?: (valueToSubmit: T) => Promise<unknown>
  ) => {
    const valueToSubmit = beforeSubmit(value)

    if (onSubmit && valueToSubmit) {
      try {
        await onSubmit(valueToSubmit)
        // Update initValue only after successful submission
        setInitValue(value)
        setIsError(false)
        setErrors(undefined)
      } catch (error) {
        setIsError(true)
        setErrors(errors)
        throw error // Re-throw to let Formik handle it
      }
    }
  }

  // Helper function to check if form has meaningful changes
  const hasChanges = (currentValue?: M): boolean => {
    if (!currentValue || !initValue) return false
    return !isEqual(currentValue, initValue)
  }

  return {
    isError,
    errors,
    initValue,
    handleSubmit,
    beforeSubmit,
    beforeInitValue,
    hasChanges
  }
}
