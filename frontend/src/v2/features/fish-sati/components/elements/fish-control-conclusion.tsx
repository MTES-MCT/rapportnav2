import { FormikCheckbox, Label, Message, THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldArray, FieldArrayRenderProps, FieldProps, useFormikContext } from 'formik'
import { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import { FormikTextAreaInput } from '../../../common/components/ui/formik-textarea-input.tsx'
import { Target, TargetType } from '../../../common/types/target-types.ts'
import MissionActionDivingOperation from '../../../mission-action/components/ui/mission-action-diving-operation.tsx'
import MissionActionIncidentDonwload from '../../../mission-action/components/ui/mission-action-incident-download.tsx'
import { ActionFishControlInput } from '../../../mission-action/types/action-type.ts'
import FishControlSeizureSection from '../../../mission-control/components/ui/fish-control-seizure-section.tsx'
import InfractionFishSummary from '../../../mission-infraction/components/ui/infraction-fish-summary.tsx'
import TargetItemDefault from '../../../mission-target/components/elements/target-item-default.tsx'
import { useTarget } from '../../../mission-target/hooks/use-target.tsx'
import InspectorList from '../ui/inpector-list.tsx'
import InspectorPrincipal from '../ui/inspector-item-principal.tsx'
import ResourceForm from '../ui/resource-form.tsx'

interface FishControlConclusionProps {
  values: ActionFishControlInput
}

const FishControlConclusion: FC<FishControlConclusionProps> = ({ values }) => {
  const showCheckbox = false // TODO: remove when MonitorFish dev is done
  const { defaultControlTypes } = useTarget()
  const { setFieldValue } = useFormikContext<ActionFishControlInput>()

  return (
    <Stack direction="column" spacing="0.5rem" alignItems="flex-start" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <ResourceForm
          resource={values?.sati?.resource}
          onChange={resource => setFieldValue('sati.resource', resource)}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <InspectorPrincipal
          inspector={values?.sati?.principalInspector}
          onChange={inspector => setFieldValue('sati.principalInspector', inspector)}
        />
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        <FieldArray name="sati.otherInspectors">
          {(fieldArray: FieldArrayRenderProps) => (
            <InspectorList fieldArray={fieldArray} inspectors={values?.sati?.otherInspectors} />
          )}
        </FieldArray>
      </Stack.Item>

      <Stack.Item style={{ width: '100%' }}>
        <Label>Conclusions</Label>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="column" spacing="1rem" style={{ width: '100%', backgroundColor: 'white', padding: 16 }}>
          <Stack.Item style={{ width: '100%' }}>
            <InfractionFishSummary infractions={values?.fishInfractions ?? []} />
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Field name={`targets[0]`}>
              {(fieldFormik: FieldProps<Target>) => (
                <TargetItemDefault
                  name={`targets[0]`}
                  message={
                    <Message level="WARNING">
                      Veuillez vous assurez que l'infraction n'a pas déjà été saisie par le CNSP
                    </Message>
                  }
                  fieldFormik={fieldFormik}
                  targetType={TargetType.DEFAULT}
                  availableControlTypes={defaultControlTypes}
                  title={`Ajout d'infraction (hors pol. pêche)`}
                  buttonLabel={`Ajouter une infraction (hors pol. pêche)`}
                />
              )}
            </Field>
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Divider style={{ backgroundColor: THEME.color.lightGray, margin: 0 }} />
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <FishControlSeizureSection action={values} />
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <FormikTextAreaInput
              isLight={false}
              name="observationsByUnit"
              data-testid="observations-by-unit"
              label="Observations de l'inspecteur"
            />
            {showCheckbox && <MissionActionIncidentDonwload />}
            <FormikCheckbox
              isLight
              style={{ marginTop: 8 }}
              name="sati.principalInspector.party.signature"
              label="Rapport de contrôle signé par l’inspecteur"
            />
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>{showCheckbox && <MissionActionDivingOperation />}</Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <Divider style={{ backgroundColor: THEME.color.lightGray, margin: 0 }} />
          </Stack.Item>
          <Stack.Item style={{ width: '100%' }}>
            <FormikTextAreaInput
              isLight={false}
              name="sati.vessel.master.comments"
              data-testid="observations-master"
              label="Observations de l’opérateur (personne contrôlée)"
            />
            <FormikCheckbox
              isLight
              style={{ marginTop: 8 }}
              name="sati.vessel.master.signature"
              label="Rapport de contrôle signé par l’opérateur (personne contrôlée)"
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>
    </Stack>
  )
}

export default FishControlConclusion
