import { FormikSelect, FormikTextInput } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { Stack } from 'rsuite'
import { object, string } from 'yup'
import useHandleDialogForm from '../../../common/hooks/use-handle-dialog-form.tsx'
import useGetAgentRoles from '../../../common/services/use-agent-roles.tsx'
import { AdminActionType } from '../../../common/types/basic-action.ts'

interface ManageAgentFormProps {
  type: AdminActionType
  formik: FormikProps<any>
}

const ManageAgentForm: React.FC<ManageAgentFormProps> = ({ formik }) => {
  const { data: roles } = useGetAgentRoles()
  const schema = object().shape({
    cardId: string().required(),
    roleId: string().required(),
    lastName: string().required(),
    firstName: string().required()
  })
  useHandleDialogForm({ schema, formik })

  return (
    <Stack.Item style={{ width: '100%' }}>
      <Stack direction="row" spacing={'.5rem'}>
        <Stack.Item style={{ width: '100%' }}>
          <FormikTextInput label="Prénom" name="firstName" isRequired={true} isErrorMessageHidden={true} />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FormikTextInput label="Nom" name="lastName" isRequired={true} isErrorMessageHidden={true} />
        </Stack.Item>
      </Stack>
      <Stack direction="row" alignItems="flex-start" spacing=".5rem" style={{ width: '100%', marginTop: '1rem' }}>
        <Stack.Item style={{ flex: 1, width: '50%' }}>
          <FormikSelect
            label="Role"
            name="roleId"
            isRequired={true}
            isErrorMessageHidden={true}
            options={roles?.map(({ id, title }) => ({ value: id, label: title })) ?? []}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '50%' }}>
          <FormikTextInput name="cardId" isRequired={true} label="N° de carte de service" isErrorMessageHidden={true} />
        </Stack.Item>
      </Stack>
    </Stack.Item>
  )
}

export default ManageAgentForm
