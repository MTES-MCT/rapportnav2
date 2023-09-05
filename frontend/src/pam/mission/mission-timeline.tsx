import React from 'react'
import { FlexboxGrid, Stack } from 'rsuite'
import { Accent, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { EnvAction, Mission } from '../mission-types'

interface MissionTimelineProps {
  mission: Mission
  onSelectAction: (action: EnvAction) => void
}

const MissionTimeline: React.FC<MissionTimelineProps> = ({ mission, onSelectAction }) => {
  return (
    <div>
      <FlexboxGrid justify="space-between" align="middle" style={{ height: '100%' }}>
        <FlexboxGrid.Item>
          <Stack direction="column">
            {mission.envActions.map((envAction: EnvAction) => (
              <Stack.Item key={envAction.id} style={{ width: '100%', padding: '1rem' }}>
                <div onClick={() => onSelectAction(envAction)}>Action from Env: {envAction.id}</div>
              </Stack.Item>
            ))}
          </Stack>
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </div>
  )
}

export default MissionTimeline
