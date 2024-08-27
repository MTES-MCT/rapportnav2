import { Label, MultiRadio, THEME } from '@mtes-mct/monitor-ui'
import { missionTypeEnum } from '../../../../../common/types/env-mission-types.ts'
import { Stack } from 'rsuite'
import Text from '../../../../../common/components/ui/text.tsx'
import { FC, useState } from 'react'
import { VesselTypeEnum } from '../../../../../common/types/mission-types.ts'
import { ControlTarget } from '../../../../../common/types/control-types.ts'
import { vesselTypeToHumanString } from '../../../utils/control-utils.ts'
import IconVesselCommerce from '../../../../../common/components/ui/icon/IconVesselCommerce.tsx'
import IconVesselFishing from '../../../../../common/components/ui/icon/IconVesselFishing.tsx'
import IconVesselSailingPro from '../../../../../common/components/ui/icon/IconVesselSailingPro.tsx'
import IconVesselSailingLeisure from '../../../../../common/components/ui/icon/IconVesselSailingLeisure.tsx'
import IconVesselServices from '../../../../../common/components/ui/icon/IconVesselServices.tsx'
import styled from 'styled-components'

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

const controls: {
  type: VesselTypeEnum
  icon: any
}[] = [
  {
    type: VesselTypeEnum.FISHING,
    icon: IconVesselFishing
  },
  {
    type: VesselTypeEnum.SAILING,
    icon: IconVesselSailingPro
  },
  {
    type: VesselTypeEnum.COMMERCIAL,
    icon: IconVesselCommerce
  },
  {
    type: VesselTypeEnum.MOTOR,
    icon: IconVesselServices
  },
  {
    type: VesselTypeEnum.SAILING_LEISURE,
    icon: IconVesselSailingLeisure
  }
]

interface ControlSelectionProps {
  onSelect: (controlType: string, targetType: ControlTarget) => void
}

const ControlSelection: FC<ControlSelectionProps> = ({ onSelect }) => {
  const [selectedControlType, setSelectedControlType] = useState<string>(missionTypeEnum.SEA.code)

  return (
    <>
      <Stack direction="column" spacing="1.5rem" alignItems="flex-start" style={{ padding: '1rem' }}>
        {/* <Stack.Item style={{ width: '100%' }} alignSelf="flex-start">
          <Text as="h2">Ajouter des contrôles</Text>
        </Stack.Item> */}
        <Stack.Item>
          <Label>Type de contrôle</Label>
          <MultiRadio
            isInline
            name="controlType"
            options={Object.values(controlTypeRadio)}
            value={selectedControlType}
            onChange={(code: string) => setSelectedControlType(code)}
          />
        </Stack.Item>

        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="column" spacing="1rem" alignItems="flex-start">
            {controls.map((control: { type: VesselTypeEnum; icon: any }) => {
              const Icon = control.icon
              return (
                <Stack.Item
                  onClick={() => onSelect(selectedControlType, control.type)}
                  style={{ width: '100%' }}
                  key={control.type}
                >
                  <StyledItem>
                    <Stack direction="row" spacing="1rem" alignItems="center" style={{ width: '100%' }}>
                      <Stack.Item style={{ width: '15%' }}>
                        <Icon />
                      </Stack.Item>
                      <Stack.Item style={{ width: '80%' }}>
                        <Text as="h3" weight="normal">
                          Contrôles de <b>{vesselTypeToHumanString(control.type).toLowerCase()}</b>
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
    </>
  )
}

export default ControlSelection
