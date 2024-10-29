import { FormikErrors } from 'formik'
import { isEmpty, isEqual, isNull, mapValues, omitBy, pick } from 'lodash'
import { useEffect, useState } from 'react'
import { AbstractFormikHook } from '../types/abstract-formik-hook'

export function useAbstractFormik<T, M>(
  value: T,
  fromFieldValueToInput: (input: T) => M,
  fromInputToFieldValue: (value: M) => T,
  booleans?: string[]
): AbstractFormikHook<T, M> {
  const [initValue, setInitValue] = useState<M>()
  const [isError, setIsError] = useState<boolean>(false)

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
    const isError = !isEmpty(errors)
    setIsError(isError)
    if (isError) return
    if (!value) return
    if (isEqual(value, initValue)) return
    return fromInputToFieldValue(value)
  }

  const handleSubmit = async (
    value?: M,
    errors?: FormikErrors<M>,
    onSubmit?: (valueToSubmit?: T) => Promise<unknown>
  ) => {
    const valueToSubmit = beforeSubmit(value, errors)
    setInitValue(value)
    if (onSubmit) await onSubmit(valueToSubmit)
  }

  return {
    isError,
    initValue,
    handleSubmit,
    beforeSubmit,
    beforeInitValue
  }
}
