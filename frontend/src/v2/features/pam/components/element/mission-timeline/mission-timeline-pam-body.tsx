import Text from '@common/components/ui/text.tsx'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { useGlobalRoutes } from '@router/use-global-routes.tsx'
import matchesProperty from 'lodash/matchesProperty'
import some from 'lodash/some'
import { FC } from 'react'
import { Loader, Stack } from 'rsuite'
import TimelineWrapper from '../../../../common/components/layout/timeline-wrapper.tsx'
import { NetworkSyncStatus } from '../../../../common/types/network-types.ts'
import { OwnerType } from '../../../../common/types/owner-type.ts'
import { MissionTimelineAction } from '../../../../mission-timeline/types/mission-timeline-output.ts'
import MissionTimelineItemPam from './mission-timeline-pam-item.tsx'
import { useGlobalSyncStatus } from '../../../../common/hooks/use-global-sync-status.tsx'

interface MissionTimelinePamBodyProps {
  missionId: number
  isLoading: boolean
  isError: Error | null
  actions: MissionTimelineAction[]
}

const MissionTimelinePamBody: FC<MissionTimelinePamBodyProps> = ({ isError, actions, missionId, isLoading }) => {
  const { getUrl } = useGlobalRoutes()
  const hasActionsNotYetSyncWithServer = some(actions, matchesProperty('networkSyncStatus', NetworkSyncStatus.UNSYNC))
  const { active: syncActive, mutatingCount } = useGlobalSyncStatus(300)
  return (
    <>
      {syncActive && mutatingCount > 1 ? (
        <Stack direction={'row'} alignItems={'center'} spacing={'1rem'} style={{ marginBottom: '2rem' }}>
          <Stack.Item>
            <Loader center={false} size={'md'} vertical={false} />
          </Stack.Item>
          <Stack.Item>
            <Text as={'h3'} weight={'medium'} fontStyle={'italic'}>
              {`Synchronisation de ${mutatingCount} action(s) avec le serveur`}
            </Text>
          </Stack.Item>
        </Stack>
      ) : hasActionsNotYetSyncWithServer ? (
        <Stack direction={'row'} alignItems={'center'} spacing={'1rem'} style={{ marginBottom: '2rem' }}>
          <Stack.Item>
            <Icon.Offline color={THEME.color.charcoal} size={14} />
          </Stack.Item>
          <Stack.Item>
            <Text as={'h3'} weight={'medium'} fontStyle={'italic'}>
              Attention, vous êtes actuellement hors connexion. Les actions ajoutées ou modifiées seront enregistrées
              dans le serveur quand vous reviendrez en ligne.
            </Text>
          </Stack.Item>
        </Stack>
      ) : null}
      <TimelineWrapper
        isError={isError}
        actions={actions}
        isLoading={isLoading}
        groupBy="startDateTimeUtc"
        item={MissionTimelineItemPam}
        baseUrl={`${getUrl(OwnerType.MISSION)}/${missionId}`}
      />
    </>
  )
}

export default MissionTimelinePamBody
