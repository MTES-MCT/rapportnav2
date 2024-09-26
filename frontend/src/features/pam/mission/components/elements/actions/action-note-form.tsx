import React, { useEffect, useState } from 'react'
import { Icon, Textarea } from '@mtes-mct/monitor-ui'
import DatePicker from '@common/components/elements/dates/date-picker.tsx'
import { ActionFreeNote } from '@common/types/action-types.ts'
import { Stack } from 'rsuite'
import { useNavigate, useParams } from 'react-router-dom'
import omit from 'lodash/omit'
import useActionById from '../../../hooks/use-action-by-id.tsx'
import useAddOrUpdateNote from '../../../hooks/use-add-update-note.tsx'
import useDeleteNote from '../../../hooks/use-delete-note.tsx'
import { ActionDetailsProps } from './action-mapping.ts'
import ActionHeader from './action-header.tsx'
import useIsMissionFinished from '../../../hooks/use-is-mission-finished.tsx'

type ActionNoteFormProps = ActionDetailsProps

const ActionNoteForm: React.FC<ActionNoteFormProps> = ({ action }) => {
  const navigate = useNavigate()
  const { missionId, actionId } = useParams()
  const isMissionFinished = useIsMissionFinished(missionId)

  const { data: navAction, loading, error } = useActionById(actionId, missionId, action.source, action.type)
  const [mutateNote] = useAddOrUpdateNote()
  const [deleteNote] = useDeleteNote()

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
    const note = navAction?.data as ActionFreeNote

    const handleObservationsChange = (nextValue?: string) => {
      setObservationsValue(nextValue)
    }

    const handleObservationsBlur = async () => {
      await onChange(observationsValue)('observations')
    }

    const onChange = (value: any) => async (field: string) => {
      const updatedData = {
        missionId: missionId,
        ...omit(note, '__typename'),
        [field]: value
      }
      await mutateNote({ variables: { freeNoteAction: updatedData } })
    }

    const deleteAction = async () => {
      await deleteNote({
        variables: {
          id: action.id
        }
      })
      navigate(`/pam/missions/${missionId}`)
    }

    return (
      <form style={{ width: '100%' }} data-testid={'action-note-form'}>
        <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
          {/* TITLE AND BUTTONS */}
          <Stack.Item style={{ width: '100%' }}>
            <ActionHeader
              icon={Icon.Note}
              title={'Note libre'}
              date={note.startDateTimeUtc}
              showButtons={true}
              showStatus={false}
              onDelete={deleteAction}
              isMissionFinished={isMissionFinished}
              completenessForStats={navAction.completenessForStats}
            />
          </Stack.Item>

          <Stack.Item style={{ width: '100%' }}>
            <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
              <Stack.Item grow={1}>
                <DatePicker
                  defaultValue={note.startDateTimeUtc}
                  isRequired={true}
                  label="Date et heure"
                  withTime={true}
                  isCompact={false}
                  isLight={true}
                  name="startDateTimeUtc"
                  onChange={async (nextUtcDate: Date) => {
                    await onChange(nextUtcDate)('startDateTimeUtc')
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

export default ActionNoteForm
