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
      setInitValue(undefined)
    }
  }, [value])

  const beforeSubmit = (value?: M): T | undefined => {
    if (!value) return
    if (isEqual(value, initValue)) return
    return fromInputToFieldValue(value)
  }

  const handleSubmit = async (value?: M, onSubmit?: (valueToSubmit: T) => Promise<unknown>) => {
    const valueToSubmit = beforeSubmit(value)

    if (onSubmit && valueToSubmit) {
      await onSubmit(valueToSubmit)
      setInitValue(value)
    }
  }

  return {
    initValue,
    handleSubmit,
    beforeSubmit,
    beforeInitValue
  }
}
