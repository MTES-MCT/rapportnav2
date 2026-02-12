import { Textarea, TextareaProps } from '@mtes-mct/monitor-ui'
import { FieldProps } from 'formik'
import styled from 'styled-components'
import { useDelayFormik } from '../../hooks/use-delay-formik'

type FormikTextareaInputDelayProps = {
  name: string
  delay?: number
  fieldFormik: FieldProps<string>
}

export const FormikTextareaInputDelay = styled(
  ({ delay, fieldFormik, name, ...props }: TextareaProps & FormikTextareaInputDelayProps) => {
    const { value, onChange } = useDelayFormik(
      fieldFormik.field.value,
      (v?: string | number) => fieldFormik.form.setFieldValue(name, v as string),
      delay
    )
    return <Textarea {...props} name={name} value={value as string} onChange={(v?: string) => onChange(v)} />
  }
)(() => ({}))
