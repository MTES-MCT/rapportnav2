import React from 'react'
import {
  THEME,
  Icon,
  Button,
  Accent,
  Size,
  DatePicker,
  Tag,
  Select,
  MultiRadio,
  Textarea,
  OptionValue
} from '@mtes-mct/monitor-ui'
import { ActionStatus, ACTION_STATUS_REASON_OPTIONS, ActionStatusType, Action } from '../../mission-types'
import { Stack } from 'rsuite'
import Title from '../../../ui/title'
import { formatDateTimeForFrenchHumans } from '../../../dates'
import { getColorForStatus, mapStatusToText } from '../status/utils'
import { useMutation } from '@apollo/client'
import { DELETE_ACTION_STATUS, MUTATION_ADD_OR_UPDATE_ACTION_STATUS } from '../queries'
import { useNavigate, useParams } from 'react-router-dom'
import omit from 'lodash/omit'

interface ActionStatusFormProps {
  action: Action
}

const ActionStatusForm: React.FC<ActionStatusFormProps> = ({ action }) => {
  const navigate = useNavigate()
  const { missionId } = useParams()

  const status = action.data as unknown as ActionStatus

  const [mutateStatus, { statusData, statusLoading, statusError }] = useMutation(MUTATION_ADD_OR_UPDATE_ACTION_STATUS, {
    refetchQueries: ['GetMissionById']
  })

  const [deleteStatus, { deleteData, deleteLoading, deleteError }] = useMutation(DELETE_ACTION_STATUS, {
    refetchQueries: ['GetMissionById']
  })

  const onChange = (field: string, value: any) => {
    let date = ''
    if (field === 'startDateTimeUtc') {
      date = value
    } else {
      const savedDate = new Date(status.startDateTimeUtc!)
      date = savedDate.toISOString()
    }
    const updatedData = {
      missionId: missionId,
      ...omit(status, '__typename'),
      startDateTimeUtc: date,
      [field]: value
    }
    mutateStatus({ variables: { statusAction: updatedData } })
  }

  const deleteAction = () => {
    deleteStatus({
      variables: {
        id: action.id!
      }
    })
    navigate(`/pam/missions/${missionId}`)
  }

  return (
    <form>
      <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
        {/* TITLE AND BUTTONS */}
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
            <Stack.Item alignSelf="baseline">
              <Icon.FleetSegment color={THEME.color.charcoal} size={20} />
            </Stack.Item>
            <Stack.Item grow={2}>
              <Stack direction="column" alignItems="flex-start">
                <Stack.Item>
                  <Title as="h2">
                    Status du navire{' '}
                    {status.startDateTimeUtc && `(${formatDateTimeForFrenchHumans(status.startDateTimeUtc)})`}
                  </Title>
                </Stack.Item>
              </Stack>
            </Stack.Item>
            <Stack.Item>
              <Stack direction="row" spacing="0.5rem">
                <Stack.Item>
                  <Button accent={Accent.SECONDARY} size={Size.SMALL} Icon={Icon.Duplicate} disabled>
                    Dupliquer
                  </Button>
                </Stack.Item>
                <Stack.Item>
                  <Button accent={Accent.SECONDARY} size={Size.SMALL} Icon={Icon.Delete} onClick={deleteAction}>
                    Supprimer
                  </Button>
                </Stack.Item>
              </Stack>
            </Stack.Item>
          </Stack>
        </Stack.Item>
        {/* STATUS FIELDS */}
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="row" spacing="2rem" style={{ width: '100%' }}>
            <Stack.Item>
              <Tag bullet="DISK" bulletColor={getColorForStatus(ActionStatusType[action.status])} isLight>
                {mapStatusToText(ActionStatusType[action.status])}
              </Tag>
            </Stack.Item>
            <Stack.Item>
              <MultiRadio
                value={status.isStart || false}
                error=""
                isInline
                label=""
                name="isStart"
                onChange={(nextValue: OptionValue) => onChange('isStart', nextValue)}
                options={[
                  {
                    label: 'Début',
                    value: true
                  },
                  {
                    label: 'Fin',
                    value: false
                  }
                ]}
              />
            </Stack.Item>
          </Stack>
        </Stack.Item>
        {/* DATE & REASON FIELDS */}
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
            <Stack.Item grow={1}>
              <DatePicker
                defaultValue={action.startDateTimeUtc}
                label="Date et heure"
                withTime={true}
                isCompact={false}
                isLight={true}
                name="startDateTimeUtc"
                onChange={(nextUtcDate: Date) => {
                  const date = new Date(nextUtcDate)
                  onChange('startDateTimeUtc', date.toISOString())
                }}
              />
            </Stack.Item>
            <Stack.Item grow={3}>
              <Select
                label="Motif"
                name="reason"
                isLight={true}
                options={ACTION_STATUS_REASON_OPTIONS}
                value={status.reason}
                onChange={(nextValue: OptionValue) => onChange('reason', nextValue)}
              />
            </Stack.Item>
          </Stack>
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Textarea
            label="Observations"
            value={status.observations}
            isLight={true}
            name="observations"
            onChange={(nextValue: string) => onChange('observations', nextValue)}
          />
        </Stack.Item>
      </Stack>
    </form>
  )
}

export default ActionStatusForm
