export type ServiceType = 'PAM' | 'ULAM'

export interface ImpersonationState {
  isImpersonating: boolean
  targetServiceId?: number
  targetServiceName?: string
  targetServiceType?: ServiceType
}

export interface ImpersonationService {
  id: number
  name: string
  serviceType: string
}
