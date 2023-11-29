import React from 'react'
import { Divider, FlexboxGrid, Stack } from 'rsuite'
import { THEME } from '@mtes-mct/monitor-ui'
import MissionTimelineItemContainer from './timeline-item-container'
import MissionTimelineItem from './timeline-item'
import { Mission } from '../../../types/mission-types'
import { Action } from '../../../types/action-types'
import { formatShortDate, formatTime } from '../../../dates'
import Text from '../../../ui/text'
import { getColorForStatus } from '../status/utils'
import { ActionTypeEnum } from '../../../types/env-mission-types'
import { groupByDay } from '../utils'

interface MissionTimelineProps {
  mission: Mission
  onSelectAction: (action: Action) => void
}

const MissionTimeline: React.FC<MissionTimelineProps> = ({ mission, onSelectAction }) => {
  return (
    <div>
      <FlexboxGrid justify="space-between" align="middle" style={{ height: '100%' }}>
        <FlexboxGrid.Item style={{ width: '100%' }}>
          <Stack direction="column" spacing={'1rem'} style={{ width: '100%' }} alignItems="stretch">
            {Object.entries(groupByDay(mission.actions, 'startDateTimeUtc')).map(([day, actions], index) => (
              <React.Fragment key={day}>
                {/* Render Divider if it's not the first day */}
                {index !== 0 && (
                  <Stack.Item>
                    <Divider style={{ backgroundColor: THEME.color.charcoal }} />
                  </Stack.Item>
                )}

                <Stack.Item>
                  <Stack direction="column" spacing={'1rem'} style={{ width: '100%' }} alignItems="stretch">
                    {/* Render actions for the day */}
                    {(actions as Action[]).map((action: Action) => {
                      if (!action.data) {
                        return <></>
                      }
                      return (
                        <Stack.Item
                          key={`${action.source}-${action.type}-${action.id}-${Math.random()}`}
                          style={{ width: '100%' }}
                        >
                          <Stack direction="row" spacing={'0.5rem'} style={{ overflow: 'hidden' }}>
                            <Stack.Item style={{ minWidth: '50px' }}>
                              <Stack direction="column" alignItems="flex-start">
                                <Stack.Item>
                                  <Text as="h3" color={THEME.color.slateGray} weight="bold">
                                    {formatShortDate(action.startDateTimeUtc)}
                                  </Text>
                                </Stack.Item>
                                <Stack.Item>
                                  <Text as="h3" color={THEME.color.slateGray} weight="normal">
                                    à {formatTime(action.startDateTimeUtc)}
                                  </Text>
                                </Stack.Item>
                              </Stack>
                            </Stack.Item>
                            <Stack.Item style={{ width: '100%' }}>
                              <MissionTimelineItemContainer
                                actionSource={action.source}
                                actionType={action.type as any}
                              >
                                <MissionTimelineItem action={action} onClick={() => onSelectAction(action)} />
                              </MissionTimelineItemContainer>
                            </Stack.Item>
                            {action.type !== ActionTypeEnum.STATUS && (
                              <Stack.Item alignSelf="stretch" style={{ width: '10px', padding: '5px 0' }}>
                                <div
                                  style={{
                                    height: '100%',
                                    backgroundColor: getColorForStatus(action.status),
                                    borderRadius: '5px'
                                  }}
                                >
                                  &nbsp;
                                </div>
                              </Stack.Item>
                            )}
                          </Stack>
                        </Stack.Item>
                      )
                    })}
                  </Stack>
                </Stack.Item>
              </React.Fragment>
            ))}
          </Stack>
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </div>
  )
}

export default MissionTimeline
