import { Accent, Button, FormikEffect, FormikSelect, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import { isEqual } from 'lodash'
import React, { useEffect, useState } from 'react'
import { Stack } from 'rsuite'

type Service = { admin?: string; unit?: string }
type ServiceFormInput = { services: Service[] }

interface MissionGeneralInformationServiceUlamProps {
  name: string
  fieldFormik: FieldProps<any>
}

const MissionGeneralInformationServiceUlam: React.FC<MissionGeneralInformationServiceUlamProps> = ({
  name,
  fieldFormik
}) => {
  const [initialValues, setInitialValues] = useState<ServiceFormInput>({
    services: [{ admin: undefined, unit: undefined }]
  })

  const fromFieldToInput = (value: Service[]) => ({ services: value })

  const fromInputToFieldValue = (input: ServiceFormInput) =>
    input.services.filter(t => t.admin !== undefined && t.unit !== undefined)

  useEffect(() => {
    if (!fieldFormik.field?.value) return //TODO: a enlever quand on aura une array vide
    if (fieldFormik.field?.value?.length === 0) return
    const input = fromFieldToInput(fieldFormik.field.value)
    setInitialValues(input)
  }, [fieldFormik])

  const handleSubmit = (input: ServiceFormInput) => {
    const newValue = fromInputToFieldValue(input)
    if (isEqual(newValue, fieldFormik.field.value)) return
    console.log(newValue)
    fieldFormik.form.setFieldValue(name, newValue)
  }

  return (
    <Formik initialValues={initialValues} onSubmit={handleSubmit} enableReinitialize>
      {({ values }) => (
        <>
          <FormikEffect onChange={newValues => handleSubmit(newValues as ServiceFormInput)} />
          <FieldArray name="services">
            {(arrayHelpers: FieldArrayRenderProps) => (
              <Stack direction="column" style={{ width: '100%' }}>
                <Stack.Item style={{ width: '100%' }}>
                  {values.services?.map((service: any, index: number) => (
                    <Stack
                      direction="row"
                      alignItems="flex-start"
                      key={`services.${index}`}
                      style={{ width: '100%', marginTop: 6 }}
                    >
                      <Stack.Item style={{ width: '90%' }}>
                        <Stack direction="column" style={{ width: '100%' }}>
                          <Stack.Item style={{ width: '100%' }}>
                            <FormikSelect
                              label={`Administration (${index + 1})`}
                              placeholder=""
                              isRequired={true}
                              searchable={true}
                              options={[]}
                              name={`services.${index}.admin`}
                            />
                          </Stack.Item>
                          <Stack.Item style={{ width: '100%', marginTop: 12 }}>
                            <FormikSelect
                              label={`Unité (${index + 1})`}
                              placeholder=""
                              isRequired={true}
                              searchable={true}
                              options={[]}
                              name={`services.${index}.unit`}
                            />
                          </Stack.Item>
                        </Stack>
                      </Stack.Item>
                      <Stack.Item style={{ paddingLeft: 5, marginTop: 22 }}>
                        <IconButton
                          role="delete-services"
                          size={Size.NORMAL}
                          Icon={Icon.Delete}
                          accent={Accent.TERTIARY}
                          style={{ border: `1px solid ${THEME.color.charcoal}` }}
                          onClick={() => arrayHelpers.remove(index)}
                        />
                      </Stack.Item>
                    </Stack>
                  ))}
                </Stack.Item>

                <Stack.Item style={{ width: '100%', marginTop: 16 }}>
                  <Button
                    Icon={Icon.Plus}
                    size={Size.SMALL}
                    isFullWidth={true}
                    accent={Accent.SECONDARY}
                    onClick={() => arrayHelpers.push({})}
                  >
                    Ajouter une administration
                  </Button>
                </Stack.Item>
              </Stack>
            )}
          </FieldArray>
        </>
      )}
    </Formik>
  )
}

export default MissionGeneralInformationServiceUlam
