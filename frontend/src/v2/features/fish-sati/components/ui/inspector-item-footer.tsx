import { Accent, Button, Size } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Stack } from 'rsuite'

interface InspectorItemFooterProps {
  onClose: () => void
  onSubmit: () => void
}

const InspectorItemFooter: FC<InspectorItemFooterProps> = ({ onClose, onSubmit }) => {
  return (
    <Stack justifyContent="flex-end" spacing={'1rem'} style={{ width: '100%' }}>
      <Stack.Item>
        <Button size={Size.NORMAL} role="cancel-infraction" accent={Accent.SECONDARY} onClick={onClose}>
          Annuler
        </Button>
      </Stack.Item>
      <Stack.Item>
        <Button
          onClick={onSubmit}
          size={Size.NORMAL}
          accent={Accent.PRIMARY}
          role="validate-infraction"
          data-testid="validate-infraction"
        >
          Valider
        </Button>
      </Stack.Item>
    </Stack>
  )
}

export default InspectorItemFooter
