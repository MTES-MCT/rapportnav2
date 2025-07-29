import Text from '@common/components/ui/text'
import { Tag, THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { InquiryStatusType } from '../../../common/types/inquiry'
import { useInquiryStatus } from '../../hooks/use-inquiry-status'

interface InquiryStatusCompletenessProps {
  status?: InquiryStatusType
}

const InquiryStatusCompleteness: FC<InquiryStatusCompletenessProps> = ({ status }) => {
  const statusComponent = useInquiryStatus(status ?? InquiryStatusType.NEW)
  return (
    <Tag
      withCircleIcon={true}
      Icon={statusComponent.icon}
      color={THEME.color.charcoal}
      iconColor={statusComponent.color}
      backgroundColor={THEME.color.cultured}
    >
      <Text as="h3" weight="medium" color={THEME.color.charcoal}>
        {statusComponent.text}
      </Text>
    </Tag>
  )
}

export default InquiryStatusCompleteness
