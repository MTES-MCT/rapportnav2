import { FormikEffect } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikTextAreaInput } from '../../../common/components/ui/formik-textarea-input'
import { MissionAction } from '../../../common/types/mission-action'
import { useMissionActionFreeNote } from '../../hooks/use-mission-action-note'
import { ActionFreeNoteInput } from '../../types/action-type'
import MissionBoundFormikDatePicker from '../../../common/components/elements/mission-bound-formik-date-picker.tsx'

const MissionActionItemNote: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { initValue, handleSubmit, validationSchema } = useMissionActionFreeNote(action, onChange)
  return (
    <form style={{ width: '100%' }}>
      {initValue && (
        <Formik
          initialValues={initValue}
          onSubmit={handleSubmit}
          validationSchema={validationSchema}
          validateOnChange={true}
          enableReinitialize
        >
          {({ validateForm }) => (
            <>
              <FormikEffect
                onChange={async nextValue => {
                  await handleSubmit(nextValue as ActionFreeNoteInput)
                  await validateForm(nextValue)
                }}
              />
              <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
                    <Stack.Item grow={1}>
                      <MissionBoundFormikDatePicker
                        name="date"
                        isLight={true}
                        withTime={true}
                        isRequired={true}
                        isCompact={false}
                        label="Date et heure (utc)"
                        missionId={action.missionId}
                      />
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FormikTextAreaInput
                    label="Observations"
                    isLight={true}
                    name="observations"
                    data-testid="observations"
                  />
                </Stack.Item>
              </Stack>
            </>
          )}
        </Formik>
      )}
    </form>
  )
}

export default MissionActionItemNote
