import { ControlMethod, ControlResult, ControlType } from '@common/types/control-types'

type ControlTypeRegistry = { [key in ControlType]: string }
type ControlMethodRegistry = { [key in ControlMethod]: string }
type ControlResultOptionRegistry = { [key in ControlResult]: string }

const CONTROL_METHOD_REGISTRY: ControlMethodRegistry = {
  [ControlMethod.SEA]: 'en Mer',
  [ControlMethod.AIR]: 'aérien',
  [ControlMethod.LAND]: 'à Terre'
}

const CONTROL_RESULT_OPTION__REGISTRY: ControlResultOptionRegistry = {
  [ControlResult.NO]: 'Non',
  [ControlResult.YES]: 'Oui',
  [ControlResult.NOT_CONCERNED]: 'Non concerné',
  [ControlResult.NOT_CONTROLLED]: 'Non contrôlé'
}

const CONTROL_TYPE_REGISTRY: ControlTypeRegistry = {
  [ControlType.NAVIGATION]: 'Respect des règles de navigation',
  [ControlType.ADMINISTRATIVE]: 'Contrôle administratif navire',
  [ControlType.GENS_DE_MER]: 'Contrôle administratif gens de mer',
  [ControlType.SECURITY]: 'Equipements et respect des normes de sécurité'
}

interface ControlHook {
  getControlType: (type?: ControlType) => string | undefined
  controlTypeOptions: { label: string; value: ControlType }[]
  controlResultOptions: { label: string; value: ControlResult }[]
  getControlMethod: (method?: ControlMethod) => string | undefined
  controlResultOptionsExtra: { label: string; value: ControlResult }[]
  getDisabledControlTypes: (enabledControlTypes?: ControlType[]) => ControlType[]
  getControlResultOptions: (withExtra?: boolean) => { label: string; value: ControlResult }[]
}

export function useControlRegistry(): ControlHook {
  const getControlType = (type?: ControlType) => (type ? CONTROL_TYPE_REGISTRY[type] : '')
  const getControlMethod = (method?: ControlMethod) => (method ? CONTROL_METHOD_REGISTRY[method] : '')
  const getControlResultOptions = (withExtra?: boolean) => {
    const options = Object.keys(ControlResult)?.map(key => ({
      value: ControlResult[key as keyof typeof ControlResult],
      label: CONTROL_RESULT_OPTION__REGISTRY[key as keyof typeof ControlResult]
    }))
    return withExtra ? options : options.filter(v => v.value !== ControlResult.NOT_CONCERNED)
  }

  const getControlTypeOptions = () =>
    Object.keys(ControlType)?.map(key => ({
      value: ControlType[key as keyof typeof ControlType],
      label: `Infraction - ${CONTROL_TYPE_REGISTRY[key as keyof typeof ControlType]}`
    }))

  const getDisabledControlTypes = (enabledControlTypes?: ControlType[]) => {
    const enables = enabledControlTypes ?? []
    return Object.keys(ControlType)
      .map(key => ControlType[key as keyof typeof ControlType])
      .filter(controlType => !enables.includes(controlType))
  }

  return {
    getControlType,
    getControlMethod,
    getControlResultOptions,
    getDisabledControlTypes,
    controlTypeOptions: getControlTypeOptions(),
    controlResultOptions: getControlResultOptions(),
    controlResultOptionsExtra: getControlResultOptions(true)
  }
}
