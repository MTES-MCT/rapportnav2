import { FormikTextareaProps } from '@mtes-mct/monitor-ui'
import { Field, FieldProps } from 'formik'
import styled from 'styled-components'
import { FormikTextareaInputDelay } from './formik-text-area-delay'

export const FormikTextAreaInput = styled(({ name, ...props }: FormikTextareaProps) => (
  <Field name={name}>
    {(field: FieldProps<string>) => (
      <FormikTextareaInputDelay name={name} isLight={true} fieldFormik={field} isErrorMessageHidden={true} {...props} />
    )}
  </Field>
))({})
