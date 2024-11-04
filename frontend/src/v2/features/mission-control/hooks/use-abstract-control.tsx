import { FieldProps } from 'formik'
import { useAbstractFormikSubForm } from '../../common/hooks/use-abstract-formik-sub-form'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'

export function useAbstractControl<T, M>(
  name: string,
  fieldFormik: FieldProps<T>,
  fromFieldValueToInput: (input: T) => M,
  fromInputToFieldValue: (vaue?: M) => T
): AbstractFormikSubFormHook<M> {
  const { initValue, handleSubmit, isError } = useAbstractFormikSubForm<T, M>(
    name,
    fieldFormik,
    fromFieldValueToInput,
    fromInputToFieldValue,
    ['unitHasConfirmed', 'unitShouldConfirm']
  )

  return {
    isError,
    initValue,
    handleSubmit
  }
}
