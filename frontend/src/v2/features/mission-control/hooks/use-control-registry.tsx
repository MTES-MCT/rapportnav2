import { ControlMethod, ControlResult, ControlType } from '@common/types/control-types'

type ControlTypeRegistry = { [key in ControlType]: string }
type ControlMethodRegistry = { [key in ControlMethod]: string }
type ControlResultOptionRegistry = { [key in ControlResult]: string }
type ControlRadioRegistry = { [key in ControlType]?: { name: string; label: string; extra?: boolean }[] }

const DEFAULT_EMPTY_VALUES = {
  infractions: [],
  observations: undefined
}

const ADMINISTRATIVE_EMPTY_VALUES = {
  compliantOperatingPermit: undefined,
  upToDateNavigationPermit: undefined,
  compliantSecurityDocuments: undefined
}

const GENS_DE_MER_EMPTY_VALUES = {
  staffOutnumbered: undefined,
  upToDateMedicalCheck: undefined,
  knowledgeOfFrenchLawAndLanguage: undefined
}

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
  [ControlType.SECURITY]: 'Équipements et respect des normes de sécurité',

  [ControlType.SECTOR]: 'Filière (traçabilité, certificat, note de vente, vente sous-taille...)',
  [ControlType.TRANSPORT]: 'Transport',
  [ControlType.FISHING_REPORTING_OBLIGATION]: 'Obligations déclaratives pêche (certificats, licences, pesées...)',
  [ControlType.LANDING_OBLIGATION]: 'Obligation de débarquement (rejets...)',
  [ControlType.TECHNICAL_MEASURE]:
    'Mesures techniques et de conservation (engins, espèces interdites, surquota, période, zone interdite...)',
  [ControlType.INN_ACTIVITY]: `Activités INN (pêche non conforme à l'autorisation par navire tiers, navire sans immatriculation...)`,
  [ControlType.OTHER]: 'Autre'
}

const CONTROL_RADIO_REGISTRY: ControlRadioRegistry = {
  [ControlType.NAVIGATION]: [],
  [ControlType.ADMINISTRATIVE]: [
    { name: 'compliantOperatingPermit', label: 'Permis de mise en exploitation (autorisation à pêcher) conforme' },
    { name: 'upToDateNavigationPermit', label: 'Permis de navigation à jour' },
    { name: 'compliantSecurityDocuments', label: 'Titres de sécurité conformes' }
  ],

  [ControlType.GENS_DE_MER]: [
    { name: 'staffOutnumbered', label: 'Décision d’effectif conforme au nombre de personnes à bord' },
    { name: 'upToDateMedicalCheck', label: 'Aptitudes médicales ; Visites médicales à jour' },
    {
      name: 'knowledgeOfFrenchLawAndLanguage',
      label: 'Connaissance suffisante de la langue et de la loi français (navires fr/esp)',
      extra: true
    }
  ],
  [ControlType.SECURITY]: []
}

interface ControlHook {
  getEmptyValues: (type?: ControlType) => any
  getControlType: (type?: ControlType) => string | undefined
  controlTypeOptions: { label: string; value: ControlType }[]
  controlResultOptions: { label: string; value: ControlResult }[]
  getControlMethod: (method?: ControlMethod) => string | undefined
  controlResultOptionsExtra: { label: string; value: ControlResult }[]
  getDisabledControlTypes: (enabledControlTypes?: ControlType[]) => ControlType[]
  getControlResultOptions: (withExtra?: boolean) => { label: string; value: ControlResult }[]
  getRadios: (controlType?: ControlType) => { name: string; label: string; extra?: boolean }[]
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
      label: `${CONTROL_TYPE_REGISTRY[key as keyof typeof ControlType]}`
    }))

  const getDisabledControlTypes = (enabledControlTypes?: ControlType[]) => {
    const enables = enabledControlTypes ?? []
    return Object.keys(ControlType)
      .map(key => ControlType[key as keyof typeof ControlType])
      .filter(controlType => !enables.includes(controlType))
  }

  const getRadios = (controltype?: ControlType) => (controltype ? CONTROL_RADIO_REGISTRY[controltype] : [])

  const getEmptyValues = (controlType?: ControlType) => ({
    ...DEFAULT_EMPTY_VALUES,
    ...(controlType === ControlType.GENS_DE_MER ? GENS_DE_MER_EMPTY_VALUES : {}),
    ...(controlType === ControlType.ADMINISTRATIVE ? ADMINISTRATIVE_EMPTY_VALUES : {})
  })

  return {
    getRadios,
    getControlType,
    getEmptyValues,
    getControlMethod,
    getControlResultOptions,
    getDisabledControlTypes,
    getControlTypeOptions,
    controlTypeOptions: getControlTypeOptions(),
    controlResultOptions: getControlResultOptions(),
    controlResultOptionsExtra: getControlResultOptions(true)
  }
}
