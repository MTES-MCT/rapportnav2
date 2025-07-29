import Text from '@common/components/ui/text'
import { formatDateTimeForFrenchHumans } from '@common/utils/dates-for-humans'
import { IconProps, THEME } from '@mtes-mct/monitor-ui'
import { createElement, FunctionComponent } from 'react'
import { Stack } from 'rsuite'
import { MissionSourceEnum } from '../../types/mission-types'
import { OwnerType } from '../../types/owner-type'
import ActionHeaderAction from '../elements/action-header-action'

interface ActionHeaderTitleWrapperProps {
  title?: string
  actionId?: string
  ownerId: string
  ownerType: OwnerType
  source: MissionSourceEnum
  startDateTimeUtc?: string
  icon?: FunctionComponent<IconProps>
}

export const ActionHeaderTitleWrapper: React.FC<ActionHeaderTitleWrapperProps> = ({
  icon,
  title,
  source,
  actionId,
  ownerId,
  ownerType,
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
        {actionId && <ActionHeaderAction source={source} actionId={actionId} ownerId={ownerId} ownerType={ownerType} />}
      </Stack.Item>
    </Stack>
  )
}

export default ActionHeaderTitleWrapper
