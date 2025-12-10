import { NumberInput, NumberInputProps } from '@mtes-mct/monitor-ui'
import { FieldProps } from 'formik'
import styled from 'styled-components'
import { useDelayFormik } from '../../hooks/use-delay-formik'

type FormikNumberInputDelayProps = {
  name: string
  delay?: number
  fieldFormik: FieldProps<number>
}

export const FormikNumberInputDelay = styled(
  ({ delay, fieldFormik, name, ...props }: NumberInputProps & FormikNumberInputDelayProps) => {
    const { value, onChange } = useDelayFormik(
      fieldFormik.field.value,
      (v: string | number) => fieldFormik.form.setFieldValue(name, v as number),
      delay
    )
    return <NumberInput {...props} name={name} value={value as number} onChange={(v?: number) => onChange(v)} />
  }
)(() => ({}))
