import Text from '@common/components/ui/text.tsx'
import { Accent, Tag } from '@mtes-mct/monitor-ui'
import { FC } from 'react'

interface MissionListCountTagProps {
  count: number
}

/** Primary tag showing how many reports are currently loaded, rendered just above the list. */
const MissionListCountTag: FC<MissionListCountTagProps> = ({ count }) => (
  <Tag accent={Accent.PRIMARY}>
    <Text as="h3" weight="bold">
      {count} rapport{count > 1 ? 's' : ''}
    </Text>
  </Tag>
)

export default MissionListCountTag
