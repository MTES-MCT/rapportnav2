import { Accent, Button, Dialog, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { createElement } from 'react'
import { FlexboxGrid, Stack } from 'rsuite'

type DialogQuestionProps = {
  title: string
  question: string
  onSubmit: (response: boolean) => void
  type: 'danger' | 'neutral'
}

const styles = {
  danger: {
    fontWeight: 'bold',
    icon: Icon.Attention,
    color: THEME.color.maximumRed
  },
  neutral: {
    fontWeight: 'normal',
    icon: Icon.Attention,
    color: THEME.color.charcoal
  }
}

const DialogQuestion: React.FC<DialogQuestionProps> = ({ type, title, question, onSubmit }) => {
  return (
    <Dialog>
      <Dialog.Title>
        <FlexboxGrid align="middle" justify="space-between" style={{ paddingLeft: 14, paddingRight: 24 }}>
          <FlexboxGrid.Item style={{ fontSize: '16px' }}>{title}</FlexboxGrid.Item>
          <FlexboxGrid.Item>
            <IconButton
              Icon={Icon.Close}
              size={Size.NORMAL}
              accent={Accent.TERTIARY}
              color={THEME.color.gainsboro}
              role={'dialog-question'}
              data-testid="close-dialog-question"
              onClick={() => onSubmit(true)}
            />
          </FlexboxGrid.Item>
        </FlexboxGrid>
      </Dialog.Title>
      <Dialog.Body>
        <Stack direction="row" justifyContent="center" alignItems="flex-start" style={styles[type]}>
          <Stack.Item>{createElement(styles[type].icon, { size: 20 })}</Stack.Item>
          <Stack.Item style={{ marginLeft: 4 }}>{question}</Stack.Item>
        </Stack>
      </Dialog.Body>
      <Dialog.Action>
        <Button data-testId="dialog-question-cancel-button" accent={Accent.SECONDARY} onClick={() => onSubmit(false)}>
          Annuler
        </Button>
        <Button data-testId="dialog-question-confirm-button" accent={Accent.PRIMARY} onClick={() => onSubmit(true)}>
          Confirmer
        </Button>
      </Dialog.Action>
    </Dialog>
  )
}

export default DialogQuestion
