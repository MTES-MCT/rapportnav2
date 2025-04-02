import { FC } from 'react'
import MissionTimelineWrapper from '../../../mission-timeline/components/layout/mission-timeline-wrapper'
import { MissionTimelineAction } from '../../../mission-timeline/types/mission-timeline-output'
import MissionTimelineItemPam from './mission-timeline-pam-item'
import some from 'lodash/some'
import matchesProperty from 'lodash/matchesProperty'
import { NetworkSyncStatus } from '../../../common/types/network-types.ts'
import Text from '@common/components/ui/text.tsx'
import { Stack } from 'rsuite'
import { Icon, THEME } from '@mtes-mct/monitor-ui'

interface MissionTimelinePamBodyProps {
  missionId: number
  isLoading: boolean
  isError: Error | null
  actions: MissionTimelineAction[]
}

const MissionTimelinePamBody: FC<MissionTimelinePamBodyProps> = ({ isError, actions, missionId, isLoading }) => {
  const hasActionsNotYetSyncWithServer = some(actions, matchesProperty('networkSyncStatus', NetworkSyncStatus.UNSYNC))
  return (
    <>
      {hasActionsNotYetSyncWithServer && (
        <Stack direction={'row'} alignItems={'center'} spacing={'1rem'} style={{ marginBottom: '2rem' }}>
          <Stack.Item>
            <Icon.Offline color={THEME.color.charcoal} size={14} />
          </Stack.Item>
          <Stack.Item>
            <Text as={'h3'} weight={'medium'} fontStyle={'italic'}>
              Attention, nous êtes actuellement hors connexion. Les actions ajoutées ou modifiées seront enregistrées
              dans le serveur quand vous reviendrez en ligne.
            </Text>
          </Stack.Item>
        </Stack>
      )}
      <MissionTimelineWrapper
        isError={isError}
        missionId={missionId}
        groupBy="startDateTimeUtc"
        isLoading={isLoading}
        item={MissionTimelineItemPam}
        actions={actions}
      />
    </>
  )
}

export default MissionTimelinePamBody
