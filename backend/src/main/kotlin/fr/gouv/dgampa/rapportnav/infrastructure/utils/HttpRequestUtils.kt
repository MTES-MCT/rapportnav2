package fr.gouv.dgampa.rapportnav.infrastructure.utils

import jakarta.servlet.http.HttpServletRequest

object HttpRequestUtils {

    /**
     * Extracts the client IP address from the request.
     *
     * Note: X-Forwarded-For is handled automatically by ForwardedHeaderFilter
     * (configured via server.forward-headers-strategy=framework).
     * This method only checks X-Real-IP as a fallback for nginx configurations that use it.
     *
     * @param request The HTTP servlet request
     * @return The client IP address, or "unknown" if not available
     */
    fun getClientIp(request: HttpServletRequest): String {
        val xRealIp = request.getHeader("X-Real-IP")
        if (!xRealIp.isNullOrBlank()) return xRealIp.trim()
        return request.remoteAddr ?: "unknown"
    }

    /**
     * Extracts the User-Agent header from the request.
     *
     * @param request The HTTP servlet request
     * @return The User-Agent string, or null if not present
     */
    fun getUserAgent(request: HttpServletRequest): String? {
        return request.getHeader("User-Agent")
    }
}
