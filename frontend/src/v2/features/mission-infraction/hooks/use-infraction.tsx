import { ControlType } from '@common/types/control-types'
import { InfractionTypeEnum } from '@common/types/env-mission-types'
import { InfractionType } from '@common/types/fish-mission-types'

type InfractionTypeRegistry = { [key in InfractionType | InfractionTypeEnum]: string }

const INFRACTION_TYPE_REGISTRY: InfractionTypeRegistry = {
  [InfractionType.PENDING]: 'En attente',
  [InfractionType.WITH_RECORD]: 'Avec PV',
  [InfractionType.WITHOUT_RECORD]: 'Sans PV',
  [InfractionTypeEnum.WAITING]: 'En attente',
  [InfractionTypeEnum.WITH_REPORT]: 'Avec PV',
  [InfractionTypeEnum.WITHOUT_REPORT]: 'Sans PV'
}

type ControlTypeRegistry = { [key in ControlType]: { title: string; button: string } }

const CONTROL_TYPE_REGISTRY: ControlTypeRegistry = {
  [ControlType.ADMINISTRATIVE]: { title: 'Infraction administrative', button: 'Ajouter une infraction administrative' },
  [ControlType.NAVIGATION]: {
    title: 'Infraction règles de navigation',
    button: 'Ajouter une infraction règle de navigation'
  },
  [ControlType.GENS_DE_MER]: {
    title: 'Infraction administrative gens de mer',
    button: 'Ajouter une infraction administrative'
  },
  [ControlType.SECURITY]: {
    title: `Infraction équipements et respect des normes de sécurité`,
    button: 'Ajouter une infraction sécurité'
  }
}

interface InfractionHook {
  infractionTypeOptions: { label: string; value: InfractionType }[]
  infractionTypeEnumOptions: { label: string; value: InfractionTypeEnum }[]
  getInfractionByControlTypeTitle: (type: ControlType) => string
  getInfractionByControlTypeButton: (type: ControlType) => string
  getInfractionTypeTag: (type?: InfractionType | InfractionTypeEnum) => string | undefined
}

export function useInfraction(): InfractionHook {
  const getInfractionTypeTag = (type?: InfractionType | InfractionTypeEnum) =>
    type ? INFRACTION_TYPE_REGISTRY[type] : INFRACTION_TYPE_REGISTRY[InfractionType.WITHOUT_RECORD]

  const getInfractionTypeOptions = () =>
    Object.keys(InfractionType)?.map(key => ({
      value: InfractionType[key as keyof typeof InfractionType],
      label: INFRACTION_TYPE_REGISTRY[key as keyof typeof InfractionType]
    }))

  const getInfractionTypeEnumOptions = () =>
    Object.keys(InfractionTypeEnum)?.map(key => ({
      value: InfractionTypeEnum[key as keyof typeof InfractionTypeEnum],
      label: INFRACTION_TYPE_REGISTRY[key as keyof typeof InfractionTypeEnum]
    }))

  const getInfractionByControlTypeTitle = (type: ControlType) => CONTROL_TYPE_REGISTRY[type].title
  const getInfractionByControlTypeButton = (type: ControlType) => CONTROL_TYPE_REGISTRY[type].button

  return {
    getInfractionTypeTag,
    getInfractionByControlTypeTitle,
    getInfractionByControlTypeButton,
    infractionTypeOptions: getInfractionTypeOptions(),
    infractionTypeEnumOptions: getInfractionTypeEnumOptions()
  }
}
