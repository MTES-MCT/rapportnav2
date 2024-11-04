import { FormikErrors } from 'formik'
import { ObjectSchema } from 'yup'

export interface AbstractFormikHook<T, M> {
  initValue?: M
  isError?: boolean
  beforeInitValue: (value?: T) => M | undefined
  beforeSubmit: (value?: M, errors?: FormikErrors<M>) => T | undefined
  handleSubmit: (
    value?: M,
    errors?: FormikErrors<M>,
    onSubmit?: (valueToSubmit?: T) => Promise<unknown>
  ) => Promise<void>
}

export interface AbstractFormikSubFormHook<M> {
  initValue?: M
  isError?: boolean
  validationSchema?: ObjectSchema<any>
  handleSubmit: (value?: M, errors?: FormikErrors<M>) => Promise<void>
}
