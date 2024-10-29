import { ControlNavigation, ControlSecurity, ControlType } from '@common/types/control-types.ts'
import { FormikEffect, FormikTextarea, THEME } from '@mtes-mct/monitor-ui'
import { FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Panel, Stack } from 'rsuite'
import { MissionActionFormikToogleUnitShouldConfirm } from '../../../mission-action/components/ui/mission-action-formik-toogle-unit-has-confirm'
import MissionInfractionList from '../../../mission-infraction/components/elements/mission-infraction-list-form'
import { ControlModelInput, useControlModel } from '../../hooks/use-control-model'
import { MissionControlFormikCheckBoxTitle } from '../ui/mission-control-title-checkbox'

type ControlModel = ControlNavigation | ControlSecurity

type MissionControlModelFormProps = {
  name: string
  controlType: ControlType
  unitShouldConfirm?: boolean
  shouldCompleteControl?: boolean
  fieldFormik: FieldProps<ControlModel>
}

const MissionControlModelForm: FC<MissionControlModelFormProps> = ({
  name,
  controlType,
  fieldFormik,
  unitShouldConfirm,
  shouldCompleteControl
}) => {
  const { controlTypeLabel, initValue, handleSubmit } = useControlModel(name, fieldFormik, controlType)
  return (
    <Panel
      header={
        <MissionControlFormikCheckBoxTitle
          text={controlTypeLabel}
          shouldComplete={true}
          name={`unitHasConfirmed`}
          //shouldComplete={isRequired}
          //checked={controlIsChecked}
          //onChange={(isChecked: boolean) => console.log('handleToggleControl')}
        />
      }
      style={{ backgroundColor: THEME.color.white, borderRadius: 0 }}
    >
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit}>
          <>
            <FormikEffect onChange={nextValues => handleSubmit(nextValues as ControlModelInput)} />
            <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
                  {unitShouldConfirm && (
                    <Stack.Item style={{ width: '100%' }}>
                      <MissionActionFormikToogleUnitShouldConfirm name={`unitHasConfirmed`} />
                    </Stack.Item>
                  )}
                  <Stack.Item style={{ width: '100%', paddingTop: 4 }}>
                    <FormikTextarea
                      style={{ width: '100%' }}
                      name={`observations`}
                      label="Observations (hors infraction) sur les pièces administratives"
                    />
                  </Stack.Item>
                </Stack>
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FieldArray name="infractions">
                  {(fieldArray: FieldArrayRenderProps) => (
                    <MissionInfractionList name="infractions" fieldArray={fieldArray} controlType={controlType} />
                  )}
                </FieldArray>
              </Stack.Item>
            </Stack>
          </>
        </Formik>
      )}
    </Panel>
  )
}

export default MissionControlModelForm
