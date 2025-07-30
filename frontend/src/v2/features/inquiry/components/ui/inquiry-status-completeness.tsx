import Text from '@common/components/ui/text'
import { IconProps, Tag, THEME } from '@mtes-mct/monitor-ui'
import { FC, FunctionComponent } from 'react'

interface InquiryStatusCompletenessProps {
  status: { text: string; color: string; icon: FunctionComponent<IconProps> }
}

const InquiryStatusCompleteness: FC<InquiryStatusCompletenessProps> = ({ status }) => {
  return (
    <Tag
      withCircleIcon={true}
      Icon={status.icon}
      color={status.color}
      iconColor={status.color}
      backgroundColor={THEME.color.cultured}
    >
      <Text as="h3" weight="medium" color={status.color}>
        {status.text}
      </Text>
    </Tag>
  )
}

export default InquiryStatusCompleteness
