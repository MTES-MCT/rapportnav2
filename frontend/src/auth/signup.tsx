import { useNavigate } from 'react-router-dom'
import PageWrapper from '../ui/page-wrapper'
import AuthToken from './token'
import { FlexboxGrid, Stack } from 'rsuite'
import { Accent, Button, FormikTextInput, Size } from '@mtes-mct/monitor-ui'
import { Form, Formik, FormikHelpers } from 'formik'
import { validate } from 'email-validator'
import { csrfToken } from './csrf'

interface SignUpResponse {
  token: string
}

interface SignUpFormValues {
  name: string
  email: string
  password: string
}

const initialValues: SignUpFormValues = {
  name: '',
  email: '',
  password: ''
}

const authToken = new AuthToken()

const SignUp: React.FC = () => {
  const navigate = useNavigate()

  const handleSubmit = async (
    { name, email, password }: SignUpFormValues,
    { setStatus, setSubmitting }: FormikHelpers<SignUpFormValues>
  ) => {
    try {
      const response = await fetch('/api/v1/auth/register', {
        method: 'post',
        headers: {
          'Content-Type': 'application/json',
          'X-XSRF-TOKEN': csrfToken() ?? ''
        },
        body: JSON.stringify({
          name,
          email,
          password
        })
      })

      const content: SignUpResponse = await response.json()
      if (content) {
        authToken.set(content.token)
        navigate('/login', { replace: true })
      }
    } catch (error) {
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

    if (!values.name) {
      errors.name = 'Le nom est requis'
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
                      name="name"
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

export default SignUp
