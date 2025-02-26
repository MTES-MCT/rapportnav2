import { Accent, Button, FormikEffect, FormikSelect, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import { isEmpty, isEqual } from 'lodash'
import { FC, useEffect, useState } from 'react'
import { Divider, Stack } from 'rsuite'
import { Administration } from '../../common/types/control-unit-types.ts'
import { InterMinisterialService } from '../../common/types/mission-types.ts'
import { useMissionGeneralInfosInterService } from '../hooks/use-mission-general-infos-inter-service.tsx'
import MissionGeneralInformationControlUnit from '../ui/mission-general-information-control-unit.tsx'

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
  const [opens, setOpens] = useState<boolean[]>([])
  const [initialValues, setInitialValues] = useState<InterMinisterielFormInput>()
  const { admins, controlUnits } = useMissionGeneralInfosInterService(administrations)

  useEffect(() => {
    if (!fieldFormik.field?.value || fieldFormik.field.value.length === 0) {
      setInitialValues({
        interMinisterialServices: [{ administrationId: undefined, controlUnitId: undefined }]
      })
      return
    }
    setOpens(fieldFormik.field.value.map(() => false))
    setInitialValues({ interMinisterialServices: fieldFormik.field.value })
  }, [fieldFormik])

  const handleSubmit = (input: Record<string, any>) => {
    const interServices: InterMinisterialService[] = input.interMinisterialServices
    const newValue = interServices.filter(t => t.administrationId !== undefined && t.controlUnitId !== undefined)
    if (isEqual(newValue, fieldFormik.field.value) || isEmpty(newValue)) return
    fieldFormik.form.setFieldValue(name, newValue)
  }

  const handleOpen = (index: number) => {
    const newOpens = [...opens]
    newOpens[index] = true
    setOpens(newOpens)
  }
  const handleClose = (index: number) => {
    const newOpens = [...opens]
    newOpens[index] = false
    setOpens(newOpens)
  }

  const handleAdd = (arrayHelpers: FieldArrayRenderProps) => {
    setOpens([...opens, false])
    arrayHelpers.push({ administrationId: undefined, controlUnitId: undefined })
  }

  const handleRemove = (index: number, arrayHelpers: FieldArrayRenderProps) => {
    setOpens([...opens].splice(index, 1))
    arrayHelpers.remove(index)
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
                        return (
                          <div key={`inter-service-${index}`}>
                            {!!index && (
                              <Stack.Item style={{ width: '100%' }}>
                                <Divider style={{ width: '100%', backgroundColor: THEME.color.lightGray }} />
                              </Stack.Item>
                            )}
                            <Stack direction="row" alignItems="flex-start" style={{ width: '100%', marginTop: 6 }}>
                              <Stack.Item style={{ width: '90%' }}>
                                <Stack direction="column" style={{ width: '100%' }}>
                                  <Stack.Item style={{ width: '100%' }}>
                                    <FormikSelect
                                      label={`Administration (${index + 1})`}
                                      isRequired
                                      searchable
                                      onOpen={() => handleOpen(index)}
                                      onClose={() => handleClose(index)}
                                      options={admins.map(admin => ({ value: admin.id, label: admin.name }))}
                                      name={`interMinisterialServices.${index}.administrationId`}
                                    />
                                  </Stack.Item>
                                  <Stack.Item style={{ width: '100%', marginTop: 8 }}>
                                    <MissionGeneralInformationControlUnit
                                      open={!opens[index]}
                                      label={`Unité (${index + 1})`}
                                      isDisabled={!service.administrationId}
                                      controlUnits={controlUnits.filter(
                                        c => c.administrationId === service.administrationId
                                      )}
                                      name={`interMinisterialServices.${index}.controlUnitId`}
                                    />
                                  </Stack.Item>
                                </Stack>
                              </Stack.Item>
                              <Stack.Item style={{ paddingLeft: 5, marginTop: 22 }}>
                                <IconButton
                                  size={Size.NORMAL}
                                  Icon={Icon.Delete}
                                  disabled={index === 0}
                                  accent={Accent.TERTIARY}
                                  role="delete-interMinisterialServices"
                                  onClick={() => handleRemove(index, arrayHelpers)}
                                  style={{ border: `1px solid ${THEME.color.charcoal}` }}
                                />
                              </Stack.Item>
                            </Stack>
                          </div>
                        )
                      })}
                    </Stack.Item>
                    <Stack.Item style={{ width: '100%', marginTop: 16 }}>
                      <Button
                        Icon={Icon.Plus}
                        size={Size.SMALL}
                        isFullWidth
                        accent={Accent.SECONDARY}
                        onClick={() => handleAdd(arrayHelpers)}
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
