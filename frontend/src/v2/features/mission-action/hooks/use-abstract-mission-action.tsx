import { FieldProps } from 'formik'
import { isEqual, isNull, mapValues, omitBy, pick } from 'lodash'
import { useEffect, useState } from 'react'

interface AbstractControlHook<M> {
  initValue?: M
  handleSubmit: (value?: M) => void
}

export function useAbstractControl<T, M>(
  name: string,
  fieldFormik: FieldProps<T>,
  fromFieldValueToInput: (input: T) => M,
  fromInputToFieldValue: (vaue?: M) => T
): AbstractControlHook<M> {
  const [initValue, setInitValue] = useState<M>()

  useEffect(() => {
    if (!fieldFormik) return
    const b = mapValues(pick(fieldFormik.field.value, 'unitHasConfirmed', 'unitShouldConfirm'), o => !!o)
    const fieldValue = omitBy(
      {
        ...fieldFormik.field.value,
        ...b
      },
      isNull
    )
    setInitValue(fromFieldValueToInput(fieldValue as T))
  }, [fieldFormik]) //, booleans

  const handleSubmit = (value?: M): void => {
    if (isEqual(value, initValue)) return
    setInitValue(value)
    fieldFormik.form.setFieldValue(name, fromInputToFieldValue(value))
  }

  return {
    initValue,
    handleSubmit
  }
}
