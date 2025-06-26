import { FormikErrors } from 'formik'
import { ObjectSchema } from 'yup'

export interface AbstractFormikHook<T, M> {
  initValue?: M
  isError?: boolean
  errors?: FormikErrors<M>
  beforeInitValue: (value?: T) => M | undefined
  beforeSubmit: (value?: M, errors?: FormikErrors<M>) => T | undefined
  handleSubmit: (
    value?: M,
    errors?: FormikErrors<M>,
    onSubmit?: (valueToSubmit?: T) => Promise<unknown>
  ) => Promise<void>
  hasChanges?: (currentValue?: M) => boolean
}

export interface AbstractFormikSubFormHook<M> {
  initValue?: M
  isError?: boolean
  validationSchema?: ObjectSchema<any>
  handleSubmit: (value?: M, errors?: FormikErrors<M>) => Promise<void>
}
