/** URL-safe Base64 encoding as required by JWT spec (RFC 7519) */
function base64url(str: string): string {
  return btoa(str).replace(/\+/g, '-').replace(/\//g, '_').replace(/=/g, '')
}

/** Creates a test JWT token that jwt-decode can parse. No signature verification on frontend. */
export function createTestJwt(payload: { userId: number; roles: string[] }): string {
  const header = base64url(JSON.stringify({ alg: 'HS256', typ: 'JWT' }))
  const body = base64url(
    JSON.stringify({
      ...payload,
      exp: Math.floor(Date.now() / 1000) + 3600,
      iat: Math.floor(Date.now() / 1000)
    })
  )
  return `${header}.${body}.${base64url('fake-signature')}`
}

/** Creates an expired JWT for testing auth expiry scenarios */
export function createExpiredTestJwt(payload: { userId: number; roles: string[] }): string {
  const header = base64url(JSON.stringify({ alg: 'HS256', typ: 'JWT' }))
  const body = base64url(
    JSON.stringify({
      ...payload,
      exp: Math.floor(Date.now() / 1000) - 3600,
      iat: Math.floor(Date.now() / 1000) - 7200
    })
  )
  return `${header}.${body}.${base64url('fake-signature')}`
}
