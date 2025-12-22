import Text from '@common/components/ui/text.tsx'
import { Accent, Button, Icon, Size } from '@mtes-mct/monitor-ui'
import { useState } from 'react'
import PageFooterWrapper from '../../../common/components/layout/page-footer-wrapper.tsx'
import DialogQuestion from '../../../common/components/ui/dialog-question.tsx'
import { useDate } from '../../../common/hooks/use-date.tsx'
import { useOnlineManager } from '../../../common/hooks/use-online-manager.tsx'
import { InquiryStatusType } from '../../../common/types/inquiry.ts'
import { useInquiry } from '../../hooks/use-inquiry.tsx'
import useDeleteInquiryMutation from '../../services/use-delete-inquiry.tsx'
import useGetInquiryQuery from '../../services/use-inquiry.tsx'
import useUpdateInquiryMutation from '../../services/use-update-inquiry.tsx'

interface InquiryFooterProps {
  inquiryId?: string
  exitMission: () => void
}

const InquiryFooter: React.FC<InquiryFooterProps> = ({ inquiryId, exitMission }) => {
  const { isClosable } = useInquiry()
  const { isOnline } = useOnlineManager()
  const { formatDateForFrenchHumans } = useDate()
  const [showDialog, setShowDialog] = useState(false)
  const mutationUpdate = useUpdateInquiryMutation(inquiryId)
  const mutationDelete = useDeleteInquiryMutation(inquiryId)
  const { data: inquiry, dataUpdatedAt } = useGetInquiryQuery(inquiryId)

  const handleDeleteMission = async (response: boolean) => {
    setShowDialog(false)
    if (!response) return
    await mutationDelete.mutateAsync()
    exitMission()
  }

  const handleUpdateStatus = async (status: InquiryStatusType) => {
    await mutationUpdate.mutateAsync({ ...inquiry, status })
  }
  return (
    <>
      <PageFooterWrapper
        action={
          <Button
            accent={Accent.ERROR}
            size={Size.NORMAL}
            Icon={Icon.Delete}
            onClick={() => setShowDialog(true)}
            title={"Cette fonctionnalité n'a pas encore été implémentée"}
          >
            Supprimer le contrôle croisé
          </Button>
        }
        message={
          <Text as="h4">
            {`Connexion ${isOnline ? 'disponible' : 'indisponible'} `}&nbsp;
            {dataUpdatedAt ? `- Dernière synchronisation le ${formatDateForFrenchHumans(dataUpdatedAt)}` : ``}
          </Text>
        }
        online={
          <Button
            disabled={!isClosable(inquiry)}
            Icon={inquiry?.status === InquiryStatusType.CLOSED ? Icon.Unlock : Icon.Check}
            accent={inquiry?.status === InquiryStatusType.CLOSED ? Accent.SECONDARY : Accent.PRIMARY}
            title={inquiry?.status === InquiryStatusType.CLOSED ? '' : 'Les chanmps obligatoires doivent être remplis'}
            onClick={() =>
              handleUpdateStatus(
                inquiry?.status === InquiryStatusType.CLOSED ? InquiryStatusType.IN_PROGRESS : InquiryStatusType.CLOSED
              )
            }
          >
            {`${inquiry?.status === InquiryStatusType.CLOSED ? 'Ré-ouvrir' : 'Cloturer'} le contrôle`}
          </Button>
        }
        exitMission={exitMission}
      />
      {showDialog && (
        <DialogQuestion
          type="danger"
          onSubmit={handleDeleteMission}
          title="Suppression du contrôle croisé"
          question="Voulez vous vraiment supprimer ce contrôle croisé?"
        />
      )}
    </>
  )
}

export default InquiryFooter
