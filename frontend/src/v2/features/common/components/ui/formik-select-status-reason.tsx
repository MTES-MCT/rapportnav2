import { ActionStatusReason } from '@common/types/action-types.ts'
import { FormikSelect, FormikSelectProps } from '@mtes-mct/monitor-ui'
import styled from 'styled-components'

const REASONS = {
  [ActionStatusReason.MAINTENANCE]: 'Maintenance',
  [ActionStatusReason.WEATHER]: 'Météo',
  [ActionStatusReason.REPRESENTATION]: 'Représentation',
  [ActionStatusReason.ADMINISTRATION]: 'Administration',
  [ActionStatusReason.HARBOUR_CONTROL]: 'Contrôle portuaire',
  [ActionStatusReason.TECHNICAL]: 'Technique',
  [ActionStatusReason.PERSONNEL]: 'Personnel',
  [ActionStatusReason.OTHER]: 'Autre'
}

export const FormikSelectStatusReason = styled((props: Omit<FormikSelectProps, 'options'>) => (
  <FormikSelect
    isLight={true}
    options={Object.keys(ActionStatusReason)?.map(key => ({
      value: key,
      label: REASONS[key as keyof typeof ActionStatusReason]
    }))}
    isRequired={true}
    isErrorMessageHidden={true}
    {...props}
  />
))({})

export default FormikSelectStatusReason
