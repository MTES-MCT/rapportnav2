import { FormikEffect, FormikMultiRadio, FormikSelect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Field, FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { string } from 'yup'
import { FormikDateRangePicker } from '../../../common/components/ui/formik-date-range-picker.tsx'
import { FormikSearchEstablishment } from '../../../common/components/ui/formik-search-establishment.tsx'
import { useOnlineManager } from '../../../common/hooks/use-online-manager'
import { useSector } from '../../../common/hooks/use-sector.tsx'
import { MissionAction } from '../../../common/types/mission-action'
import MissionTargetList from '../../../mission-target/components/elements/mission-target-list.tsx'
import MissionTargetNew from '../../../mission-target/components/elements/mission-target-new'
import { useTarget } from '../../../mission-target/hooks/use-target'
import { useMissionActionGenericControl } from '../../hooks/use-mission-action-generic-control'
import { ActionEnvControlInput } from '../../types/action-type'
import MissionActionDivingOperation from '../ui/mission-action-diving-operation.tsx'
import MissionActionIncidentDonwload from '../ui/mission-action-incident-download.tsx'

const MissionActionItemSectorControl: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const schema = {
    resourceId: string().required(),
    resourceType: string().required()
  }

  const { controlTypes } = useTarget()
  const { isOnline } = useOnlineManager()
  const { sectorTypeOptions, getSectionEtablishmentTypeOptions } = useSector()
  const { initValue, handleSubmit } = useMissionActionGenericControl(action, onChange, schema)

  return (
    <form style={{ width: '100%' }}>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} validateOnChange={true} enableReinitialize={true}>
          {({ values }) => (
            <>
              <FormikEffect onChange={nextValues => handleSubmit(nextValues as ActionEnvControlInput)} />
              <Stack
                direction="column"
                spacing="2rem"
                alignItems="flex-start"
                style={{ width: '100%' }}
                data-testid={'action-generic-control'}
              >
                <Stack.Item grow={1}>
                  <Field name="dates">
                    {(field: FieldProps<Date[]>) => (
                      <FormikDateRangePicker
                        label=""
                        name="dates"
                        isLight={true}
                        fieldFormik={field}
                        disabled={!isOnline}
                        title={
                          isOnline
                            ? ''
                            : "Non disponible hors ligne, il est nécessaire d'être synchronisé avec les centres pour saisir/modifier cette donnée."
                        }
                      />
                    )}
                  </Field>
                </Stack.Item>

                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction="column" spacing={'1rem'} alignItems="flex-start">
                    <Stack.Item style={{ width: '100%' }}>
                      <FormikMultiRadio
                        label=""
                        name="sectorType"
                        options={sectorTypeOptions}
                        isErrorMessageHidden={true}
                      />
                    </Stack.Item>
                    <Stack.Item style={{ width: '70%' }}>
                      <FormikSelect
                        name="sectorEstablishmentType"
                        label="Type d'établissement"
                        isLight={true}
                        isRequired={true}
                        isErrorMessageHidden={true}
                        options={getSectionEtablishmentTypeOptions(values.sectorType)}
                      />
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Field name="siren">
                    {(field: FieldProps<string>) => (
                      <FormikSearchEstablishment
                        name="siren"
                        isLight={true}
                        fieldFormik={field}
                        label="Nom de l'etablissement"
                      />
                    )}
                  </Field>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FieldArray name="targets">
                    {(fieldArray: FieldArrayRenderProps) => (
                      <MissionTargetNew
                        isDisabled={false} //TODO: how many target max we can have?
                        actionId={action.id}
                        fieldArray={fieldArray}
                        availableControlTypes={controlTypes}
                      />
                    )}
                  </FieldArray>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FieldArray name="targets">
                    {(fieldArray: FieldArrayRenderProps) => (
                      <MissionTargetList
                        name="targets"
                        fieldArray={fieldArray}
                        actionNumberOfControls={0}
                        controlsToComplete={[]}
                        availableControlTypes={controlTypes}
                      />
                    )}
                  </FieldArray>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FormikTextarea
                    isLight={true}
                    name="observationsByUnit"
                    data-testid="observationsByUnit"
                    label="Observation de l'unité sur le contrôle"
                  />
                  <MissionActionIncidentDonwload />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <MissionActionDivingOperation />
                </Stack.Item>
              </Stack>
            </>
          )}
        </Formik>
      )}
    </form>
  )
}
export default MissionActionItemSectorControl
