import React, { useEffect, useState } from 'react'
import { Textarea } from '@mtes-mct/monitor-ui'
import { Action, ActionVigimer } from '@common/types/action-types.ts'
import { Stack } from 'rsuite'
import { useNavigate, useParams } from 'react-router-dom'
import { omit } from 'lodash'
import useActionById from '../../../hooks/use-action-by-id.tsx'
import useAddOrUpdateVigimer from '../../../hooks/vigimer/use-add-vigimer.tsx'
import useDeleteVigimer from '../../../hooks/vigimer/use-delete-vigimer.tsx'
import ActionHeader from './action-header.tsx'
import useIsMissionFinished from '../../../hooks/use-is-mission-finished.tsx'
import { DateRange } from '@mtes-mct/monitor-ui/types/definitions'
import DateRangePicker from '@common/components/elements/dates/daterange-picker.tsx'

interface ActionVigimerFormProps {
  action: Action
}

const ActionVigimerForm: React.FC<ActionVigimerFormProps> = ({ action }) => {
  const navigate = useNavigate()
  const { missionId, actionId } = useParams()
  const isMissionFinished = useIsMissionFinished(missionId)

  const { data: navAction, loading, error } = useActionById(actionId, missionId, action.source, action.type)
  const [mutateVigimer] = useAddOrUpdateVigimer()
  const [deleteVigimer] = useDeleteVigimer()

  const [observationsValue, setObservationsValue] = useState<string | undefined>(undefined)

  useEffect(() => {
    setObservationsValue(navAction?.data?.observations)
  }, [navAction])

  if (loading) {
    return <div>Chargement...</div>
  }
  if (error) {
    return <div>error</div>
  }
  if (navAction) {
    const actionData = navAction?.data as unknown as ActionVigimer

    const handleObservationsChange = (nextValue?: string) => {
      setObservationsValue(nextValue)
    }

    const handleObservationsBlur = async () => {
      await onChange(observationsValue)('observations')
    }

    const onChange = (value: any) => async (field: string) => {
      let updatedField: {}
      if (field === 'dates') {
        const startDateTimeUtc = value[0]
        const endDateTimeUtc = value[1]
        updatedField = {
          startDateTimeUtc,
          endDateTimeUtc
        }
      } else {
        updatedField = {
          [field]: value
        }
      }

      const updatedData = {
        missionId: missionId,
        ...omit(actionData, ['__typename']),
        startDateTimeUtc: navAction.startDateTimeUtc,
        endDateTimeUtc: navAction.endDateTimeUtc,
        ...updatedField
      }

      await mutateVigimer({ variables: { vigimerAction: updatedData } })
    }

    const deleteAction = async () => {
      await deleteVigimer({
        variables: {
          id: action.id
        }
      })
      navigate(`/pam/missions/${missionId}`)
    }

    return (
      <form style={{ width: '100%' }} data-testid={'action-nautical-event-form'}>
        <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
          {/* TITLE AND BUTTONS */}
          <Stack.Item style={{ width: '100%' }}>
            <ActionHeader
              icon={undefined}
              title={'Permanence Vigimer'}
              date={actionData.startDateTimeUtc}
              onDelete={deleteAction}
              showButtons={true}
              showStatus={true}
              isMissionFinished={isMissionFinished}
              completenessForStats={navAction.completenessForStats}
            />
          </Stack.Item>

          <Stack.Item style={{ width: '100%' }}>
            <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
              <Stack.Item grow={1}>
                <DateRangePicker
                  name="dates"
                  isRequired={true}
                  error={!navAction.startDateTimeUtc && !navAction.endDateTimeUtc ? 'error' : undefined}
                  selectedRange={[navAction.startDateTimeUtc, navAction.endDateTimeUtc]}
                  isStringDate={false}
                  label="Date et heure de début et de fin"
                  withTime={true}
                  isCompact={true}
                  isLight={true}
                  onChange={async (nextValue?: DateRange) => {
                    await onChange(nextValue)('dates')
                  }}
                />
              </Stack.Item>
            </Stack>
          </Stack.Item>

          <Stack.Item style={{ width: '100%' }}>
            <Textarea
              label="Observations"
              value={observationsValue}
              isLight={true}
              name="observations"
              data-testid="observations"
              onChange={handleObservationsChange}
              onBlur={handleObservationsBlur}
            />
          </Stack.Item>
        </Stack>
      </form>
    )
  }
  return null
}

export default ActionVigimerForm
