export type UserAuthLogging = {
  id: string
  userId?: number
  email: string
  eventType: AuthEventTypeEnum
  ipAddress?: string
  userAgent?: string
  success: boolean
  failureReason: string
  timestamp: string
}

export enum AuthEventTypeEnum {
  LOGIN_SUCCESS = 'LOGIN_SUCCESS',
  LOGIN_FAILURE = 'LOGIN_FAILURE',
  LOGOUT = 'LOGOUT'
}
