import { MultiRadio, Icon, Button, Accent, THEME, Checkbox } from '@mtes-mct/monitor-ui'
import { ActionTypeEnum, missionTypeEnum } from '../../env-mission-types'
import { Stack } from 'rsuite'
import Title from '../../../ui/title'
import { useState } from 'react'
import { ControlTarget, ControlTargetText, ControlType, VesselType } from '../../mission-types'
import { vesselTypeToHumanString } from './utils'

const title = (controlType: ControlType) => {
  switch (controlType) {
    case ControlType.ADMINISTRATIVE:
      return 'Contrôle administratif navire'
    case ControlType.GENS_DE_MER:
      return 'Contrôle administratif gens de mer'
    case ControlType.SECURITY:
      return 'Equipements et respect des normes de sécurité'
    case ControlType.NAVIGATION:
      return 'Respect des règles de navigation'
  }
}

interface ControlTitleCheckboxProps {
  controlType: ControlType
  checked?: boolean
  shouldCompleteControl?: boolean
}

const ControlTitleCheckbox: React.FC<ControlTitleCheckboxProps> = ({ controlType, checked, shouldCompleteControl }) => {
  return (
    <Stack direction="row" alignItems="center">
      <Stack.Item alignSelf="baseline">
        <Checkbox error="" label="" name="control" checked={!!checked} />
      </Stack.Item>
      <Stack.Item>
        <Title as="h3" color={THEME.color.gunMetal} weight="bold">
          {title(controlType)}
        </Title>
      </Stack.Item>
      <Stack.Item>&nbsp;&nbsp;</Stack.Item>
      {!!shouldCompleteControl && (
        <Stack.Item>
          <p style={{ color: THEME.color.maximumRed }}>●</p>
        </Stack.Item>
      )}
    </Stack>
  )
}

export default ControlTitleCheckbox
