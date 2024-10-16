import Text from '@common/components/ui/text.tsx'
import { ActionTypeEnum } from '@common/types/env-mission-types.ts'
import { CompletenessForStatsStatusEnum, MissionStatusEnum } from '@common/types/mission-types.ts'
import { IconProps, THEME } from '@mtes-mct/monitor-ui'
import { createElement, FC } from 'react'
import { Stack } from 'rsuite'
import { useActionRegistry } from '../../../common/hooks/use-action-registry.tsx'
import { useDate } from '../../../common/hooks/use-date.tsx'
import { ModuleType } from '../../../common/types/module-type.ts'
import MissionActionHeaderAction from './mission-action-header-action.tsx'
import MissionActionHeaderCompletenessForStats from './mission-action-header-completeness-for-stats.tsx'

export type MissionActionHeaderProps = {
  actionId?: string
  missionId?: string
  moduleType: ModuleType
}

const MissionActionHeader: FC<MissionActionHeaderProps> = ({ actionId, missionId, moduleType }) => {
  const { formatDateTimeForFrenchHumans } = useDate()
  const { title, icon } = useActionRegistry(ActionTypeEnum.CONTROL)
  const date = new Date() //date={envAction.startDateTimeUtc}
  //const { data:action, loading, error } = useActionById(actionId, missionId)

  return (
    <Stack direction="column" spacing={'0.5rem'}>
      <Stack.Item style={{ width: '100%' }}>
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
                  ({formatDateTimeForFrenchHumans(date)})
                </Text>
              </Stack.Item>
            </Stack>
          </Stack.Item>
          <Stack.Item>
            <MissionActionHeaderAction />
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <MissionActionHeaderCompletenessForStats
          missionStatus={MissionStatusEnum.IN_PROGRESS}
          completenessForStats={CompletenessForStatsStatusEnum.INCOMPLETE}
        />
      </Stack.Item>
    </Stack>
  )
}

export default MissionActionHeader
