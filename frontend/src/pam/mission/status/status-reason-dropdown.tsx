import { OptionValue, Select } from '@mtes-mct/monitor-ui'
import { ActionStatusReason, ActionStatusType } from '../../../types/action-types'
import React from "react";
import { statusReasonToHumanString } from "./utils.ts";


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

const getSelectOptionsForType = (type: ActionStatusType): {
  label: string | undefined;
  value: ActionStatusReason
}[] | undefined => {
  switch (type) {
    case ActionStatusType.DOCKED:
      return DOCKED_REASON_OPTIONS;
    case ActionStatusType.UNAVAILABLE:
      return UNAVAILABLE_REASON_OPTIONS;
    default:
      return undefined;
  }
}

interface StatusReasonDropdownProps {
  actionType: ActionStatusType
  value: ActionStatusReason
  onSelect: (key: string, value: string) => void
}

const StatusReasonDropdown: React.FC<StatusReasonDropdownProps> = ({actionType, value, onSelect}) => {
  const options = getSelectOptionsForType(actionType)
  return !options ? null : (
    <Select
      label="Motif"
      name="reason"
      isLight={true}
      options={options}
      value={value}
      onChange={(nextValue: OptionValue) => onSelect('reason', nextValue)}
    />
  )
}


export default StatusReasonDropdown
