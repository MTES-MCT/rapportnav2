import { useStore } from '@tanstack/react-store'
import { isEqual } from 'lodash'
import { FC } from 'react'
import { store } from '../../../../store'
import { resetDebounceTime } from '../../../../store/slices/delay-query-reducer'
import { useDelay } from '../../../common/hooks/use-delay'
import useUpdateActionMutation from '../../../common/services/use-update-action'
import { MissionAction } from '../../../common/types/mission-action'
import { OwnerType } from '../../../common/types/owner-type'
import MissionActionItemInquiry from '../../../mission-action/components/elements/mission-action-item-inquiry'

interface InquiryActionItemProps {
  ownerId: string
  action: MissionAction
}

const InquiryActionItem: FC<InquiryActionItemProps> = ({ action, ownerId }) => {
  const { handleExecuteOnDelay } = useDelay()
  const debounceTime = useStore(store, state => state.delayQuery.debounceTime)
  const mutation = useUpdateActionMutation()

  const onChange = async (newAction: MissionAction) => {
    handleExecuteOnDelay(async () => {
      if (!isEqual(action, newAction)) {
        await mutation.mutateAsync({ ownerId, ownerType: OwnerType.INQUIRY, action: newAction })
      }
      if (debounceTime !== undefined) resetDebounceTime()
    }, debounceTime)
  }

  return (
    <div style={{ width: '100%' }}>
      <MissionActionItemInquiry action={action} onChange={onChange} />
    </div>
  )
}

export default InquiryActionItem
