import React from 'react'
import { FlexboxGrid, Stack } from 'rsuite'
import { Accent, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import MissionTimelineItemContainer from './mission-timeline-item-container'
import MissionTimelineItem from './mission-timeline-item'
import { Mission, MissionAction, MissionActions, getActionData, getActionStartTime } from '../../mission-types'
import { formatShortDate, formatTime } from '../../../dates'
import Title from '../../../ui/title'

interface MissionTimelineProps {
  mission: Mission
  onSelectAction: (action: MissionAction) => void
}

const MissionTimeline: React.FC<MissionTimelineProps> = ({ mission, onSelectAction }) => {
  return (
    <div>
      <FlexboxGrid justify="space-between" align="middle" style={{ height: '100%' }}>
        <FlexboxGrid.Item style={{ width: '100%' }}>
          <Stack direction="column">
            {mission.actions.map((action: MissionActions) => {
              const [actionSource, actionData] = getActionData(action)
              console.log('actionData', actionData)
              console.log('actionSource', actionSource)
              return (
                <Stack.Item key={actionData.id} style={{ width: '100%', padding: '1rem 0' }}>
                  <Stack direction="row">
                    <Stack.Item style={{ minWidth: '50px' }}>
                      <Stack direction="column">
                        <Stack.Item>
                          <Title as="h3" color={THEME.color.slateGray} weight="bold">
                            {formatShortDate(getActionStartTime(action))}
                          </Title>
                        </Stack.Item>
                        <Stack.Item>
                          <Title as="h3" color={THEME.color.slateGray}>
                            {formatTime(getActionStartTime(action))}
                          </Title>
                        </Stack.Item>
                      </Stack>
                    </Stack.Item>
                    <Stack.Item style={{ width: '100%' }}>
                      <MissionTimelineItemContainer actionSource={actionSource} actionType={actionData.actionType}>
                        <MissionTimelineItem
                          actionSource={actionSource}
                          action={actionData}
                          onClick={() => onSelectAction(actionData)}
                        />
                      </MissionTimelineItemContainer>
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
              )
            })}
          </Stack>
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </div>
  )
}

export default MissionTimeline
