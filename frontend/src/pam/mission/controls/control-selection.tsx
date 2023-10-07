import { MultiRadio, Icon, Button, Accent, THEME } from '@mtes-mct/monitor-ui'
import { ActionTypeEnum, missionTypeEnum } from '../../env-mission-types'
import { Stack } from 'rsuite'
import Title from '../../../ui/title'
import { useState } from 'react'
import { ControlTarget, ControlTargetText, VesselType } from '../../mission-types'
import { vesselTypeToHumanString } from './utils'

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
            onClick={() => onSelect(selectedControlType, VesselType.FISHING)}
            Icon={Icon.Plus}
            accent={Accent.SECONDARY}
            isFullWidth
          >
            Contrôles de <b>{vesselTypeToHumanString(VesselType.FISHING)}</b>
          </Button>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Button
            onClick={() => onSelect(selectedControlType, VesselType.SAILING)}
            Icon={Icon.Plus}
            accent={Accent.SECONDARY}
            isFullWidth
          >
            Contrôles de <b>{vesselTypeToHumanString(VesselType.SAILING)}</b>
          </Button>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Button
            onClick={() => onSelect(selectedControlType, VesselType.COMMERCIAL)}
            Icon={Icon.Plus}
            accent={Accent.SECONDARY}
            isFullWidth
          >
            Contrôles de <b>{vesselTypeToHumanString(VesselType.COMMERCIAL)}</b>
          </Button>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Button
            onClick={() => onSelect(selectedControlType, VesselType.MOTOR)}
            Icon={Icon.Plus}
            accent={Accent.SECONDARY}
            isFullWidth
          >
            Contrôles de <b>{vesselTypeToHumanString(VesselType.MOTOR)} (travaux...)</b>
          </Button>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Button
            onClick={() => onSelect(selectedControlType, VesselType.SAILING_LEISURE)}
            Icon={Icon.Plus}
            accent={Accent.SECONDARY}
            isFullWidth
          >
            Contrôles de <b>{vesselTypeToHumanString(VesselType.SAILING_LEISURE)}</b>
          </Button>
        </Stack.Item>
      </Stack>
    </>
  )
}

export default ControlSelection
