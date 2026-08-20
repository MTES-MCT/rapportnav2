import {
  Accent,
  Button,
  FormikEffect,
  FormikNumberInput,
  FormikSelect,
  Icon,
  IconButton,
  Size,
  THEME
} from '@mtes-mct/monitor-ui'
import { FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import { isEqual } from 'lodash'
import React, { useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import {
  ControlUnitResource,
  ControlUnitResourceType,
  getResourceUsageKind
} from '../../common/types/control-unit-types.ts'
import { useResourceUsage } from '../../common/hooks/use-resource-usage.tsx'
import Text from '@common/components/ui/text.tsx'

type ResourceRowInput = { id?: number; type?: ControlUnitResourceType; nbKms?: number; nbEngineHours?: number }
type ResourceFormInput = { resources: ResourceRowInput[] } | undefined

interface MissionGeneralInformationControlUnitResourceProps {
  name: string
  disabled?: boolean
  fieldFormik: FieldProps<ControlUnitResource[]>
  controlUnitResources?: ControlUnitResource[]
  isMissionFinished: boolean
}

const MissionGeneralInformationControlUnitResource: React.FC<MissionGeneralInformationControlUnitResourceProps> = ({
  name,
  disabled,
  fieldFormik,
  controlUnitResources,
  isMissionFinished
}) => {
  const [initialValues, setInitialValues] = useState<ResourceFormInput>()

  // Re-seed the internal form only when the SET of selected resources changes (add/remove/select) or the
  // dropdown loads — NOT on every parent render. `fieldFormik` is a new object each render, so depending on
  // it (with enableReinitialize) would reset the form mid-typing and wipe multi-keystroke values like nbKms.
  const currentResources = Array.isArray(fieldFormik.field?.value) ? fieldFormik.field.value : undefined
  const resourceIdsKey = (currentResources ?? []).map(v => v.id).join(',')

  useEffect(() => {
    if (!controlUnitResources) return
    if (!currentResources || currentResources.length === 0) {
      setInitialValues({ resources: [{ id: undefined }] })
      return
    }
    setInitialValues({
      resources: currentResources.map(v => ({
        id: v.id,
        type: v.type,
        nbKms: v.nbKms,
        nbEngineHours: v.nbEngineHours
      }))
    })
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [resourceIdsKey, controlUnitResources])

  const getNewValue = (input: ResourceFormInput) => {
    return (input?.resources ?? [])
      .filter(row => Boolean(row?.id))
      .map(row => {
        const resource = controlUnitResources?.find(cr => cr.id === row.id)
        if (!resource) return undefined
        const kind = getResourceUsageKind(resource.type)
        // Keep the resource type in the payload (backend needs it to decide the usage column),
        // and only carry the value matching the resource's kind.
        return {
          id: resource.id,
          name: resource.name,
          controlUnitId: resource.controlUnitId,
          type: resource.type,
          nbKms: kind === 'KM' ? row.nbKms : undefined,
          nbEngineHours: kind === 'ENGINE_HOURS' ? row.nbEngineHours : undefined
        }
      })
      .filter(Boolean) as ControlUnitResource[]
  }

  const handleSubmit = (input: ResourceFormInput) => {
    const newValue = getNewValue(input)
    if (isEqual(newValue, fieldFormik.field.value)) return
    fieldFormik.form.setFieldValue(name, newValue)
  }

  // The usage inputs live in this inner Formik (not the parent form that carries the schema), so they need
  // their own validation to get a `meta.error` and show the red required border, like every other form.
  const { validationSchema } = useResourceUsage(isMissionFinished)

  return (
    <>
      {initialValues && (
        <>
          <Formik
            initialValues={initialValues}
            onSubmit={handleSubmit}
            validationSchema={validationSchema}
            validateOnMount
            enableReinitialize
          >
            {({ values }) => (
              <>
                <FormikEffect onChange={newValues => handleSubmit(newValues as ResourceFormInput)} />
                <FieldArray name="resources">
                  {(arrayHelpers: FieldArrayRenderProps) => (
                    <Stack direction="column" style={{ width: '100%' }}>
                      <Stack.Item style={{ width: '100%' }}>
                        {values.resources.map((value, index) => {
                          const selectedType = controlUnitResources?.find(r => r.id === value.id)?.type
                          const usageKind = getResourceUsageKind(selectedType)
                          return (
                            <Stack
                              direction="row"
                              alignItems="flex-end"
                              key={`resources.${index}.id`}
                              style={{ width: '100%', marginTop: 6 }}
                            >
                              <Stack.Item style={{ width: usageKind === 'NONE' ? '100%' : '55%' }}>
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
                              {usageKind !== 'NONE' && (
                                <Stack.Item style={{ width: '45%', paddingLeft: 8 }}>
                                  <FormikNumberInput
                                    disabled={disabled}
                                    isRequired={isMissionFinished}
                                    name={
                                      usageKind === 'KM'
                                        ? `resources.${index}.nbKms`
                                        : `resources.${index}.nbEngineHours`
                                    }
                                    label={usageKind === 'KM' ? 'Km parcourus' : "Nb d'heures moteur"}
                                  />
                                </Stack.Item>
                              )}
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
                          )
                        })}
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
          {isMissionFinished &&
            !fieldFormik.field.value?.length &&
            fieldFormik.form.values.isResourcesNotUsed !== true && (
              <Text as={'h2'} color={THEME.color.maximumRed} style={{ margin: '1rem 0' }}>
                Veuillez renseigner la liste de ressources participant à la mission{' '}
              </Text>
            )}
        </>
      )}
    </>
  )
}

export default MissionGeneralInformationControlUnitResource
