import { FormikTextInputProps } from '@mtes-mct/monitor-ui'
import { Field, FieldProps } from 'formik'
import styled from 'styled-components'
import { FormikTextInputDelay } from './formik-text-input-delay'

export const FormikTextInput = styled(({ name, ...props }: FormikTextInputProps) => (
  <Field name={name}>
    {(field: FieldProps<string>) => (
      <FormikTextInputDelay
        name={name}
        isLight={true}
        isRequired={true}
        fieldFormik={field}
        isErrorMessageHidden={true}
        {...props}
      />
    )}
  </Field>
))({})
