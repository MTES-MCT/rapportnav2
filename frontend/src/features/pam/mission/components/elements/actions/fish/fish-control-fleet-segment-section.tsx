import React from 'react'
import { Label, THEME } from '@mtes-mct/monitor-ui'
import { FishAction, FleetSegment } from '../../../../../../common/types/fish-mission-types.ts'
import Text from '../../../../../../common/components/ui/text.tsx'
import { Stack } from 'rsuite'

interface FishControlFleetSegmentSectionProps {
  action: FishAction
}

const FishControlFleetSegmentSection: React.FC<FishControlFleetSegmentSectionProps> = ({ action }) => {
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

export default FishControlFleetSegmentSection
