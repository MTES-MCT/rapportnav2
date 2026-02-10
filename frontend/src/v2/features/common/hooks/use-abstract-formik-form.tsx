//import { diff } from 'deep-object-diff'
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

    return () => {
      setErrors(undefined)
      setInitValue(undefined)
    }
  }, [value])

  const beforeSubmit = (value?: M, errors?: FormikErrors<M>): T | undefined => {
    if (!value) return
    if (!isEmpty(errors)) {
      console.log('Submission blocked due to validation errors:', errors)
      return
    }

    if (isEqual(value, initValue)) return
    //if (initValue) console.log('useAbstractFormik', diff(value, initValue))

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
        setInitValue(value)
        setErrors(undefined)
      } catch (error) {
        setErrors(errors)
        throw error
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
