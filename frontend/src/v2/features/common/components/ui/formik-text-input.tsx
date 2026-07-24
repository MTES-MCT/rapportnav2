import { FormikTextInput, FormikTextInputProps, TextInput, TextInputProps } from '@mtes-mct/monitor-ui'
import { Field, FieldProps } from 'formik'
import styled from 'styled-components'
import { FormikTextInputDelay } from './formik-text-input-delay'

const readOnlyStyle = {
  '& input': {
    border: 'none !important'
  },
  '& input:hover': {
    border: 'none !important'
  },
  '& input:focus': {
    border: 'none !important'
  }
}

export const StyledFormikTextInputDelay = styled(({ name, ...props }: FormikTextInputProps) => (
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
))(({ readOnly }) => (readOnly ? readOnlyStyle : {}))

export const StyledFormikTextInput = styled(({ name, ...props }: TextInputProps) => (
  <FormikTextInput name={name} isLight={true} isRequired={true} isErrorMessageHidden={true} {...props} />
))(({ readOnly }) => (readOnly ? readOnlyStyle : {}))

export const StyledTextInput = styled(({ name, ...props }: TextInputProps) => (
  <TextInput name={name} isLight={true} isRequired={true} isErrorMessageHidden={true} {...props} />
))(({ readOnly }) => (readOnly ? readOnlyStyle : {}))
