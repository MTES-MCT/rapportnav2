import { ActionStatusReason, ActionStatusType } from '@common/types/action-types.ts'
import { FormikSelect, FormikSelectProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'

const REASONS = {
  [ActionStatusReason.MAINTENANCE]: 'Maintenance',
  [ActionStatusReason.WEATHER]: 'Météo',
  [ActionStatusReason.REPRESENTATION]: 'Représentation',
  [ActionStatusReason.ADMINISTRATION]: 'Administration',
  [ActionStatusReason.MCO_AND_LOGISTICS]: 'MCO/Logistique',
  [ActionStatusReason.HARBOUR_CONTROL]: 'Contrôle portuaire',
  [ActionStatusReason.TECHNICAL]: 'Technique',
  [ActionStatusReason.PERSONNEL]: 'Personnel',
  [ActionStatusReason.OTHER]: 'Autre'
}

const DOCKED_REASON_OPTIONS: ActionStatusReason[] = [
  ActionStatusReason.MAINTENANCE,
  ActionStatusReason.WEATHER,
  ActionStatusReason.REPRESENTATION,
  ActionStatusReason.ADMINISTRATION,
  ActionStatusReason.MCO_AND_LOGISTICS,
  ActionStatusReason.HARBOUR_CONTROL,
  ActionStatusReason.OTHER
]

const UNAVAILABLE_REASON_OPTIONS: ActionStatusReason[] = [
  ActionStatusReason.TECHNICAL,
  ActionStatusReason.PERSONNEL,
  ActionStatusReason.OTHER
]

const getSelectOptionsForType = (type: ActionStatusType): ActionStatusReason[] | undefined => {
  switch (type) {
    case ActionStatusType.DOCKED:
      return DOCKED_REASON_OPTIONS
    case ActionStatusType.UNAVAILABLE:
      return UNAVAILABLE_REASON_OPTIONS
    default:
      return undefined
  }
}

type FormikSelectStatusReasonProps = Omit<FormikSelectProps, 'options'> & { status: ActionStatusType }

export const FormikSelectStatusReason = styled((props: FormikSelectStatusReasonProps) => {
  const reasons: ActionStatusReason[] | undefined = getSelectOptionsForType(props.status)
  return !reasons ? undefined : (
    <FormikSelect
      isLight={true}
      options={reasons?.map(reason => ({
        value: reason,
        label: REASONS[reason]
      }))}
      isRequired={true}
      isErrorMessageHidden={true}
      {...props}
    />
  )
})({})

export default FormikSelectStatusReason
