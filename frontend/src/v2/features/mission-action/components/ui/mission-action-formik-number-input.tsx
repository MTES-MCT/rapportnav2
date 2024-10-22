import { FormikNumberInput, FormikNumberInputProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'

export const MissionActionFormikNumberInput = styled((props: FormikNumberInputProps) => (
  <FormikNumberInput isLight={true} placeholder="0" isRequired={true} isErrorMessageHidden={true} {...props} />
))({})
