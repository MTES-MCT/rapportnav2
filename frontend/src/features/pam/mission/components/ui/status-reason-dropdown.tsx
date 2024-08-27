import { OptionValue, Select } from '@mtes-mct/monitor-ui'
import { ActionStatusReason, ActionStatusType } from '@common/types/action-types.ts'
import React from 'react'
import { statusReasonToHumanString } from '../../utils/status-utils.ts'

const DOCKED_REASON_OPTIONS: { label: string | undefined; value: ActionStatusReason }[] = [
  {
    label: statusReasonToHumanString(ActionStatusReason.MAINTENANCE),
    value: ActionStatusReason.MAINTENANCE
  },
  {
    label: statusReasonToHumanString(ActionStatusReason.WEATHER),
    value: ActionStatusReason.WEATHER
  },
  {
    label: statusReasonToHumanString(ActionStatusReason.REPRESENTATION),
    value: ActionStatusReason.REPRESENTATION
  },
  {
    label: statusReasonToHumanString(ActionStatusReason.ADMINISTRATION),
    value: ActionStatusReason.ADMINISTRATION
  },
  {
    label: statusReasonToHumanString(ActionStatusReason.HARBOUR_CONTROL),
    value: ActionStatusReason.HARBOUR_CONTROL
  },
  {
    label: statusReasonToHumanString(ActionStatusReason.OTHER),
    value: ActionStatusReason.OTHER
  }
]
const UNAVAILABLE_REASON_OPTIONS: { label: string | undefined; value: ActionStatusReason }[] = [
  {
    label: statusReasonToHumanString(ActionStatusReason.TECHNICAL),
    value: ActionStatusReason.TECHNICAL
  },
  {
    label: statusReasonToHumanString(ActionStatusReason.PERSONNEL),
    value: ActionStatusReason.PERSONNEL
  },
  {
    label: statusReasonToHumanString(ActionStatusReason.OTHER),
    value: ActionStatusReason.OTHER
  }
]

const getSelectOptionsForType = (
  type: ActionStatusType
):
  | {
      label: string | undefined
      value: ActionStatusReason
    }[]
  | undefined => {
  switch (type) {
    case ActionStatusType.DOCKED:
      return DOCKED_REASON_OPTIONS
    case ActionStatusType.UNAVAILABLE:
      return UNAVAILABLE_REASON_OPTIONS
    default:
      return undefined
  }
}

interface StatusReasonDropdownProps {
  actionType: ActionStatusType
  value: ActionStatusReason
  onSelect: (key: string, value: string) => void
  isRequired: boolean
  error: boolean
}

const StatusReasonDropdown: React.FC<StatusReasonDropdownProps> = ({
  actionType,
  value,
  error,
  isRequired,
  onSelect
}) => {
  const options = getSelectOptionsForType(actionType)
  return !options ? null : (
    <Select
      isRequired={isRequired}
      label="Motif"
      name="reason"
      isLight={true}
      options={options}
      value={value}
      error={error ? 'error ' : undefined}
      isErrorMessageHidden={true}
      onChange={(nextValue: OptionValue) => onSelect('reason', nextValue)}
    />
  )
}

export default StatusReasonDropdown
