import { MultiRadio, Icon, Button, Accent, THEME } from '@mtes-mct/monitor-ui'
import { ActionTypeEnum, missionTypeEnum } from '../../env-mission-types'
import { Stack } from 'rsuite'
import Title from '../../../ui/title'
import { useState } from 'react'
import { ControlTarget } from '../../mission-types'

export const controlTypeRadio = {
  YES: {
    label: missionTypeEnum.SEA.libelle,
    value: missionTypeEnum.SEA.code
  },
  NO: {
    label: missionTypeEnum.LAND.libelle,
    value: missionTypeEnum.LAND.code
  }
}

interface ControlSelectionProps {
  onSelect: (controlType: string, targetType: ControlTarget) => void
}

const ControlSelection: React.FC<ControlSelectionProps> = ({ onSelect }) => {
  const [selectedControlType, setSelectedControlType] = useState<string>(missionTypeEnum.SEA.code)

  return (
    <>
      <Stack direction="column" spacing="1rem" alignItems="flex-start">
        <Stack.Item style={{ width: '100%' }} alignSelf="flex-start">
          <Title as="h2">Ajouter des contrôles</Title>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <MultiRadio
            isInline
            label="Type de contrôle"
            name="controlType"
            options={Object.values(controlTypeRadio)}
            value={selectedControlType}
            onChange={(code: string) => setSelectedControlType(code)}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Button
            onClick={() => onSelect(selectedControlType, ControlTarget.PECHE_PRO)}
            Icon={Icon.Plus}
            accent={Accent.SECONDARY}
            isFullWidth
          >
            Contrôles de navires de <b>pêche professionnelle</b>
          </Button>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Button
            onClick={() => onSelect(selectedControlType, ControlTarget.PLAISANCE_PRO)}
            Icon={Icon.Plus}
            accent={Accent.SECONDARY}
            isFullWidth
          >
            Contrôles de navires de <b>plaisance professionnelle</b>
          </Button>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Button
            onClick={() => onSelect(selectedControlType, ControlTarget.COMMERCE_PRO)}
            Icon={Icon.Plus}
            accent={Accent.SECONDARY}
            isFullWidth
          >
            Contrôles de navires de <b>commerce</b>
          </Button>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Button
            onClick={() => onSelect(selectedControlType, ControlTarget.SERVICE_PRO)}
            Icon={Icon.Plus}
            accent={Accent.SECONDARY}
            isFullWidth
          >
            Contrôles de navires de <b>service (travaux...)</b>
          </Button>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Button
            onClick={() => onSelect(selectedControlType, ControlTarget.PLAISANCE_LOISIR)}
            Icon={Icon.Plus}
            accent={Accent.SECONDARY}
            isFullWidth
          >
            Contrôles de navires de <b>plaisance de loisir</b>
          </Button>
        </Stack.Item>
      </Stack>
    </>
  )
}

export default ControlSelection
