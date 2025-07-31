import Text from '@common/components/ui/text'
import { FC } from 'react'
import { Stack } from 'rsuite'

interface InquirySummaryItemProps {
  label: string
  value: string | number
}

const InquirySummaryItem: FC<InquirySummaryItemProps> = ({ value, label }) => {
  return (
    <Stack direction="row">
      <Stack.Item>
        <Text as="h2" weight="bold">
          {value}
        </Text>
      </Stack.Item>
      <Stack.Item style={{ marginLeft: 3 }}>
        <Text as="h2" weight="normal" fontStyle="italic">
          {label}
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default InquirySummaryItem
