import React, { useEffect, useState } from 'react'
import { Textarea } from '@mtes-mct/monitor-ui'
import DateRangePicker from '@common/components/elements/dates/daterange-picker.tsx'
import { Action, ActionBAAEMPermanence } from '@common/types/action-types.ts'
import { Stack } from 'rsuite'
import { useNavigate, useParams } from 'react-router-dom'
import omit from 'lodash/omit'
import useActionById from '../../../hooks/use-action-by-id.tsx'
import useAddOrUpdateBAAEMPermanence from '../../../hooks/baaem/use-add-baaem-permanence.tsx'
import useDeleteBaaemPermanence from '../../../hooks/baaem/use-delete-baaem-permanence.tsx'
import useIsMissionFinished from '../../../hooks/use-is-mission-finished.tsx'
import ActionHeader from './action-header.tsx'
import { DateRange } from '@mtes-mct/monitor-ui/types/definitions'

interface ActionBAAEMPermanenceFormProps {
  action: Action
}

const ActionBAAEMPermanenceForm: React.FC<ActionBAAEMPermanenceFormProps> = ({ action }) => {
  const navigate = useNavigate()
  const { missionId, actionId } = useParams()
  const isMissionFinished = useIsMissionFinished(missionId)

  const { data: navAction, loading, error } = useActionById(actionId, missionId, action.source, action.type)
  const [mutateBAAEMPermanence] = useAddOrUpdateBAAEMPermanence()
  const [deleteBAAEMPermanence] = useDeleteBaaemPermanence()

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
    const actionData = navAction?.data as unknown as ActionBAAEMPermanence

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

      await mutateBAAEMPermanence({ variables: { baaemPermanenceAction: updatedData } })
    }

    const deleteAction = async () => {
      await deleteBAAEMPermanence({
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
              title={'Permanence BAAEM'}
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
                  selectedRange={[navAction.startDateTimeUtc, navAction.endDateTimeUtc]}
                  error={!navAction.startDateTimeUtc && !navAction.endDateTimeUtc ? 'error' : undefined}
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

export default ActionBAAEMPermanenceForm
