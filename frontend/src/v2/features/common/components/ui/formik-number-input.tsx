import { FormikNumberInputProps } from '@mtes-mct/monitor-ui'
import { Field, FieldProps } from 'formik'
import styled from 'styled-components'
import { FormikNumberInputDelay } from './formik-number-input-delay'

export const FormikNumberInput = styled(({ name, ...props }: FormikNumberInputProps) => (
  <Field name={name}>
    {(field: FieldProps<number>) => (
      <FormikNumberInputDelay
        name={name}
        isLight={true}
        placeholder="0"
        isRequired={true}
        fieldFormik={field}
        isErrorMessageHidden={true}
        {...props}
      />
    )}
  </Field>
))({})
