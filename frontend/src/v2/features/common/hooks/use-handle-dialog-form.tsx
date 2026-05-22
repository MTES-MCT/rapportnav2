import { FormikErrors, FormikProps } from 'formik'
import { useEffect } from 'react'
import { ObjectSchema } from 'yup'

type UseHandleDialogFormProps = {
  schema: ObjectSchema<any>
  formik: FormikProps<any>
}

const useHandleDialogForm = ({ schema, formik }: UseHandleDialogFormProps) => {
  useEffect(() => {
    schema
      .validate(formik.values, { abortEarly: false })
      .then(() => formik.setErrors({}))
      .catch(e => {
        const errors: FormikErrors<any> = {}
        e.inner.forEach((error: any) => {
          errors[error.path] = error.message
        })
        formik.setErrors(errors)
      })
  }, [formik.values, schema])

  return {}
}

export default useHandleDialogForm
