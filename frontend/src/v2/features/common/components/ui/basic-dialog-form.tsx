import { Dialog } from '@common/components/ui/custom-dialog.tsx'
import { Formik } from 'formik'
import { createElement } from 'react'
import { Stack } from 'rsuite'
import { BasicAction } from '../../types/basic-action'
import BasicDialogAction from './basic-dialog-action.tsx'
import BasicDialogTitle from './basic-dialog-title.tsx'

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
            <BasicDialogTitle label={action?.label} onClose={() => onSubmit(false)} />
          </Dialog.Title>
          <Dialog.Body style={{ padding: '24px 24px 0px 24px' }}>
            <Stack.Item style={{ width: '100%' }}>
              {action && createElement(action.form, { formik, type: action.key, ...(action.formProps ?? {}) })}
            </Stack.Item>
          </Dialog.Body>
          <Dialog.Action style={{ display: 'flex', justifyContent: 'flex-end', padding: '32px 24px 24px 24px' }}>
            <BasicDialogAction
              isValid={formik.isValid}
              accent={action?.accent}
              validateButtonLabel={action?.validateButton}
              onSubmit={response => onSubmit(response, response ? formik.values : undefined)}
            />
          </Dialog.Action>
        </Dialog>
      )}
    </Formik>
  )
}

export default BasicDialogForm
