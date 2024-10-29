import { InfractionTypeEnum } from '@common/types/env-mission-types'
import { Infraction } from '@common/types/infraction-types'
import { FieldProps } from 'formik'
import { useAbstractFormikSubForm } from '../../common/hooks/use-abstract-formik-sub-form'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'

export type InfractionInput = {
  withReport: boolean
} & Infraction

export function useInfractionNavForm(
  name: string,
  fieldFormik: FieldProps<Infraction>
): AbstractFormikSubFormHook<InfractionInput> {
  const fromFieldValueToInput = (infraction: Infraction) => {
    const withReport = infraction?.infractionType === InfractionTypeEnum.WITH_REPORT
    return { ...infraction, withReport }
  }
  const fromInputToFieldValue = (value: InfractionInput) => {
    const { withReport, ...newValue } = value
    const infractionType = withReport ? InfractionTypeEnum.WITH_REPORT : InfractionTypeEnum.WITHOUT_REPORT
    return { ...newValue, infractionType }
  }

  const { initValue, handleSubmit } = useAbstractFormikSubForm<Infraction, InfractionInput>(
    name,
    fieldFormik,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  return {
    initValue,
    handleSubmit
  }
}
