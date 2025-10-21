import Text from '@common/components/ui/text'
import { missionTypeEnum } from '@common/types/env-mission-types'
import { VesselTypeEnum } from '@common/types/mission-types'
import { Label, MultiRadio, THEME } from '@mtes-mct/monitor-ui'
import { FC, useState } from 'react'
import { Stack } from 'rsuite'
import styled from 'styled-components'
import { useVessel } from '../../hooks/use-vessel'
import { ModuleType } from '../../types/module-type'

const StyledItem = styled.div`
  display: flex;
  width: 100%;
  padding: 0.5rem;
  min-height: 55px;
  cursor: pointer;
  background-color: ${THEME.color.gainsboro};

  &:hover {
    background-color: ${THEME.color.blueYonder25};
  }
`

export const controlTypeRadio = [
  {
    value: missionTypeEnum.SEA.code,
    label: missionTypeEnum.SEA.libelle
  },
  {
    value: missionTypeEnum.LAND.code,
    label: missionTypeEnum.LAND.libelle
  }
]

interface ControlSelectionProps {
  moduleType: ModuleType
  onSelect: (controlType: string, targetType: VesselTypeEnum) => void
}

const MissionControlSelection: FC<ControlSelectionProps> = ({ onSelect, moduleType }) => {
  const { getVesselTypeByModule } = useVessel()
  const [selectedControlType, setSelectedControlType] = useState<string>(missionTypeEnum.SEA.code)

  const handleControlType = (code?: string) => {
    if (!code) return
    setSelectedControlType(code)
  }

  return (
    <Stack direction="column" spacing="1.5rem" alignItems="flex-start" style={{ padding: '1rem' }}>
      <Stack.Item>
        <Label>Type de contrôle</Label>
        <MultiRadio
          isInline
          label=""
          name="controlType"
          options={controlTypeRadio}
          value={selectedControlType}
          onChange={code => handleControlType(code)}
        />
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" spacing="1rem" alignItems="flex-start">
          {getVesselTypeByModule(moduleType).map(control => {
            return (
              <Stack.Item style={{ width: '100%' }} key={control.key}>
                <StyledItem onClick={() => onSelect(selectedControlType, control.key)}>
                  <Stack direction="row" spacing="1rem" alignItems="center" style={{ width: '100%' }}>
                    <Stack.Item style={{ width: '80%' }}>
                      <Text as="h3" weight="normal">
                        Contrôles de <b>{control.label.toLowerCase()}</b>
                      </Text>
                    </Stack.Item>
                  </Stack>
                </StyledItem>
              </Stack.Item>
            )
          })}
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default MissionControlSelection

// TODO: remove until we fix image issue  <Stack.Item style={{ width: '15%' }}>{control.icon}</Stack.Item>
