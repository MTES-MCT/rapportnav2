import Text from '@common/components/ui/text'
import { ActionTypeEnum } from '@common/types/env-mission-types'
import { formatDateTimeForFrenchHumans } from '@common/utils/dates-for-humans'
import { IconProps, THEME } from '@mtes-mct/monitor-ui'
import { createElement } from 'react'
import { Stack } from 'rsuite'
import { useActionRegistry } from '../../../../features/common/hooks/use-action-registry'
import MissionActionHeaderAction from './mission-action-header-action'

interface MissionActionHeaderTitleProps {
  actionType: ActionTypeEnum
  startDateTimeUtc?: string
}

export const MissionActionHeaderTitle: React.FC<MissionActionHeaderTitleProps> = ({ actionType, startDateTimeUtc }) => {
  const { title, icon } = useActionRegistry(actionType)

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
        <MissionActionHeaderAction />
      </Stack.Item>
    </Stack>
  )
}

export default MissionActionHeaderTitle
