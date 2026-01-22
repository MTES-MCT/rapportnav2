import { ActionStatusType } from '@common/types/action-types'
import { getColorForStatus, mapStatusToText } from '@common/utils/status-utils'
import { FormikDatePicker, FormikEffect, FormikTextarea, Icon, Tag } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import FormikSelectStatusReason from '../../../common/components/ui/formik-select-status-reason'
import { MissionAction, MissionNavAction } from '../../../common/types/mission-action'
import { useMissionActionStatus } from '../../hooks/use-mission-action-status'
import { ActionStatusInput } from '../../types/action-type'

const MissionActionItemStatus: FC<{
  action: MissionNavAction
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
  isMissionFinished?: boolean
}> = ({ action, onChange }) => {
  const { initValue, handleSubmit, validationSchema } = useMissionActionStatus(action, onChange)
  return (
    <form style={{ width: '100%' }}>
      {initValue && (
        <Formik
          initialValues={initValue}
          onSubmit={handleSubmit}
          validateOnChange={false}
          enableReinitialize
          validationSchema={validationSchema}
        >
          {({ validateForm }) => (
            <>
              <FormikEffect
                onChange={async nextValue => {
                  // Only handle submission, let Formik handle validation display
                  await handleSubmit(nextValue as ActionStatusInput)
                  // Optionally trigger validation to ensure UI updates
                  await validateForm()
                }}
              />
              <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
                <Stack.Item style={{ width: '100%' }}>
                  <Tag
                    Icon={Icon.CircleFilled}
                    iconColor={action.data.status ? getColorForStatus(ActionStatusType[action.data.status]) : undefined}
                    isLight
                    withCircleIcon={true}
                  >
                    {action.data.status ? mapStatusToText(ActionStatusType[action.data.status]) : undefined}
                  </Tag>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
                    <Stack.Item grow={1}>
                      <FormikDatePicker
                        name="date"
                        isLight={true}
                        withTime={true}
                        isRequired={true}
                        isCompact={false}
                        label="Date et heure (utc)"
                      />
                    </Stack.Item>
                    <Stack.Item grow={3}>
                      <FormikSelectStatusReason label="Motif" name="reason" status={action.status} />
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FormikTextarea label="Observations" isLight={true} name="observations" data-testid="observations" />
                </Stack.Item>
              </Stack>
            </>
          )}
        </Formik>
      )}
    </form>
  )
}

export default MissionActionItemStatus
