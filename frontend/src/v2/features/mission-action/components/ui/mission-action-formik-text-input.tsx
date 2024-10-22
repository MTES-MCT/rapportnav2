import { FormikTextInput, FormikTextInputProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'

export const MissionActionFormikTextInput = styled((props: FormikTextInputProps) => (
  <FormikTextInput isLight={true} isRequired={true} isErrorMessageHidden={true} {...props} />
))({})
