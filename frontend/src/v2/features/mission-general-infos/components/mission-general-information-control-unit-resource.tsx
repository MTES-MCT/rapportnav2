import { Accent, Button, FormikEffect, FormikSelect, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import { isEqual } from 'lodash'
import React, { useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { ControlUnitResource } from '../../common/types/control-unit-types.ts'

type ResourceFormInput = { resources: { id?: number }[] } | undefined

interface MissionGeneralInformationControlUnitResourceProps {
  name: string
  fieldFormik: FieldProps<{ id?: number }[]>
  controlUnitResources?: ControlUnitResource[]
}

const MissionGeneralInformationControlUnitResource: React.FC<MissionGeneralInformationControlUnitResourceProps> = ({
  name,
  fieldFormik,
  controlUnitResources
}) => {
  const [initialValues, setInitialValues] = useState<ResourceFormInput>()

  useEffect(() => {
    if (!fieldFormik.field?.value || fieldFormik.field.value.length === 0) {
      setInitialValues({ resources: [{ id: undefined }] })
      return
    }
    setInitialValues({ resources: fieldFormik.field.value })
  }, [fieldFormik])

  const handleSubmit = (input: ResourceFormInput) => {
    const newValue = input?.resources?.filter(t => t.id !== undefined)
    if (!isEqual(newValue, fieldFormik.field.value)) {
      fieldFormik.form.setFieldValue(name, newValue)
    }
  }

  return (
    <>
      {initialValues && (
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
                          key={`resources.${index}.id`}
                          style={{ width: '100%', marginTop: 6 }}
                        >
                          <Stack.Item style={{ width: '90%' }}>
                            <FormikSelect
                              placeholder=""
                              isRequired={true}
                              searchable={true}
                              style={{ width: '100%' }}
                              name={`resources.${index}.id`}
                              label="Moyen(s) utilisé(s)"
                              options={
                                controlUnitResources?.map((resource: any) => ({
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
      )}
    </>
  )
}

export default MissionGeneralInformationControlUnitResource
