import { Accent, Button, Dialog, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { createElement } from 'react'
import { FlexboxGrid, Stack } from 'rsuite'
import { BasicAction } from '../../types/basic-action'

type BasicDialogFormProps = {
  initValue: any
  action?: BasicAction
  onSubmit: (response: boolean, value?: any) => void
}

const BasicDialogForm: React.FC<BasicDialogFormProps> = ({ action, initValue, onSubmit }) => {
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
          <Dialog.Body style={{ padding: '24px 24px 0px 24px' }}>
            <Stack.Item style={{ width: '100%' }}>
              {action && createElement(action.form, { formik, type: action.key, ...(action.formProps ?? {}) })}
            </Stack.Item>
          </Dialog.Body>
          <Dialog.Action style={{ display: 'flex', justifyContent: 'flex-end', padding: '32px 24px 24px 24px' }}>
            <Button data-testid="dialog-form-cancel-button" accent={Accent.SECONDARY} onClick={() => onSubmit(false)}>
              Annuler
            </Button>
            <Button
              disabled={!formik.isValid}
              data-testid="dialog-form-confirm-button"
              accent={action?.accent ?? Accent.PRIMARY}
              onClick={() => onSubmit(true, formik.values)}
              className={[Accent.ERROR, Accent.WARNING].includes(action?.accent ?? Accent.PRIMARY) ? '_active' : ''}
            >
              {action?.validateButton ?? 'Confirmer'}
            </Button>
          </Dialog.Action>
        </Dialog>
      )}
    </Formik>
  )
}

export default BasicDialogForm
