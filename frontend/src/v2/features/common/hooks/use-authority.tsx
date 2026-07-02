import { AuthorityType } from '../types/sati'

type AuthorityRegistryTypeRegistry = {
  [key in AuthorityType]: string
}

const AUTHORITY_TYPE_REGISTRY: AuthorityRegistryTypeRegistry = {
  [AuthorityType.MEMBER_FR]: 'Etat membre (FR)',
  [AuthorityType.AECP]: 'AECP',
  [AuthorityType.OTHERS]: 'Autre'
}
interface AuthorityHook {
  authorityTypeOptions: { label: string; value: AuthorityType }[]
  getAuthorityType: (type?: AuthorityType) => string | undefined
}

export function useAuthority(): AuthorityHook {
  const getAuthorityType = (type?: AuthorityType) => (type ? AuthorityType[type] : '')
  const getAuthorityTypeOptions = () =>
    Object.keys(AuthorityType)?.map(key => ({
      value: AuthorityType[key as keyof typeof AuthorityType],
      label: AUTHORITY_TYPE_REGISTRY[key as keyof typeof AuthorityType]
    }))
  return { getAuthorityType, authorityTypeOptions: getAuthorityTypeOptions() }
}
