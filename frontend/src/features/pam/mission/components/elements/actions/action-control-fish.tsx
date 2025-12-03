import React from 'react'
import { Option } from '@mtes-mct/monitor-ui'
import { ControlCheck } from '@common/types/fish-mission-types.ts'

export const controlCheckMultiRadioOptions = Object.keys(ControlCheck).map(key => {
  let label
  switch (key) {
    case ControlCheck.YES:
      label = 'Oui'
      break
    case ControlCheck.NO:
      label = 'Non'
      break
    default:
      label = 'Non contrôlé'
  }

  return {
    label,
    value: ControlCheck[key as keyof typeof ControlCheck]
  }
})

export const BOOLEAN_AS_OPTIONS: Array<Option<boolean>> = [
  { label: 'Oui', value: true },
  { label: 'Non', value: false }
]

type ActionControlPropsFish = any

const ActionControlFish: React.FC<ActionControlPropsFish> = ({ action }) => {
  return null
}

export default ActionControlFish
