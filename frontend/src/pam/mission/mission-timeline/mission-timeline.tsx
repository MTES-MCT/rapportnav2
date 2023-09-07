import React from 'react'
import { FlexboxGrid, Stack } from 'rsuite'
import { Accent, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { EnvAction, Mission } from '../../mission-types'
import MissionTimelineItemContainer from './mission-timeline-item-container'
import MissionTimelineItem from './mission-timeline-item'

interface MissionTimelineProps {
  mission: Mission
  onSelectAction: (action: EnvAction) => void
}

const MissionTimeline: React.FC<MissionTimelineProps> = ({ mission, onSelectAction }) => {
  return (
    <div>
      <FlexboxGrid justify="space-between" align="middle" style={{ height: '100%' }}>
        <FlexboxGrid.Item style={{ width: '100%' }}>
          <Stack direction="column">
            {mission.envActions.map((action: EnvAction) => (
              <Stack.Item key={action.id} style={{ width: '100%', padding: '1rem' }}>
                <MissionTimelineItemContainer actionType={action.actionType}>
                  <MissionTimelineItem action={action} onClick={() => onSelectAction(action)} />
                </MissionTimelineItemContainer>
              </Stack.Item>
            ))}
          </Stack>
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </div>
  )
}

export default MissionTimeline
