export type ApiKeyLogging = {
  id: number
  apiKeyId?: string
  ipAddress?: string
  requestPath?: string
  success: boolean
  failureReason?: string
  timestamp: Instant
}
