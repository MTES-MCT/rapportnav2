import { FormikSelect, FormikSelectProps, Select, SelectProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'

const readOnlyStyle = {
  '&&& [role="combobox"]': {
    border: 'none !important',
    boxShadow: 'none !important',
    padding: '0 !important'
  },
  '&&& [role="combobox"]:hover': {
    border: 'none !important'
  },
  '&&& [role="combobox"]:focus': {
    border: 'none !important'
  },
  '&&& .rs-picker-toggle-indicator': {
    display: 'none'
  },
  '&&& .rs-picker-toggle-placeholder': {
    color: 'black !important'
  }
}

export const FormikSelectInput = styled(({ name, ...props }: FormikSelectProps) => (
  <FormikSelect
    name={name}
    isLight={true}
    isRequired={true}
    isErrorMessageHidden={true}
    {...props}
    placeholder={props.readOnly ? '--' : props.placeholder}
  />
))(({ readOnly }) => (readOnly ? readOnlyStyle : {}))

export const SelectInput = styled((props: SelectProps) => (
  <Select
    isLight={true}
    isRequired={true}
    isErrorMessageHidden={true}
    {...props}
    placeholder={props.readOnly ? '--' : props.placeholder}
  />
))(({ readOnly }) => (readOnly ? readOnlyStyle : {}))
