import { Label } from '@mtes-mct/monitor-ui'
import { FC, JSX } from 'react'
import { Stack } from 'rsuite'

interface InspectorItemLayoutProps {
  title?: string
  inspectorItem: JSX.Element
}

const InspectorItemLayout: FC<InspectorItemLayoutProps> = ({ title, inspectorItem }) => {
  return (
    <Stack direction="column" spacing="0.5rem" alignItems="flex-start" style={{ width: '100%', marginTop: '0.5rem' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Label>{title}</Label>
      </Stack.Item>
      <Stack.Item style={{ width: '100%', padding: '16px', backgroundColor: 'white' }}>{inspectorItem}</Stack.Item>
    </Stack>
  )
}
export default InspectorItemLayout
