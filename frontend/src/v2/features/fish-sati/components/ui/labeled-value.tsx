import Text from '@common/components/ui/text.tsx'
import { THEME } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'

interface LabeledValueProps {
  label?: string
  value?: string | number
}

const LabeledValue: FC<LabeledValueProps> = ({ value, label }) => (
  <Stack direction="row" justifyContent="flex-start" alignItems="flex-end" spacing=".5rem">
    <Stack.Item>
      <Text as="h3" color={THEME.color.slateGray} weight="medium">
        {value}
      </Text>
    </Stack.Item>
    <Stack.Item>
      {label && (
        <Text as="h3" color={THEME.color.slateGray} weight="normal">
          ({label})
        </Text>
      )}
    </Stack.Item>
  </Stack>
)

export default LabeledValue
