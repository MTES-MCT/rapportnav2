import { ActionTargetTypeEnum } from '@common/types/env-mission-types'
import { FormikTextInput, FormikTextInputProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'

type ControlledPersonProps = {
  actionTarget?: ActionTargetTypeEnum
}

export const MissionInfractionFormikControlledPersonInput = styled(
  ({ actionTarget, ...props }: Omit<FormikTextInputProps, 'label'> & ControlledPersonProps) => (
    <FormikTextInput
      isRequired={true}
      isErrorMessageHidden={true}
      {...props}
      label={`Identité de la personne ${actionTarget === ActionTargetTypeEnum.COMPANY ? 'morale ' : ''}contrôlée`}
    />
  )
)({})
