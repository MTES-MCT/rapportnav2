import { Accent, Button, FormikTextInput } from '@mtes-mct/monitor-ui'
import { FormikProps } from 'formik'
import { Stack } from 'rsuite'
import { usePassword } from '../../../common/hooks/use-password.tsx'

interface AdminUserProps {
  formik: FormikProps<any>
}

const AdminUserPasswordForm: React.FC<AdminUserProps> = ({ formik }) => {
  const { generatePassword } = usePassword()
  return (
    <Stack direction="row" spacing=".5rem" alignItems="flex-end" style={{ width: '100%', marginTop: '1rem' }}>
      <Stack.Item style={{ width: '100%' }}>
        <FormikTextInput required type={'text'} name="password" label="Mot de passe" placeholder={'************'} />
      </Stack.Item>
      <Stack.Item>
        <Button
          role="show-password"
          accent={Accent.PRIMARY}
          onClick={() => formik.setFieldValue('password', generatePassword(16))}
        >
          Générer
        </Button>
      </Stack.Item>
      <Stack.Item>
        <Button
          role="show-password"
          accent={Accent.SECONDARY}
          onClick={() => navigator.clipboard.writeText(formik.values['password'])}
        >
          Copier
        </Button>
      </Stack.Item>
    </Stack>
  )
}

export default AdminUserPasswordForm
