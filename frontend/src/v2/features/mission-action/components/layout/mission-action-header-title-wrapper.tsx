import Text from '@common/components/ui/text'
import { MissionSourceEnum } from '@common/types/env-mission-types'
import { formatDateTimeForFrenchHumans } from '@common/utils/dates-for-humans'
import { IconProps, THEME } from '@mtes-mct/monitor-ui'
import { createElement, FunctionComponent } from 'react'
import { Stack } from 'rsuite'
import MissionActionHeaderAction from '../elements/mission-action-header-action'

interface MissionActionHeaderTitleWrapperProps {
  title?: string
  actionId?: string
  missionId: number
  source: MissionSourceEnum
  startDateTimeUtc?: string
  icon?: FunctionComponent<IconProps>
}

export const MissionActionHeaderTitleWrapper: React.FC<MissionActionHeaderTitleWrapperProps> = ({
  icon,
  title,
  source,
  actionId,
  missionId,
  startDateTimeUtc
}) => {
  return (
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
        {actionId && <MissionActionHeaderAction source={source} actionId={actionId} missionId={missionId} />}
      </Stack.Item>
    </Stack>
  )
}

export default MissionActionHeaderTitleWrapper
