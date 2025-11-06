import { Accent, Button, Icon, Size } from '@mtes-mct/monitor-ui'
import { useGlobalRoutes } from '@router/use-global-routes'
import { FC } from 'react'
import { useNavigate } from 'react-router-dom'
import { useTimelineAction } from '../../../common/hooks/use-timeline-action'
import useCreateActionMutation from '../../../common/services/use-create-action'
import { ActionType } from '../../../common/types/action-type'
import { InquiryStatusType } from '../../../common/types/inquiry'
import { OwnerType } from '../../../common/types/owner-type'
import useGetInquiryQuery from '../../services/use-inquiry'

interface InquiryTimelineHeaderProps {
  inquiryId: string
}

const InquiryTimelineHeader: FC<InquiryTimelineHeaderProps> = ({ inquiryId }) => {
  const navigate = useNavigate()
  const { getUrl } = useGlobalRoutes()
  const mutation = useCreateActionMutation()
  const { data: inquiry } = useGetInquiryQuery(inquiryId)
  const { getActionInput } = useTimelineAction(inquiryId)

  const handleAddActionInquiry = async () => {
    const action = getActionInput(ActionType.INQUIRY)
    const response = await mutation.mutateAsync({ ownerId: inquiryId, ownerType: OwnerType.INQUIRY, action })
    if (response && response.id) navigate(`${getUrl(OwnerType.INQUIRY)}/${inquiryId}/${response.id}`)
  }

  return (
    <Button
      Icon={Icon.Plus}
      size={Size.NORMAL}
      accent={Accent.PRIMARY}
      onClick={handleAddActionInquiry}
      disabled={inquiry?.status === InquiryStatusType.CLOSED}
    >
      Ajouter une session de travail
    </Button>
  )
}

export default InquiryTimelineHeader
