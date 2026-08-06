import Text from '@common/components/ui/text'
import { Label, Radio, THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'
import { MissionFishActionData } from '../../../common/types/mission-action'

interface FishControlInnSectionProps {
  action: MissionFishActionData
}

const FishControlInnSection: React.FC<FishControlInnSectionProps> = ({ action }) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'} style={{ width: '100%' }}>
      <Stack.Item>
        <Label>Contrôle INN</Label>
      </Stack.Item>

      <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%', padding: '1rem' }}>
        <Stack direction="column" alignItems="flex-start" spacing={'0.5rem'}>
          <Stack direction="row" alignItems="center" spacing={'1.5rem'}>
            <Stack.Item style={{ display: 'flex', alignItems: 'center' }}>
              <Radio
                readOnly
                name="isINNControl"
                labelPosition="right"
                onChange={function noRefCheck() {}}
                checked={action?.isINNControl === true}
              >
                Oui
              </Radio>
            </Stack.Item>
            <Stack.Item style={{ display: 'flex', alignItems: 'center' }}>
              <Radio
                readOnly
                name="isINNControl"
                labelPosition="right"
                onChange={function noRefCheck() {}}
                checked={action?.isINNControl !== true}
              >
                Non
              </Radio>
            </Stack.Item>
          </Stack>
          <Stack.Item>
            <Text as="h3" fontStyle="italic" color={THEME.color.slateGray}>
              Un contrôle est considéré comme "INN" s'il est fait sur un navire tiers ou sans pavillon, dans les eaux
              d'outre-mer ou hors ZEE FR.
            </Text>
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default FishControlInnSection
