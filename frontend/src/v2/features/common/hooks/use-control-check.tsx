import { ControlCheck } from '@common/types/fish-mission-types'

type ControlCheckRegistry = { [key in ControlCheck]: string }

const CONTROL_CHECK_REGISTRY: ControlCheckRegistry = {
  [ControlCheck.NO]: 'Non',
  [ControlCheck.YES]: 'Oui',
  [ControlCheck.NOT_APPLICABLE]: 'Non contrôlé'
}

interface ControlCheckHook {
  getControlCheck: (type?: ControlCheck) => string | undefined
  controlCheckRadioOptions: { label: string; value: ControlCheck }[]
  controlCheckRadioBooleanOptions: { label: string; value: boolean }[]
}

export function usecontrolCheck(): ControlCheckHook {
  const getControlCheck = (type?: ControlCheck) => (type ? CONTROL_CHECK_REGISTRY[type] : '')
  const getControlCheckRadioOptions = () =>
    Object.keys(ControlCheck)?.map(key => ({
      value: ControlCheck[key as keyof typeof ControlCheck],
      label: CONTROL_CHECK_REGISTRY[key as keyof typeof ControlCheck]
    }))

  const getControlCheckRadioBooleanOptions = () =>
    Object.keys(ControlCheck)
      ?.map(key => ({
        value: key === ControlCheck.YES,
        label: CONTROL_CHECK_REGISTRY[key as keyof typeof ControlCheck]
      }))
      .filter(o => o.label !== CONTROL_CHECK_REGISTRY.NOT_APPLICABLE)
  return {
    getControlCheck,
    controlCheckRadioOptions: getControlCheckRadioOptions(),
    controlCheckRadioBooleanOptions: getControlCheckRadioBooleanOptions()
  }
}
