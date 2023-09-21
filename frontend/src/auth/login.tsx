import { useNavigate } from 'react-router-dom'
import { httpClient } from '../http-client'
import PageWrapper from '../ui/page-wrapper'
import AuthToken from './token'
import { FlexboxGrid, Stack } from 'rsuite'
import { Accent, Button, FormikTextInput, Size } from '@mtes-mct/monitor-ui'
import { Form, Formik, FormikHelpers } from 'formik'
import { validate } from 'email-validator'

interface LoginResponse {
  token: string
}

interface LoginFormValues {
  email: string
  password: string
}

const initialValues: LoginFormValues = {
  email: '',
  password: ''
}

const authToken = new AuthToken()

const Login: React.FC = () => {
  const navigate = useNavigate()

  const handleSubmit = async (
    { email, password }: LoginFormValues,
    { setStatus, setSubmitting }: FormikHelpers<LoginFormValues>
  ) => {
    try {
      const response = await httpClient.post('v1/auth/login', {
        body: JSON.stringify({
          email,
          password
        })
      })

      const content: LoginResponse = await response.json()
      if (content) {
        authToken.set(content.token)
        navigate('/', { replace: true })
      }
    } catch (error) {
      setStatus('La connexion a échoué. Veuillez vérifier vos identifiants.')
    } finally {
      setSubmitting(false)
    }
  }

  const validateForm = (values: LoginFormValues) => {
    const errors: Partial<LoginFormValues> = {}

    if (!values.email) {
      errors.email = "L'adresse email est requise"
    } else if (!validate(values.email)) {
      errors.email = "L'adresse email n'est pas une adresse valide"
    }

    if (!values.password) {
      errors.password = 'Le mot de passe est requis'
    }

    return errors
  }

  return (
    <PageWrapper>
      <FlexboxGrid justify="center" align="middle" style={{ width: '100%' }}>
        <FlexboxGrid.Item style={{ marginTop: '4rem' }} colspan={5}>
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
                    <h4>Connexion</h4>
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
                      itemType="password"
                      placeholder="************"
                      required
                      size={Size.LARGE}
                    />
                  </Stack.Item>
                  <Stack.Item style={{ marginTop: '2rem', width: '100%' }} alignSelf="flex-end">
                    <Button accent={Accent.PRIMARY} type="submit" size={Size.LARGE} isFullWidth disabled={isSubmitting}>
                      Se connecter
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

export default Login
