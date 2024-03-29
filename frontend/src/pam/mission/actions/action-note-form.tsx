import React, { useEffect, useState } from 'react'
import {
    Accent,
    Button,
    DatePicker,
    Icon,
    Size,
    Textarea,
    THEME
} from '@mtes-mct/monitor-ui'
import { Action, ActionFreeNote, } from '../../../types/action-types'
import { Stack } from 'rsuite'
import Text from '../../../ui/text'
import { formatDateTimeForFrenchHumans } from '../../../utils/dates.ts'
import { useNavigate, useParams } from 'react-router-dom'
import omit from 'lodash/omit'
import useActionById from "./use-action-by-id.tsx";
import useAddOrUpdateNote from "../notes/use-add-update-note.tsx";
import useDeleteNote from "../notes/use-delete-note.tsx";

interface ActionNoteFormProps {
    action: Action
}

const ActionNoteForm: React.FC<ActionNoteFormProps> = ({action}) => {
    const navigate = useNavigate()
    const {missionId, actionId} = useParams()

    const {data: navAction, loading, error} = useActionById(actionId, missionId, action.source, action.type)
    const [mutateNote] = useAddOrUpdateNote()
    const [deleteNote] = useDeleteNote()

    const [observationsValue, setObservationsValue] = useState<string | undefined>(undefined)

    useEffect(() => {
        setObservationsValue(navAction?.data?.observations)
    }, [navAction])

    if (loading) {
        return (
            <div>Chargement...</div>
        )
    }
    if (error) {
        return (
            <div>error</div>
        )
    }
    if (navAction) {
        const note = navAction?.data as ActionFreeNote

        const handleObservationsChange = (nextValue?: string) => {
            setObservationsValue(nextValue)
        }


        const handleObservationsBlur = async () => {
            await onChange('observations', observationsValue)
        }

        const onChange = async (field: string, value: any) => {
            const updatedData = {
                missionId: missionId,
                ...omit(note, '__typename'),
                [field]: value?.trim()
            }
            await mutateNote({variables: {freeNoteAction: updatedData}})
        }

        const deleteAction = async () => {
            await deleteNote({
                variables: {
                    id: action.id!
                }
            })
            navigate(`/pam/missions/${missionId}`)
        }

        return (
            <form style={{width: '100%'}} data-testid={"action-note-form"}>
                <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{width: '100%'}}>
                    {/* TITLE AND BUTTONS */}
                    <Stack.Item style={{width: '100%'}}>
                        <Stack direction="row" spacing="0.5rem" style={{width: '100%'}}>
                            <Stack.Item alignSelf="baseline">
                                <Icon.Note color={THEME.color.charcoal} size={20}/>
                            </Stack.Item>
                            <Stack.Item grow={2}>
                                <Stack direction="column" alignItems="flex-start">
                                    <Stack.Item>
                                        <Text as="h2" weight="bold">
                                            Note libre{' '}
                                            {note.startDateTimeUtc && `(${formatDateTimeForFrenchHumans(note.startDateTimeUtc)})`}
                                        </Text>
                                    </Stack.Item>
                                </Stack>
                            </Stack.Item>
                            <Stack.Item>
                                <Stack direction="row" spacing="0.5rem">
                                    <Stack.Item>
                                        <Button accent={Accent.SECONDARY} size={Size.SMALL} Icon={Icon.Duplicate}
                                                disabled>
                                            Dupliquer
                                        </Button>
                                    </Stack.Item>
                                    <Stack.Item>
                                        <Button accent={Accent.SECONDARY} size={Size.SMALL} Icon={Icon.Delete}
                                                onClick={deleteAction} data-testid={"deleteButton"}>
                                            Supprimer
                                        </Button>
                                    </Stack.Item>
                                </Stack>
                            </Stack.Item>
                        </Stack>
                    </Stack.Item>

                    <Stack.Item style={{width: '100%'}}>
                        <Stack direction="row" spacing="0.5rem" style={{width: '100%'}}>
                            <Stack.Item grow={1}>
                                <DatePicker
                                    defaultValue={note.startDateTimeUtc}
                                    label="Date et heure"
                                    withTime={true}
                                    isCompact={false}
                                    isLight={true}
                                    name="startDateTimeUtc"
                                    onChange={async (nextUtcDate: Date) => {
                                        const date = new Date(nextUtcDate)
                                        await onChange('startDateTimeUtc', date.toISOString())
                                    }}
                                />
                            </Stack.Item>
                        </Stack>
                    </Stack.Item>

                    <Stack.Item style={{width: '100%'}}>
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
