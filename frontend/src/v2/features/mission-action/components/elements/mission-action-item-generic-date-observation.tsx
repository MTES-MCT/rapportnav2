import { FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { FC, JSX } from 'react'
import { Stack } from 'rsuite'
import { ObjectShape } from 'yup'
import { MissionAction } from '../../../common/types/mission-action'
import { useMissionActionGenericDateObservation } from '../../hooks/use-mission-action-generic-date-observation'
import { ActionGenericDateObservationInput } from '../../types/action-type'
import MissionActionDivingOperation from '../ui/mission-action-diving-operation'
import MissionActionIncidentDonwload from '../ui/mission-action-incident-download'
import MissionBoundFormikDateRangePicker from '../../../common/components/elements/mission-bound-formik-date-range-picker.tsx'

const MissionActionItemGenericDateObservation: FC<{
  action: MissionAction
  children?: JSX.Element
  schema?: ObjectShape
  showIncidentReport?: boolean
  showDivingCheckBox?: boolean
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
}> = ({ action, onChange, children, schema, showDivingCheckBox, showIncidentReport }) => {
  const { initValue, handleSubmit, validationSchema, errors } = useMissionActionGenericDateObservation(
    action,
    onChange,
    schema
  )

  return (
    <form style={{ width: '100%' }}>
      {initValue && (
        <Formik
          initialValues={initValue}
          onSubmit={handleSubmit}
          validateOnChange={true}
          enableReinitialize
          validationSchema={validationSchema}
          initialErrors={errors}
        >
          {({ validateForm }) => (
            <>
              <FormikEffect
                onChange={async nextValue => {
                  await handleSubmit(nextValue as ActionGenericDateObservationInput)
                  await validateForm(nextValue)
                }}
              />
              <Stack direction="column" spacing="1rem" alignItems="flex-start" style={{ width: '100%' }}>
                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
                    <Stack.Item grow={1}>
                      <MissionBoundFormikDateRangePicker
                        isLight={true}
                        missionId={action.ownerId ?? action.missionId}
                      />
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>{children}</Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FormikTextarea label="Observations" isLight={true} name="observations" data-testid="observations" />
                  {showIncidentReport && <MissionActionIncidentDonwload />}
                  {showDivingCheckBox && (
                    <Stack.Item style={{ width: '100%' }}>
                      <MissionActionDivingOperation />
                    </Stack.Item>
                  )}
                </Stack.Item>
              </Stack>
            </>
          )}
        </Formik>
      )}
    </form>
  )
}

export default MissionActionItemGenericDateObservation
