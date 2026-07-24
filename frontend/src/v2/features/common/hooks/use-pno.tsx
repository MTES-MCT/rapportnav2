import { PnoType } from '../types/pno-type'

type PnoRegistryTypeRegistry = {
  [key in PnoType]: string
}

const PNO_TYPE_REGISTRY: PnoRegistryTypeRegistry = {
  [PnoType.ACS]: 'Accès aux services',
  [PnoType.ECY]: 'Urgence',
  [PnoType.GRD]: 'Immobilisation et convocation par les autorités',
  [PnoType.LAN]: 'Débarquement',
  [PnoType.OTH]: 'Autre',
  [PnoType.REF]: 'Ravitaillement',
  [PnoType.REP]: 'Réparation',
  [PnoType.RES]: 'Repos',
  [PnoType.SCR]: 'Retour pour Recherche Scientifique',
  [PnoType.SHE]: "Mise à l'abri",
  [PnoType.TRA]: 'Transbordement'
}
interface PnoHook {
  getPnoType: (type?: PnoType) => string | undefined
  pnoTypeOptions: { label: string; value: PnoType }[]
}

export function usePno(): PnoHook {
  const getPnoType = (type?: PnoType) => (type ? PNO_TYPE_REGISTRY[type] : '')
  const getPnoTypeOptions = () =>
    Object.keys(PnoType)?.map(key => ({
      value: PnoType[key as keyof typeof PnoType],
      label: PNO_TYPE_REGISTRY[key as keyof typeof PnoType]
    }))
  return { getPnoType, pnoTypeOptions: getPnoTypeOptions() }
}
