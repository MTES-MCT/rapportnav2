import { FC, useState } from 'react'
import AuthToken from '@features/auth/utils/token.ts'
import { Form, Formik, FormikHelpers } from 'formik'
import { validate } from 'email-validator'
import { csrfToken } from '@features/auth/utils/csrf.ts'
import { trim } from 'lodash'

export const LOGIN_ENDPOINT = '/api/v1/auth/login'

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

const LoginForm: FC = () => {
  const [showPassword, setShowPassword] = useState(false)

  const handleSubmit = async (
    { email, password }: LoginFormValues,
    { setStatus, setSubmitting }: FormikHelpers<LoginFormValues>
  ) => {
    try {
      const response = await fetch(`${window.location.origin}${LOGIN_ENDPOINT}`, {
        method: 'post',
        headers: {
          'Content-Type': 'application/json',
          'X-XSRF-TOKEN': csrfToken() ?? ''
        },
        body: JSON.stringify({
          email: trim(email).toLowerCase(),
          password
        })
      })

      if (!response.ok) {
        throw new Error(`Failed to fetch: ${response.status} - ${response.statusText}`)
      }

      const content: LoginResponse = await response.json()
      if (content) {
        authToken.set(content.token)
        // Full page reload: drops the lazily-loaded DSFR chunk/CSS so it never
        // leaks into the authenticated app (which uses rsuite / monitor-ui).
        window.location.assign('/')
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
      errors.password = 'Le mot de passe est requis' // NOSONAR not a hard-coded password
    }

    return errors
  }

  return (
    <Formik initialValues={initialValues} onSubmit={handleSubmit} validate={validateForm} validateOnChange={false}>
      {({ values, errors, isSubmitting, status, handleChange }) => {
        // Match the original behaviour: errors only surface after a submit
        // attempt. Fields are not wired to Formik's onBlur and
        // validateOnChange is false, so `errors` is only populated on submit.
        const emailError = errors.email
        const passwordError = errors.password
        return (
          <Form id="login">
            <fieldset
              className="fr-fieldset"
              id="login-fieldset"
              aria-labelledby="login-fieldset-legend login-fieldset-messages"
            >
              <legend className="fr-fieldset__legend" id="login-fieldset-legend">
                <h2>Se connecter avec son compte</h2>
              </legend>

              <div className="fr-fieldset__element">
                <fieldset className="fr-fieldset" id="credentials" aria-labelledby="credentials-messages">
                  <div className="fr-fieldset__element">
                    <span className="fr-hint-text">Sauf mention contraire, tous les champs sont obligatoires.</span>
                  </div>

                  <div className="fr-fieldset__element">
                    <div className={`fr-input-group${emailError ? ' fr-input-group--error' : ''}`}>
                      <label className="fr-label" htmlFor="email">
                        Email
                        <span className="fr-hint-text">Format attendu : mail@gouv.fr</span>
                      </label>
                      <input
                        className="fr-input"
                        autoComplete="username"
                        aria-required="true"
                        aria-invalid={emailError ? true : undefined}
                        aria-describedby="email-messages"
                        name="email"
                        id="email"
                        type="text"
                        placeholder={'mail@gouv.fr'}
                        inputMode="email"
                        value={values.email}
                        onChange={handleChange}
                      />
                      <div className="fr-messages-group" id="email-messages" aria-live="polite">
                        {emailError && <p className="fr-message fr-message--error">{emailError}</p>}
                      </div>
                    </div>
                  </div>

                  <div className="fr-fieldset__element">
                    <div className={`fr-password${passwordError ? ' fr-input-group--error' : ''}`} id="password">
                      <label className="fr-label" htmlFor="password-input">
                        Mot de passe
                      </label>
                      <div className="fr-input-wrap">
                        <input
                          className="fr-password__input fr-input"
                          aria-describedby="password-input-messages"
                          aria-required="true"
                          aria-invalid={passwordError ? true : undefined}
                          name="password"
                          autoComplete="current-password"
                          id="password-input"
                          type={showPassword ? 'text' : 'password'}
                          value={values.password}
                          onChange={handleChange}
                          placeholder="************"
                        />
                      </div>
                      <div className="fr-messages-group" id="password-input-messages" aria-live="polite">
                        {passwordError && <p className="fr-message fr-message--error">{passwordError}</p>}
                      </div>
                      <div className="fr-password__checkbox fr-checkbox-group fr-checkbox-group--sm">
                        <input
                          aria-label="Afficher le mot de passe"
                          id="password-show"
                          type="checkbox"
                          checked={showPassword}
                          onChange={() => setShowPassword(prev => !prev)}
                        />
                        <label className="fr-password__checkbox fr-label" htmlFor="password-show">
                          Afficher
                        </label>
                      </div>
                    </div>
                  </div>

                  <div className="fr-messages-group" id="credentials-messages" aria-live="polite" />
                </fieldset>
              </div>

              {status && !isSubmitting && (
                <div className="fr-fieldset__element">
                  <div className="fr-alert fr-alert--error fr-alert--sm" role="alert">
                    <p>{status}</p>
                  </div>
                </div>
              )}

              <div className="fr-fieldset__element">
                <ul className="fr-btns-group">
                  <li>
                    <button className="fr-mt-2v fr-btn" type="submit" disabled={isSubmitting}>
                      Se connecter
                    </button>
                  </li>
                </ul>
              </div>

              <div className="fr-messages-group" id="login-fieldset-messages" aria-live="polite" />
            </fieldset>
          </Form>
        )
      }}
    </Formik>
  )
}

export default LoginForm
