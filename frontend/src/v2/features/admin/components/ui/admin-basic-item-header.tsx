import Text from '@common/components/ui/text'
import { Accent, Button } from '@mtes-mct/monitor-ui'
import React from 'react'
import { Stack } from 'rsuite'
import { BasicAction } from '../../../common/types/basic-action'

type AdminBasicItemHeaderProps = {
  title: string
  count?: number
  mainAction?: BasicAction
  onAction: (action: BasicAction) => void
}

const AdminBasicItemHeader: React.FC<AdminBasicItemHeaderProps> = ({ title, count, mainAction, onAction }) => (
  <Stack direction="row" justifyContent="space-between" alignItems="flex-start">
    <Stack.Item>
      <Text as="h1">{`${title} (${count ?? 0})`}</Text>
    </Stack.Item>
    <Stack.Item>
      {mainAction && (
        <Button accent={Accent.PRIMARY} onClick={() => onAction(mainAction)}>
          {mainAction.label}
        </Button>
      )}
    </Stack.Item>
  </Stack>
)

export default AdminBasicItemHeader
