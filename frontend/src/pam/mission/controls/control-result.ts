import { ControlResult } from '../../mission-types'

interface ControlResultOption {
  label: string
  value: ControlResult
}
const defaultOptions: ControlResultOption[] = [
  {
    label: 'Oui',
    value: ControlResult.YES
  },
  {
    label: 'Non',
    value: ControlResult.NO
  },
  {
    label: 'Non contrôlé',
    value: ControlResult.NOT_CONTROLLED
  }
]

export enum ControlResultExtraOptions {
  'NOT_CONCERNED' = 'NOT_CONCERNED'
}

const extraOptions = {
  [ControlResultExtraOptions.NOT_CONCERNED]: {
    label: 'Non concerné',
    value: ControlResult.NOT_CONCERNED
  }
}

export const controlResultOptions: (availableExtraOptions?: ControlResultExtraOptions[]) => ControlResultOption[] = (
  availableExtraOptions = []
) => [...defaultOptions, ...(availableExtraOptions.map(option => extraOptions[option]) || [])]
