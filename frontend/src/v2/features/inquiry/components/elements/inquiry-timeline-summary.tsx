import Text from '@common/components/ui/text'
import { FC, useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { MissionNavAction } from '../../../common/types/mission-action'
import { useTarget } from '../../../mission-target/hooks/use-target'
import useGetInquiryQuery from '../../services/use-inquiry'
import InquirySummaryItem from '../ui/inquiry-timeline-summary-item'

interface InquirySummaryProps {
  inquiryId?: string
}

const InquirySummary: FC<InquirySummaryProps> = ({ inquiryId }) => {
  const { getNbrInfraction } = useTarget()
  const { data: inquiry } = useGetInquiryQuery(inquiryId)
  const [nbrOfHours, setNbrOfHours] = useState<number>(0)
  const [nbrOfInfractions, setNbrOfInfractions] = useState<number>(0)

  useEffect(() => {
    if (!inquiry || !inquiry.actions || inquiry.actions.length === 0) return
    const actions: MissionNavAction[] = inquiry.actions
    setNbrOfHours(actions.reduce((acc, action) => acc + (action.data.nbrOfHours ?? 0), 0))
    setNbrOfInfractions(actions.reduce((acc, action) => acc + getNbrInfraction(action.data.targets), 0))
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
