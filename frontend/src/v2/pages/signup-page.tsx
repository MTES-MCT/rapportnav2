import { useNavigate } from 'react-router-dom'
import PageWrapper from '@common/components/ui/page-wrapper.tsx'
import AuthToken from '@features/auth/utils/token.ts'
import { FlexboxGrid, Stack } from 'rsuite'
import { Accent, Button, FormikMultiSelect, FormikTextInput, Size } from '@mtes-mct/monitor-ui'
import { Form, Formik, FormikHelpers } from 'formik'
import { validate } from 'email-validator'
import { csrfToken } from '@features/auth/utils/csrf.ts'
import * as Sentry from '@sentry/react'
import { FC } from 'react'
import { RoleType } from '../features/common/types/role-type.ts'

interface SignUpResponse {
  token: string
}

interface SignUpFormValues {
  firstName: string
  lastName: string
  email: string
  password: string
  serviceId?: string
  roles?: string[]
}

const initialValues: SignUpFormValues = {
  firstName: '',
  lastName: '',
  email: '',
  password: '',
  serviceId: undefined,
  roles: undefined
}

const authToken = new AuthToken()

const SignupPage: FC = () => {
  const navigate = useNavigate()

  const handleSubmit = async (
    { firstName, lastName, email, password, serviceId, roles }: SignUpFormValues,
    { setStatus, setSubmitting }: FormikHelpers<SignUpFormValues>
  ) => {
    try {
      const token = new AuthToken().get()
      const response = await fetch('/api/v1/auth/register', {
        method: 'post',
        headers: {
          'Content-Type': 'application/json',
          'X-XSRF-TOKEN': csrfToken() ?? '',
          Authorization: token ? `Bearer ${token}` : ''
        },
        body: JSON.stringify({
          firstName,
          lastName,
          email,
          password,
          serviceId,
          roles
        })
      })

      if (!response.ok) {
        throw new Error(`Failed to fetch: ${response.status} - ${response.statusText}`)
      }

      const content: SignUpResponse = await response.json()
      if (content) {
        authToken.set(content.token)
        navigate('/login', { replace: true })
      }
    } catch (error) {
      console.log('signup error: ', error)
      Sentry.captureException(error)
      setStatus('La connexion a échoué. Veuillez vérifier vos identifiants.')
    } finally {
      setSubmitting(false)
    }
  }

  const validateForm = (values: SignUpFormValues) => {
    const errors: Partial<SignUpFormValues> = {}

    if (!values.email) {
      errors.email = "L'adresse email est requise"
    } else if (!validate(values.email)) {
      errors.email = "L'adresse email n'est pas une adresse valide"
    }

    if (!values.password) {
      errors.password = 'Le mot de passe est requis'
    }

    if (!values.firstName) {
      errors.firstName = 'Le prénom est requis'
    }
    if (!values.lastName) {
      errors.firstName = 'Le nom est requis'
    }
    if (!values.roles) {
      errors.roles = 'Le rôle est requis'
    }

    return errors
  }

  return (
    <PageWrapper>
      <FlexboxGrid justify="center" align="middle" style={{ width: '100%' }}>
        <FlexboxGrid.Item colspan={5}>
          <Formik
            initialValues={initialValues}
            onSubmit={handleSubmit}
            validate={validateForm}
            validateOnChange={false}
          >
            {({ isSubmitting, status }) => (
              <Form>
                <Stack direction="column" alignItems="flex-start">
                  <Stack.Item style={{ marginTop: '1rem', marginBottom: '1rem' }}>
                    <h4>Inscription</h4>
                  </Stack.Item>
                  <Stack.Item style={{ marginTop: '1rem', width: '100%' }}>
                    <FormikTextInput
                      name="firstName"
                      label="Prénom"
                      itemType="text"
                      placeholder=""
                      required
                      size={Size.LARGE}
                    />
                  </Stack.Item>
                  <Stack.Item style={{ marginTop: '1rem', width: '100%' }}>
                    <FormikTextInput
                      name="lastName"
                      label="Nom"
                      itemType="text"
                      placeholder=""
                      required
                      size={Size.LARGE}
                    />
                  </Stack.Item>
                  <Stack.Item style={{ marginTop: '1rem', width: '100%' }}>
                    <FormikTextInput
                      name="email"
                      label="Email"
                      itemType="email"
                      placeholder="mail@gouv.fr"
                      required
                      size={Size.LARGE}
                    />
                  </Stack.Item>
                  <Stack.Item style={{ marginTop: '1rem', width: '100%' }}>
                    <FormikTextInput
                      name="password"
                      label="Mot de passe"
                      type="password"
                      placeholder="************"
                      required
                      size={Size.LARGE}
                    />
                  </Stack.Item>{' '}
                  <Stack.Item style={{ marginTop: '1rem', width: '100%' }}>
                    <FormikTextInput
                      name="serviceId"
                      label="ServiceId"
                      type="text"
                      placeholder=""
                      required
                      size={Size.LARGE}
                    />
                  </Stack.Item>
                  <Stack.Item style={{ marginTop: '1rem', width: '100%' }}>
                    <FormikMultiSelect
                      name="roles"
                      label="Roles"
                      options={[
                        { value: RoleType.USER_PAM, label: 'PAM' },
                        { value: RoleType.USER_ULAM, label: 'ULAM' },
                        { value: RoleType.ADMIN, label: 'ADMIN' }
                      ]}
                      required
                      size={Size.LARGE}
                    />
                  </Stack.Item>
                  <Stack.Item style={{ marginTop: '2rem', width: '100%' }} alignSelf="flex-end">
                    <Button
                      accent={Accent.PRIMARY}
                      type="submit"
                      size={Size.LARGE}
                      isFullWidth={true}
                      disabled={isSubmitting}
                    >
                      S'inscrire
                    </Button>
                  </Stack.Item>
                  {status && !isSubmitting && (
                    <Stack.Item
                      style={{
                        marginTop: '1rem',
                        width: '100%',
                        color: 'red',
                        fontSize: '12px'
                      }}
                    >
                      <p>{status}</p>
                    </Stack.Item>
                  )}
                </Stack>
              </Form>
            )}
          </Formik>
        </FlexboxGrid.Item>
      </FlexboxGrid>
    </PageWrapper>
  )
}

export default SignupPage
