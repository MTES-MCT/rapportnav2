import { isEqual } from 'lodash'
import { FC } from 'react'
import useUpdateActionMutation from '../../../common/services/use-update-action'
import { MissionAction } from '../../../common/types/mission-action'
import { OwnerType } from '../../../common/types/owner-type'
import MissionActionItemInquiry from '../../../mission-action/components/elements/mission-action-item-inquiry'

interface InquiryActionItemProps {
  ownerId: string
  action: MissionAction
}

const InquiryActionItem: FC<InquiryActionItemProps> = ({ action, ownerId }) => {
  const mutation = useUpdateActionMutation()

  const onChange = async (newAction: MissionAction) => {
    if (isEqual(action, newAction) && action.id) return
    await mutation.mutateAsync({ ownerId, ownerType: OwnerType.INQUIRY, action: newAction })
  }

  return (
    <div style={{ width: '100%' }}>
      <MissionActionItemInquiry action={action} onChange={onChange} />
    </div>
  )
}

export default InquiryActionItem
