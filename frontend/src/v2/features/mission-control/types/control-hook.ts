import { FormikErrors } from 'formik'

export interface ControlHook<M> {
  initValue?: M
  isError?: boolean
  controlTypeLabel: string | undefined
  radios?: { name: string; label: string; extra?: boolean }[]
  getValidationSchema: (params: any) => any
  handleSubmit: (value?: M, errors?: FormikErrors<M>) => void
}
