import React from 'react'
import { Divider, FlexboxGrid, Loader, Stack } from 'rsuite'
import { Accent, Icon, IconButton, THEME } from '@mtes-mct/monitor-ui'
import MissionTimelineItemContainer from './item/timeline-item-container.tsx'
import MissionTimelineItem from './item/timeline-item.tsx'
import { Action } from '@common/types/action-types.ts'
import { formatShortDate, formatTime } from '@common/utils/dates-for-humans.ts'
import Text from '../../../../../common/components/ui/text.tsx'
import { getColorForStatus } from '../../../utils/status-utils.ts'
import { ActionTypeEnum } from '@common/types/env-mission-types.ts'
import { groupByDay } from '../../../utils/utils.ts'
import useGetMissionTimeline from '../../../hooks/use-mission-timeline.tsx'
import { find } from 'lodash'
import { CompletenessForStatsStatusEnum } from '@common/types/mission-types.ts'

interface MissionTimelineProps {
  missionId?: string
  onSelectAction: (action: Action) => void
}

const MissionTimeline: React.FC<MissionTimelineProps> = ({ missionId, onSelectAction }) => {
  const { data: mission, loading, error } = useGetMissionTimeline(missionId)

  if (error) {
    return (
      <Stack justifyContent={'center'} alignItems={'center'} style={{ height: '100%' }} data-testid={'timeline-error'}>
        <Stack.Item alignSelf={'center'}>
          <Text as={'h3'} color={THEME.color.maximumRed}>
            Une erreur s'est produite lors du chargement de la timeline.
            <br />
            Si le problème persiste, veuillez contacter l'équipe RapportNav.
          </Text>
          <Text as={'h3'} color={THEME.color.lightGray}>
            Erreur: {error.message}
          </Text>
        </Stack.Item>
      </Stack>
    )
  }

  if (loading) {
    return (
      <Stack
        justifyContent={'center'}
        alignItems={'center'}
        style={{ paddingTop: '5rem' }}
        data-testid={'timeline-loading'}
      >
        <Stack.Item alignSelf={'center'}>
          <Loader center={true} size={'md'} vertical={true} />
        </Stack.Item>
      </Stack>
    )
  }

  if (mission && !mission.actions?.length) {
    return (
      <Stack justifyContent={'center'} alignItems={'center'} style={{ height: '100%' }}>
        <Stack.Item alignSelf={'center'}>
          <Text as={'h3'}>Aucune action n'est ajoutée pour le moment</Text>
        </Stack.Item>
      </Stack>
    )
  }

  if (mission) {
    return (
      <div data-testid={'timeline-content'}>
        <FlexboxGrid justify="space-between" align="middle" style={{ height: '100%' }}>
          <FlexboxGrid.Item style={{ width: '100%' }}>
            <Stack direction="column" spacing={'1rem'} style={{ width: '100%' }} alignItems="stretch">
              {Object.entries(groupByDay(mission.actions, 'startDateTimeUtc')).map(([day, actions], index) => (
                <React.Fragment key={day}>
                  {/* Render Divider if it's not the first day */}
                  {index !== 0 && (
                    <Stack.Item data-testid={'timeline-day-divider'}>
                      <Divider style={{ backgroundColor: THEME.color.charcoal }} />
                      <div style={{ padding: '0.25rem' }} />
                    </Stack.Item>
                  )}

                  <Stack.Item style={{ marginRight: '1rem' }}>
                    <Stack direction="column" spacing={'0.75rem'} style={{ width: '100%' }} alignItems="stretch">
                      {/* Render actions for the day */}
                      {(actions as Action[]).map((action: Action) => {
                        return (
                          <Stack.Item
                            key={`${action.source}-${action.type}-${action.id}-${index}`}
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
                              <Stack.Item style={{ width: '100%', maxWidth: 'calc(100% - 5.4rem', overflow: 'hidden' }}>
                                <MissionTimelineItemContainer
                                  actionSource={action.source}
                                  actionType={action.type as any}
                                >
                                  <MissionTimelineItem
                                    action={action}
                                    previousActionWithSameType={find(
                                      mission.actions,
                                      { type: action.type },
                                      mission.actions.findIndex(item => item === action) + 1
                                    )}
                                    onClick={() => onSelectAction(action)}
                                  />
                                </MissionTimelineItemContainer>
                              </Stack.Item>
                              {action.completenessForStats.status === CompletenessForStatsStatusEnum.COMPLETE &&
                              action.type !== ActionTypeEnum.STATUS ? (
                                <Stack.Item
                                  alignSelf="stretch"
                                  style={{ width: '15px', padding: '5px 0 5px 5px' }}
                                  data-testid={'timeline-item-status'}
                                >
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
                              ) : action.completenessForStats.status === CompletenessForStatsStatusEnum.INCOMPLETE ? (
                                <Stack.Item data-testid={'timeline-item-incomplete-report'}>
                                  <IconButton
                                    accent={Accent.TERTIARY}
                                    Icon={Icon.AttentionFilled}
                                    color={
                                      getColorForStatus(action.status) === 'transparent'
                                        ? THEME.color.charcoal
                                        : getColorForStatus(action.status)
                                    }
                                    title={
                                      'Cet évènement contient des données manquantes indispensables pour les statistiques.'
                                    }
                                    style={{ cursor: 'auto', width: '20px', marginLeft: '-5px' }}
                                  />
                                </Stack.Item>
                              ) : undefined}
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
}

export default MissionTimeline
