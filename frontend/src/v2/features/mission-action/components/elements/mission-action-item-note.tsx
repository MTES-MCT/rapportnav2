import { FormikDatePicker, FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { MissionAction } from '../../../common/types/mission-action'
import { useMissionActionFreeNote } from '../../hooks/use-mission-action-note'
import { ActionFreeNoteInput } from '../../types/action-type'

const MissionActionItemNote: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { initValue, handleSubmit } = useMissionActionFreeNote(action, onChange)
  return (
    <form style={{ width: '100%' }}>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} validateOnChange={true}>
          <>
            <FormikEffect onChange={nextValue => handleSubmit(nextValue as ActionFreeNoteInput)} />
            <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
                  <Stack.Item grow={1}>
                    <FormikDatePicker
                      name="date"
                      isLight={true}
                      withTime={true}
                      isRequired={true}
                      isCompact={false}
                      label="Date et heure"
                    />
                  </Stack.Item>
                </Stack>
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <FormikTextarea label="Observations" isLight={true} name="observations" data-testid="observations" />
              </Stack.Item>
            </Stack>
          </>
        </Formik>
      )}
    </form>
  )
}

export default MissionActionItemNote
