import { Accent, Button, Dialog, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { Formik, FormikProps } from 'formik'
import { createElement, FunctionComponent } from 'react'
import { FlexboxGrid, Stack } from 'rsuite'

type DialogFormProps = {
  title: string
  initValue: any
  onSubmit: (response: boolean, value?: any) => void
  component?: FunctionComponent<{ formik: FormikProps<unknown> }>
}

const DialogForm: React.FC<DialogFormProps> = ({ initValue, component, title, onSubmit }) => {
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
              <FlexboxGrid.Item style={{ fontSize: '16px' }}>{title}</FlexboxGrid.Item>
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
            <Stack.Item style={{ width: '100%' }}>{component && createElement(component, { formik })}</Stack.Item>
          </Dialog.Body>
          <Dialog.Action>
            <Button data-testId="dialog-form-cancel-button" accent={Accent.SECONDARY} onClick={() => onSubmit(false)}>
              Annuler
            </Button>
            <Button
              data-testId="dialog-form-confirm-button"
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
