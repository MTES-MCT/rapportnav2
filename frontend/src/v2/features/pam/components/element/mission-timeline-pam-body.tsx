import Text from '@common/components/ui/text.tsx'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { useGlobalRoutes } from '@router/use-global-routes.tsx'
import matchesProperty from 'lodash/matchesProperty'
import some from 'lodash/some'
import { FC } from 'react'
import { Stack } from 'rsuite'
import TimelineWrapper from '../../../common/components/layout/timeline-wrapper.tsx'
import { NetworkSyncStatus } from '../../../common/types/network-types.ts'
import { OwnerType } from '../../../common/types/owner-type.ts'
import { MissionTimelineAction } from '../../../mission-timeline/types/mission-timeline-output'
import MissionTimelineItemPam from './mission-timeline-pam-item'
import { useMutationState } from '@tanstack/react-query'
import { useOnlineManager } from '../../../common/hooks/use-online-manager.tsx'

interface MissionTimelinePamBodyProps {
  missionId: number
  isLoading: boolean
  isError: Error | null
  actions: MissionTimelineAction[]
}

const MissionTimelinePamBody: FC<MissionTimelinePamBodyProps> = ({ isError, actions, missionId, isLoading }) => {
  const { getUrl } = useGlobalRoutes()
  const { isOnline } = useOnlineManager()
  const hasActionsNotYetSyncWithServer = some(actions, matchesProperty('networkSyncStatus', NetworkSyncStatus.UNSYNC))
  const inflightMutations = useMutationState({
    filters: {
      predicate: mutation => mutation.options.meta?.offline && mutation.state.status === 'pending'
    }
  })

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
      {isOnline && inflightMutations.length > 0 && (
        <Stack
          direction={'row'}
          alignItems={'center'}
          spacing={'1rem'}
          style={{ marginTop: '1rem', marginBottom: '2rem' }}
        >
          <Stack.Item>
            <Icon.Warning color={THEME.color.charcoal} size={14} />
          </Stack.Item>
          <Stack.Item>
            <Text as={'h3'} color={THEME.color.persianOrange} weight={'medium'} fontStyle={'italic'}>
              Synchronisation en cours avec le serveur action par action, cela peut prendre un peu de temps en fonction
              de votre connexion. Il reste {inflightMutations.length} sync.
            </Text>
          </Stack.Item>
        </Stack>
      )}
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
