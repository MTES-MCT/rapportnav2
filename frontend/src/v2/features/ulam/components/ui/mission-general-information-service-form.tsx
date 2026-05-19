import { FormikSelect } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, Form, Formik } from 'formik'
import React, { useMemo } from 'react'
import { Stack } from 'rsuite'

type ServiceForm = { service: { admin: string; unit: string } }

interface MissionGeneralInformationServiceFormProps {
  service?: { admin: string; unit: string }
  handleEdit: (service: { admin: string; unit: string }) => void
}

const MissionGeneralInformationServiceForm: React.FC<MissionGeneralInformationServiceFormProps> = ({
  service,
  handleEdit
}) => {
  //TODO: get ser
  const initValue = useMemo<ServiceForm | undefined>(() => {
    if (!service) return undefined
    return { service }
  }, [service])

  const handleSubmit = async ({ service }: ServiceForm) => handleEdit(service)

  return (
    <>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} enableReinitialize={true}>
          <Form>
            <Stack direction="column" style={{ width: '100%' }}>
              <Stack.Item style={{ width: '100%' }}>
                <Field name="service.admin">
                  {({ field, form }: FieldProps<ServiceForm>) => (
                    <FormikSelect
                      {...field}
                      label="Administration"
                      placeholder=""
                      isRequired={true}
                      searchable={true}
                      options={[]}
                    />
                  )}
                </Field>
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <Field name="service.unit">
                  {({ field, form }: FieldProps<ServiceForm>) => (
                    <FormikSelect
                      {...field}
                      label="Unité"
                      placeholder=""
                      isRequired={true}
                      searchable={true}
                      options={[]}
                    />
                  )}
                </Field>
              </Stack.Item>
            </Stack>
          </Form>
        </Formik>
      )}
    </>
  )
}

export default MissionGeneralInformationServiceForm
