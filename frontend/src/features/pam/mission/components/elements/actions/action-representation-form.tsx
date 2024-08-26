import React, { useEffect, useState } from 'react'
import { DateRangePicker, Textarea } from '@mtes-mct/monitor-ui'
import { Action, ActionRepresentation } from '../../../../../common/types/action-types.ts'
import { Stack } from 'rsuite'
import { useNavigate, useParams } from 'react-router-dom'
import omit from 'lodash/omit'
import useActionById from '../../../hooks/use-action-by-id.tsx'
import useAddOrUpdateRepresentation from '../../../hooks/representation/use-add-representation.tsx'
import useDeleteRepresentation from '../../../hooks/representation/use-delete-representation.tsx'
import ActionHeader from './action-header.tsx'
import useIsMissionFinished from '../../../hooks/use-is-mission-finished.tsx'

interface ActionRepresentationFormProps {
  action: Action
}

const ActionRepresentationForm: React.FC<ActionRepresentationFormProps> = ({ action }) => {
  const navigate = useNavigate()
  const { missionId, actionId } = useParams()
  const isMissionFinished = useIsMissionFinished(missionId)

  const { data: navAction, loading, error } = useActionById(actionId, missionId, action.source, action.type)
  const [mutateRepresentation] = useAddOrUpdateRepresentation()
  const [deleteRepresentation] = useDeleteRepresentation()

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
    const actionData = navAction?.data as unknown as ActionRepresentation

    const handleObservationsChange = (nextValue?: string) => {
      setObservationsValue(nextValue)
    }

    const handleObservationsBlur = async () => {
      await onChange('observations', observationsValue)
    }

    const onChange = async (field: string, value: any) => {
      let updatedField: {}
      if (field === 'dates') {
        const startDateTimeUtc = value[0].toISOString()
        const endDateTimeUtc = value[1].toISOString()
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

      console.log(updatedData)

      await mutateRepresentation({ variables: { representationAction: updatedData } })
    }

    const deleteAction = async () => {
      await deleteRepresentation({
        variables: {
          id: action.id
        }
      })
      navigate(`/pam/missions/${missionId}`)
    }

    return (
      <form style={{ width: '100%' }} data-testid={'action-nautical-event-form'}>
        <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
          <Stack.Item style={{ width: '100%' }}>
            {/* TITLE AND BUTTONS */}
            <ActionHeader
              icon={undefined}
              title={'Représentation'}
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
                  defaultValue={
                    navAction.startDateTimeUtc && navAction.endDateTimeUtc
                      ? [navAction.startDateTimeUtc, navAction.endDateTimeUtc]
                      : undefined
                  }
                  label="Date et heure de début et de fin"
                  withTime={true}
                  isCompact={true}
                  isLight={true}
                  onChange={async (nextValue?: [Date, Date] | [string, string]) => {
                    await onChange('dates', nextValue)
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

export default ActionRepresentationForm
