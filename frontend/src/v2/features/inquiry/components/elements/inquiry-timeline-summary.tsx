import Text from '@common/components/ui/text'
import { FC, useMemo } from 'react'
import { Stack } from 'rsuite'
import { useTarget } from '../../../mission-target/hooks/use-target'
import useGetInquiryQuery from '../../services/use-inquiry'
import InquirySummaryItem from '../ui/inquiry-timeline-summary-item'

interface InquirySummaryProps {
  inquiryId?: string
}

const InquirySummary: FC<InquirySummaryProps> = ({ inquiryId }) => {
  const { getNbrInfraction } = useTarget()
  const { data: inquiry } = useGetInquiryQuery(inquiryId)
  const nbrOfHours = useMemo(() => {
    if (!inquiry?.actions?.length) return 0
    return inquiry.actions.reduce((acc, action) => acc + (action.data.nbrOfHours ?? 0), 0)
  }, [inquiry])
  const nbrOfInfractions = useMemo(() => {
    if (!inquiry?.actions?.length) return 0
    return inquiry.actions.reduce((acc, action) => acc + getNbrInfraction(action.data.targets), 0)
  }, [inquiry, getNbrInfraction])

  return (
    <Stack direction="column" justifyContent="flex-start" alignItems="flex-start">
      <Stack.Item style={{ marginBottom: 3 }}>
        <Text as="h2" weight="bold">
          Synthese du contrôle
        </Text>
      </Stack.Item>
      <Stack.Item>
        <InquirySummaryItem value={`${nbrOfHours}h`} label={'passées sur le contrôle à date'} />
      </Stack.Item>
      <Stack.Item>
        <InquirySummaryItem value={nbrOfInfractions} label={'infractions'} />
      </Stack.Item>
    </Stack>
  )
}

export default InquirySummary
