import { MissionSourceEnum } from '@common/types/env-mission-types.ts'
import { useMissionTag } from '@features/v2/common/hooks/use-mission-tag'
import { Tag } from '@mtes-mct/monitor-ui'
import React from 'react'
import Text from '../../../../common/components/ui/text'

interface MissionSourceTagProps {
  isFake?: boolean
  missionSource?: MissionSourceEnum
}

const MissionSourceTag: React.FC<MissionSourceTagProps> = ({ missionSource, isFake }) => {
  const { getTagTextColor, getTagTextContent, getTagBorderColor, getTagBackgroundColor } = useMissionTag(
    !isFake ? missionSource : undefined
  )

  const textColor = getTagTextColor()
  const borderColor = getTagBorderColor()
  const textContent = getTagTextContent()
  const backgroundColor = getTagBackgroundColor()

  return (
    <Tag backgroundColor={backgroundColor} borderColor={borderColor} color={textColor}>
      <Text as={'h3'} weight="medium" color={textColor}>
        {isFake ? ' Mission fictive' : textContent}
      </Text>
    </Tag>
  )
}

export default MissionSourceTag
