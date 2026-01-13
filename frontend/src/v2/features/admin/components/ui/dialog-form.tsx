import { Accent, Button, Dialog, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { createElement } from 'react'
import { FlexboxGrid, Stack } from 'rsuite'
import { AdminAction } from '../../types/admin-action'

type DialogFormProps = {
  initValue: any
  action?: AdminAction
  onSubmit: (response: boolean, value?: any) => void
}

const DialogForm: React.FC<DialogFormProps> = ({ action, initValue, onSubmit }) => {
  return (
    <Formik
      validateOnChange={true}
      enableReinitialize={true}
      initialValues={initValue}
      onSubmit={value => console.log(value)}
    >
      {formik => (
        <Dialog>
          <Dialog.Title>
            <FlexboxGrid align="middle" justify="space-between" style={{ paddingLeft: 14, paddingRight: 24 }}>
              <FlexboxGrid.Item style={{ fontSize: '16px' }}>{action?.label}</FlexboxGrid.Item>
              <FlexboxGrid.Item>
                <IconButton
                  Icon={Icon.Close}
                  size={Size.NORMAL}
                  accent={Accent.TERTIARY}
                  color={THEME.color.gainsboro}
                  role={'dialog-form'}
                  data-testid="close-dialog-form"
                  onClick={() => onSubmit(false)}
                />
              </FlexboxGrid.Item>
            </FlexboxGrid>
          </Dialog.Title>
          <Dialog.Body>
            <Stack.Item style={{ width: '100%' }}>
              {action && createElement(action.form, { formik, type: action.key, ...(action.formProps ?? {}) })}
            </Stack.Item>
          </Dialog.Body>
          <Dialog.Action>
            <Button data-testid="dialog-form-cancel-button" accent={Accent.SECONDARY} onClick={() => onSubmit(false)}>
              Annuler
            </Button>
            <Button
              data-testid="dialog-form-confirm-button"
              accent={Accent.PRIMARY}
              onClick={() => onSubmit(true, formik.values)}
            >
              Confirmer
            </Button>
          </Dialog.Action>
        </Dialog>
      )}
    </Formik>
  )
}

export default DialogForm
