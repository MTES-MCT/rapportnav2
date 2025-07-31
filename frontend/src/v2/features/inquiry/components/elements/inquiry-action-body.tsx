import { FC } from 'react'
import ActionWrapper from '../../../common/components/layout/action-wrapper'
import { MissionAction } from '../../../common/types/mission-action'
import InquiryActionItem from './inquiry-action-item'

interface InquiryActionBodyProps {
  ownerId: string
  isLoading?: boolean
  error?: Error | null
  action?: MissionAction
}

const InquiryActionBody: FC<InquiryActionBodyProps> = ({ ownerId, error, action, isLoading }) => {
  return (
    <ActionWrapper action={action} isError={error} ownerId={ownerId} isLoading={isLoading} item={InquiryActionItem} />
  )
}

export default InquiryActionBody
