import { Accent, Button, FormikEffect, FormikSelect, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import { isEqual } from 'lodash'
import React, { useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { ControlUnitResource } from '../../common/types/control-unit-types.ts'

type ResourceFormInput = { resources: { id?: number }[] } | undefined

interface MissionGeneralInformationControlUnitResourceProps {
  name: string
  disabled?: boolean
  fieldFormik: FieldProps<ControlUnitResource[]>
  controlUnitResources?: ControlUnitResource[]
}

const MissionGeneralInformationControlUnitResource: React.FC<MissionGeneralInformationControlUnitResourceProps> = ({
  name,
  disabled,
  fieldFormik,
  controlUnitResources
}) => {
  const [initialValues, setInitialValues] = useState<ResourceFormInput>()

  useEffect(() => {
    if (!controlUnitResources) return
    if (!fieldFormik.field?.value || fieldFormik.field.value.length === 0) {
      setInitialValues({ resources: [{ id: undefined }] })
      return
    }
    setInitialValues({ resources: fieldFormik.field.value?.map(v => ({ id: v.id })) })
  }, [fieldFormik, controlUnitResources])

  const getNewValue = (input: ResourceFormInput) => {
    const newIds = input?.resources?.map(resource => resource?.id)?.filter(Boolean) ?? []
    return controlUnitResources
      ?.filter(resource => newIds?.includes(resource.id)) //TODO: replace by reduce
      .map(resource => ({ id: resource.id, name: resource.name, controlUnitId: resource.controlUnitId }))
  }

  const handleSubmit = (input: ResourceFormInput) => {
    const newValue = getNewValue(input)
    if (isEqual(newValue, fieldFormik.field.value)) return
    fieldFormik.form.setFieldValue(name, newValue)
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
                          <Stack.Item style={{ width: '100%' }}>
                            <FormikSelect
                              isRequired
                              searchable
                              disabled={disabled}
                              style={{ width: '100%' }}
                              name={`resources.${index}.id`}
                              label="Moyen(s) utilisé(s)"
                              options={
                                controlUnitResources?.map((resource: ControlUnitResource) => ({
                                  value: resource.id!!,
                                  label: `${resource.name}`
                                })) || []
                              }
                              disabledItemValues={values.resources.map(resource => resource.id).filter(Boolean)}
                            />
                          </Stack.Item>
                          <Stack.Item style={{ paddingLeft: 5 }}>
                            <IconButton
                              role="delete-resources"
                              size={Size.NORMAL}
                              Icon={Icon.Delete}
                              accent={Accent.TERTIARY}
                              disabled={index === 0 || disabled}
                              onClick={() => arrayHelpers.remove(index)}
                              style={{ border: `1px solid ${THEME.color.charcoal}` }}
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
                        disabled={disabled}
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
