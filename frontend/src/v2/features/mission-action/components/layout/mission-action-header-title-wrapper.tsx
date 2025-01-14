import Text from '@common/components/ui/text'
import { formatDateTimeForFrenchHumans } from '@common/utils/dates-for-humans'
import { Banner, IconProps, Level, THEME } from '@mtes-mct/monitor-ui'
import React, { createElement, FunctionComponent } from 'react'
import { Stack } from 'rsuite'
import { MissionSource } from '../../../common/types/mission-types'
import { ModuleType } from '../../../common/types/module-type'
import MissionActionHeaderAction from '../elements/mission-action-header-action'
import { SyncStatus } from '../../../common/types/network-types.ts'

interface MissionActionHeaderTitleWrapperProps {
  title?: string
  actionId?: string
  missionId: number
  source: MissionSource
  moduleType: ModuleType
  startDateTimeUtc?: string
  icon?: FunctionComponent<IconProps>
  syncStatus?: SyncStatus
}

export const MissionActionHeaderTitleWrapper: React.FC<MissionActionHeaderTitleWrapperProps> = ({
  icon,
  title,
  source,
  actionId,
  missionId,
  moduleType,
  startDateTimeUtc,
  syncStatus
}) => {
  return (
    <>
      {syncStatus === SyncStatus.UNSYNC && (
        <Banner
          data-testid={'mission-report-status-banner'}
          isClosable={false}
          isCollapsible={false}
          isHiddenByDefault={false}
          level={Level.INFO}
          top={'60px'}
        >
          Attention, cette action n'est pas encore synchronisée avec le serveur
        </Banner>
      )}
      <Stack direction="row" spacing="0.5rem" style={{ width: '100%', alignItems: 'initial' }}>
        <Stack.Item alignSelf="baseline">
          {icon && createElement<IconProps>(icon, { color: THEME.color.charcoal, size: 20 })}
        </Stack.Item>
        <Stack.Item grow={2}>
          <Stack direction="row" spacing={'0.5rem'} wrap={true}>
            <Stack.Item>
              <Text as="h2">{title}</Text>
            </Stack.Item>
            <Stack.Item>
              <Text as="h2" weight={'normal'}>
                ({formatDateTimeForFrenchHumans(startDateTimeUtc)})
              </Text>
            </Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item>
          {actionId && (
            <MissionActionHeaderAction
              source={source}
              actionId={actionId}
              missionId={missionId}
              moduleType={moduleType}
            />
          )}
        </Stack.Item>
      </Stack>
    </>
  )
}

export default MissionActionHeaderTitleWrapper
