import { Accent, Button, FormikEffect, FormikSelect, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import { isEqual } from 'lodash'
import React, { useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import useControlUnitResourcesQuery from '../../services/use-control-unit-resources.tsx'

type ResourceFormInput = { resources: { data?: number }[] }

interface MissionGeneralInformationControlUnitRessourceProps {
  name: string
  fieldFormik: FieldProps
}

const MissionGeneralInformationControlUnitRessource: React.FC<MissionGeneralInformationControlUnitRessourceProps> = ({
  name,
  fieldFormik
}) => {
  const { data: resources } = useControlUnitResourcesQuery()
  const [initialValues, setInitialValues] = useState<ResourceFormInput>({ resources: [{}] })

  const fromFieldToInput = (value: string[]) => ({ resources: value?.map(data => ({ data })) })
  const fromInputToFieldValue = (input: ResourceFormInput) =>
    input.resources.map(t => t.data).filter(t => t !== undefined)

  useEffect(() => {
    if (fieldFormik.field.value?.length === 0) return
    const input = fromFieldToInput(fieldFormik.field.value)
    setInitialValues(input)
  }, [fieldFormik])

  const handleSubmit = (input: ResourceFormInput) => {
    const newValue = fromInputToFieldValue(input)
    if (isEqual(newValue, fieldFormik.field.value)) return
    fieldFormik.form.setFieldValue(name, newValue)
  }

  return (
    <Formik initialValues={initialValues} onSubmit={handleSubmit} enableReinitialize>
      {({ values }) => (
        <>
          <FormikEffect onChange={newValues => handleSubmit(newValues as ResourceFormInput)} />
          <FieldArray name="resources">
            {(arrayHelpers: FieldArrayRenderProps) => (
              <Stack direction="column" style={{ width: '100%' }}>
                <Stack.Item style={{ width: '100%' }}>
                  {values.resources.map((value, index) => (
                    <Stack
                      direction="row"
                      alignItems="flex-end"
                      key={`resources.${index}`}
                      style={{ width: '100%', marginTop: 6 }}
                    >
                      <Stack.Item style={{ width: '90%' }}>
                        <FormikSelect
                          placeholder=""
                          isRequired={true}
                          searchable={true}
                          style={{ width: '100%' }}
                          name={`resources.${index}.data`}
                          label="Moyen(s) utilisé(s)"
                          options={
                            resources?.map((resource: any) => ({
                              value: resource.id,
                              label: `${resource.name}`
                            })) ?? []
                          }
                        />
                      </Stack.Item>
                      <Stack.Item style={{ paddingLeft: 5 }}>
                        <IconButton
                          disabled={index === 0}
                          role="delete-resources"
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
                <Stack.Item style={{ width: '100%', marginTop: 8 }}>
                  <Button
                    Icon={Icon.Plus}
                    size={Size.SMALL}
                    isFullWidth={true}
                    accent={Accent.SECONDARY}
                    onClick={() => arrayHelpers.push({})}
                  >
                    Ajouter un moyen
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

export default MissionGeneralInformationControlUnitRessource
