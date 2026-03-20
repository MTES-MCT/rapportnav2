import { ObjectSchema } from 'yup'

export interface AbstractFormikHook<T, M> {
  initValue?: M
  beforeInitValue: (value?: T) => M | undefined
  beforeSubmit: (value?: M) => T | undefined
  handleSubmit: (value?: M, onSubmit?: (valueToSubmit?: T) => Promise<unknown>) => Promise<void>
}

export interface AbstractFormikSubFormHook<M> {
  initValue?: M
  validationSchema?: ObjectSchema<any>
  handleSubmit: (value?: M) => Promise<void>
}
