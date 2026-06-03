import { FormikMultiSelect, FormikNumberInput, FormikTextInput } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { Stack } from 'rsuite'
import useAuth from '../../../auth/hooks/use-auth.tsx'
import { AdminActionType } from '../../../common/types/basic-action.ts'
import AdminUserPasswordForm from './admin-user-password-form.tsx'

interface AdminUserProps {
  formik: FormikProps<any>
  type: AdminActionType
}

const AdminUserForm: React.FC<AdminUserProps> = ({ formik, type }) => {
  const { roleOptions } = useAuth()
  return (
    <Stack.Item style={{ width: '100%' }}>
      <Stack direction="row" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
        <Stack.Item>
          <FormikTextInput name="firstName" label="Prénom" itemType="text" placeholder="" required />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <FormikTextInput name="lastName" label="Nom" itemType="text" placeholder="" required />
        </Stack.Item>
      </Stack>
      <Stack direction="row" alignItems="flex-start" spacing="1rem" style={{ width: '100%', marginTop: '1rem' }}>
        <Stack.Item style={{ width: '50%' }}>
          <FormikTextInput name="email" label="Email" itemType="email" placeholder="mail@gouv.fr" required />
        </Stack.Item>
        <Stack.Item style={{ width: '40%' }}>
          <FormikMultiSelect name="roles" label="Roles" options={roleOptions} required />
        </Stack.Item>
        <Stack.Item style={{ width: '10%' }}>
          <FormikNumberInput name="serviceId" label="Service" itemType="text" required />
        </Stack.Item>
      </Stack>
      {type === AdminActionType.CREATE_USER && <AdminUserPasswordForm formik={formik} />}
    </Stack.Item>
  )
}

export default AdminUserForm
