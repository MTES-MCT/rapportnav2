import React from 'react'
import { THEME, Icon, Button, Accent, Size, DatePicker, Tag, Select, MultiRadio, Textarea } from '@mtes-mct/monitor-ui'
import { ActionStatus, ACTION_STATUS_REASON_OPTIONS, ActionStatusType, Action } from '../../mission-types'
import { Stack } from 'rsuite'
import Title from '../../../ui/title'
import { formatDateTimeForFrenchHumans } from '../../../dates'
import { getColorForStatus, mapStatusToText } from '../status/utils'

interface ActionStatusFormProps {
  action: Action
}

const ActionStatusForm: React.FC<ActionStatusFormProps> = ({ action }) => {
  const status: ActionStatus = action.data || {}
  return (
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
                  {action.startDateTimeUtc && `(${formatDateTimeForFrenchHumans(action.startDateTimeUtc)})`}
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
                <Button accent={Accent.SECONDARY} size={Size.SMALL} Icon={Icon.Delete}>
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
              onChange={function noRefCheck() {}}
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
            />
          </Stack.Item>
          <Stack.Item grow={3}>
            <Select label="Motif" isLight={true} options={ACTION_STATUS_REASON_OPTIONS} value={status.reason} />
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Textarea label="Observations" value={status.observations} isLight={true} />
      </Stack.Item>
    </Stack>
  )
}

export default ActionStatusForm
