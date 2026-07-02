import { FormikSelect, FormikSelectProps, Select, SelectProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'

const readOnlyStyle = {
  '&&& [role="combobox"]': {
    border: 'none !important',
    boxShadow: 'none !important'
  },
  '&&& [role="combobox"]:hover': {
    border: 'none !important'
  },
  '&&& [role="combobox"]:focus': {
    border: 'none !important'
  }
}

export const FormikSelectInput = styled(({ name, ...props }: FormikSelectProps) => (
  <FormikSelect name={name} isLight={true} isRequired={true} isErrorMessageHidden={true} {...props} />
))(({ readOnly }) => (readOnly ? readOnlyStyle : {}))

export const SelectInput = styled((props: SelectProps) => (
  <Select isLight={true} isRequired={true} isErrorMessageHidden={true} {...props} />
))(({ readOnly }) => (readOnly ? readOnlyStyle : {}))
