import { useEffect } from 'react'
import { resetFormValidation, setFormValidation } from '../../../store/slices/form-validation-reducer'

export function useFormValidationReporter() {
  const handleErrors = (errors: Record<string, any>) => {
    setFormValidation(Object.keys(errors).length === 0)
  }

  useEffect(() => {
    return () => resetFormValidation()
  }, [])

  return { onFormError: handleErrors }
}
