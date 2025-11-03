import { FormikMultiSelect, FormikTextInput } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { Stack } from 'rsuite'
import { RoleType } from '../../../common/types/role-type.ts'
import { AdminActionType } from '../../types/admin-action.ts'
import AdminUserPasswordForm from './admin-user-password-form.tsx'

interface AdminUserProps {
  formik: FormikProps<any>
  type: AdminActionType
}

const AdminUserForm: React.FC<AdminUserProps> = ({ formik, type }) => {
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
        <Stack.Item style={{ width: '100%' }}>
          <FormikTextInput name="email" label="Email" itemType="email" placeholder="mail@gouv.fr" required />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Stack
            spacing="1rem"
            direction="row"
            alignItems="flex-end"
            style={{ width: '100%' }}
            justifyContent={'flex-start'}
          >
            <Stack.Item style={{ width: '100%' }}>
              <FormikMultiSelect
                name="roles"
                label="Roles"
                options={[
                  { value: RoleType.ADMIN, label: 'ADMIN' },
                  { value: RoleType.USER_PAM, label: 'PAM' },
                  { value: RoleType.USER_ULAM, label: 'ULAM' }
                ]}
                required
              />
            </Stack.Item>
          </Stack>
        </Stack.Item>
      </Stack>
      {type === AdminActionType.CREATE && <AdminUserPasswordForm formik={formik} />}
    </Stack.Item>
  )
}

export default AdminUserForm
