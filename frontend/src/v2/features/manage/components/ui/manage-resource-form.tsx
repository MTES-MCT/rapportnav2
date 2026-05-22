import Text from '@common/components/ui/text.tsx'
import { FormikTextInput, Label } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { Stack } from 'rsuite'
import { object, string } from 'yup'
import useHandleDialogForm from '../../../common/hooks/use-handle-dialog-form.tsx'
import { AdminActionType } from '../../../common/types/basic-action.ts'

interface ManageResourceFormProps {
  type: AdminActionType
  formik: FormikProps<any>
}

const ManageResourceForm: React.FC<ManageResourceFormProps> = ({ formik }) => {
  const schema = object().shape({
    registrationId: string().required()
  })

  useHandleDialogForm({ schema, formik })

  return (
    <Stack.Item style={{ width: '100%' }}>
      <Stack direction="column" spacing={'.1rem'} alignItems="flex-start">
        <Stack.Item>
          <Label>Nom du moyen</Label>
        </Stack.Item>
        <Stack.Item>
          <Text as={'h3'}>{formik.values.name}</Text>
        </Stack.Item>
      </Stack>
      <Stack direction="row" alignItems="flex-start" spacing=".5rem" style={{ width: '100%', marginTop: '1rem' }}>
        <Stack.Item style={{ flex: 1, width: '50%' }}>
          <FormikTextInput
            isRequired={true}
            name="registrationId"
            isErrorMessageHidden={true}
            label="Marquage externe/immat"
          />
        </Stack.Item>
        <Stack.Item style={{ width: '50%' }}>
          <FormikTextInput name="radioFrequency" label="Indicatif d'appel radio" isErrorMessageHidden={true} />
        </Stack.Item>
      </Stack>
    </Stack.Item>
  )
}

export default ManageResourceForm
