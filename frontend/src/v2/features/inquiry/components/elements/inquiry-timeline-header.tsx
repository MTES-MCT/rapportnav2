import { Accent, Button, Icon, Size } from '@mtes-mct/monitor-ui'
import { useGlobalRoutes } from '@router/use-global-routes'
import { FC } from 'react'
import { useNavigate } from 'react-router-dom'
import { useTimelineAction } from '../../../common/hooks/use-timeline-action'
import useCreateMissionActionMutation from '../../../common/services/use-create-action'
import { ActionType } from '../../../common/types/action-type'
import { OwnerType } from '../../../common/types/owner-type'

interface InquiryTimelineHeaderProps {
  inquiryId: string
}

const InquiryTimelineHeader: FC<InquiryTimelineHeaderProps> = ({ inquiryId }) => {
  const navigate = useNavigate()
  const { getUrl } = useGlobalRoutes()
  const { getActionInput } = useTimelineAction(inquiryId)
  const mutation = useCreateMissionActionMutation(inquiryId, OwnerType.INQUIRY)

  const handleAddActionInquiry = async () => {
    const action = getActionInput(ActionType.INQUIRY)
    const response = await mutation.mutateAsync(action)
    if (response) navigate(`${getUrl(OwnerType.INQUIRY)}/${inquiryId}/${response.id}`)
  }

  return (
    <Button accent={Accent.PRIMARY} size={Size.NORMAL} Icon={Icon.Plus} onClick={handleAddActionInquiry}>
      Ajouter une session de travail
    </Button>
  )
}

export default InquiryTimelineHeader
