import { Accent, Button, FormikEffect, FormikSelect, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import { isEqual } from 'lodash'
import { FC, useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { Administration } from '../../common/types/control-unit-types.ts'
import { InterMinisterialService } from '../../common/types/mission-types.ts'

type InterMinisterielFormInput =
  | { interMinisterialServices: { administrationId?: number; controlUnitId?: number }[] }
  | undefined

interface MissionGeneralInformationInterServiceProps {
  name: string
  fieldFormik: FieldProps
  administrations: Administration[]
}

const MissionGeneralInformationInterService: FC<MissionGeneralInformationInterServiceProps> = ({
  name,
  fieldFormik,
  administrations
}) => {
  const [initialValues, setInitialValues] = useState<InterMinisterielFormInput>()

  useEffect(() => {
    if (!fieldFormik.field?.value || fieldFormik.field.value.length === 0) {
      setInitialValues({
        interMinisterialServices: [{ administrationId: undefined, controlUnitId: undefined }]
      })
      return
    }
    setInitialValues({ interMinisterialServices: fieldFormik.field.value })
  }, [fieldFormik])

  const handleSubmit = (input: Record<string, any>) => {
    const newValue = input.interMinisterialServices.filter(
      (t: InterMinisterialService) => t.administrationId !== undefined && t.controlUnitId !== undefined
    )
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
              <FormikEffect onChange={newValues => handleSubmit(newValues)} />
              <FieldArray name="interMinisterialServices">
                {(arrayHelpers: FieldArrayRenderProps) => (
                  <Stack direction="column" style={{ width: '100%' }}>
                    <Stack.Item style={{ width: '100%' }}>
                      {values.interMinisterialServices?.map((service, index) => {
                        const selectedAdmin = administrations?.find(admin => admin.id === service.administrationId)
                        const unitOptions =
                          selectedAdmin?.controlUnits?.map(unit => ({ value: unit.id, label: unit.name })) || []

                        return (
                          <Stack
                            direction="row"
                            alignItems="flex-start"
                            key={index}
                            style={{ width: '100%', marginTop: 6 }}
                          >
                            <Stack.Item style={{ width: '90%' }}>
                              <Stack direction="column" style={{ width: '100%' }}>
                                <Stack.Item style={{ width: '100%' }}>
                                  <FormikSelect
                                    label={`Administration (${index + 1})`}
                                    isRequired
                                    searchable
                                    options={
                                      administrations
                                        ? administrations.map(admin => ({ value: admin.id, label: admin.name }))
                                        : []
                                    }
                                    name={`interMinisterialServices.${index}.administrationId`}
                                  />
                                </Stack.Item>
                                <Stack.Item style={{ width: '100%', marginTop: 12 }}>
                                  <FormikSelect
                                    label={`Unité (${index + 1})`}
                                    isRequired
                                    searchable
                                    options={unitOptions}
                                    name={`interMinisterialServices.${index}.controlUnitId`}
                                    disabled={!service.administrationId}
                                  />
                                </Stack.Item>
                              </Stack>
                            </Stack.Item>
                            <Stack.Item style={{ paddingLeft: 5, marginTop: 22 }}>
                              <IconButton
                                disabled={index === 0}
                                role="delete-interMinisterialServices"
                                size={Size.NORMAL}
                                Icon={Icon.Delete}
                                accent={Accent.TERTIARY}
                                style={{ border: `1px solid ${THEME.color.charcoal}` }}
                                onClick={() => arrayHelpers.remove(index)}
                              />
                            </Stack.Item>
                          </Stack>
                        )
                      })}
                    </Stack.Item>
                    <Stack.Item style={{ width: '100%', marginTop: 16 }}>
                      <Button
                        Icon={Icon.Plus}
                        size={Size.SMALL}
                        isFullWidth
                        accent={Accent.SECONDARY}
                        onClick={() => arrayHelpers.push({ administrationId: undefined, controlUnitId: undefined })}
                        disabled={values.interMinisterialServices.some(
                          service => !service.administrationId || !service.controlUnitId
                        )}
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
      )}
    </>
  )
}

export default MissionGeneralInformationInterService
