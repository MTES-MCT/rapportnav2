import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { Inquiry } from '../../common/types/inquiry'
import { inquiryValidationSchema } from './inquiry-schema'

export type InquiryInput = {
  dates: [Date?, Date?]
} & Inquiry

export function useInquiryGeneralInformation(
  inquiry: Inquiry,
  onChange: (inquiry: Inquiry) => Promise<unknown>
): AbstractFormikSubFormHook<InquiryInput> {
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: Inquiry): InquiryInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate]
    }
  }

  const fromInputToFieldValue = (value: InquiryInput): Inquiry => {
    const { dates, ...newData } = value
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])

    return {
      ...newData,
      startDateTimeUtc,
      endDateTimeUtc
    }
  }

  const { initValue, handleSubmit } = useAbstractFormik<Inquiry, InquiryInput>(
    inquiry,
    fromFieldValueToInput,
    fromInputToFieldValue,
    ['isSignedByInspector']
  )

  const onSubmit = async (valueToSubmit?: Inquiry) => {
    if (!valueToSubmit) return
    await onChange(valueToSubmit)
  }

  const handleSubmitOverride = async (value?: InquiryInput, errors?: FormikErrors<InquiryInput>) => {
    handleSubmit(value, errors, onSubmit)
  }

  return {
    initValue,
    handleSubmit: handleSubmitOverride,
    validationSchema: inquiryValidationSchema
  }
}
