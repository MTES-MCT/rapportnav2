import { TextInput, TextInputProps } from '@mtes-mct/monitor-ui'
import { FieldProps } from 'formik'
import styled from 'styled-components'
import { useDelayFormik } from '../../hooks/use-delay-formik'

type FormikTextInputDelayProps = {
  name: string
  delay?: number
  fieldFormik: FieldProps<string>
}

export const FormikTextInputDelay = styled(
  ({ delay, fieldFormik, name, ...props }: TextInputProps & FormikTextInputDelayProps) => {
    const { value, onChange } = useDelayFormik(
      fieldFormik.field.value,
      (v?: string | number) => fieldFormik.form.setFieldValue(name, v as string),
      delay
    )
    return <TextInput {...props} name={name} value={value as string} onChange={(v?: string) => onChange(v)} />
  }
)(() => ({}))
