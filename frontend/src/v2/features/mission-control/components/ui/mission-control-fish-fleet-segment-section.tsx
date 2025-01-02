import Text from '@common/components/ui/text'
import { FleetSegment } from '@common/types/fish-mission-types.ts'
import { Label, THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'
import { MissionFishActionData } from '../../../common/types/mission-action'

interface MissionControlFishFleetSegmentSectionProps {
  action: MissionFishActionData
}

const MissionControlFishFleetSegmentSection: React.FC<MissionControlFishFleetSegmentSectionProps> = ({ action }) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing={'0.2rem'}>
      <Stack.Item>
        <Label>Segment de flotte</Label>
      </Stack.Item>

      <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%', padding: '1rem' }}>
        <Stack direction="column" alignItems="flex-start" spacing={'1rem'}>
          <Stack.Item>
            <Label>Zones de pêche de la marée (issues des FAR)</Label>
            <Text as="h3" weight="bold">
              {action?.faoAreas?.join(' - ')}
            </Text>
          </Stack.Item>
          <Stack.Item>
            <Label>Segment de flotte de la marée</Label>
            <Text as="h3" weight="bold">
              {action?.segments
                ?.map((segment: FleetSegment) => `${segment.segment} - ${segment.segmentName}`)
                .join(' - ')}
            </Text>
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default MissionControlFishFleetSegmentSection
