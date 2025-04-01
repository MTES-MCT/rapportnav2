export const ROOT_PATH = '/'
export const LOGIN_PATH = '/login'
export const SIGNUP_PATH = '/signup'
export const PAM_HOME_PATH = '/pam/missions'
export const PAM_V2_HOME_PATH = '/v2/pam/missions'
export const ULAM_V2_HOME_PATH = '/v2/ulam/missions'
// admin routes
export const ADMIN_CREW_PATH = '/admin/crews'

/**
 * Redirects to a specified ID regardless of whether the current path is PAM or ULAM
 * Works for both cases:
 * - /v2/xxx/missions/${missionId} => /v2/xxx/missions/${missionId}/${id}
 * - /v2/xxx/missions/${missionId}/${oldId} => /v2/xxx/missions/${missionId}/${id}
 *
 * @param {string} id - The new ID to navigate to
 * @param {function} navigate - React Router's navigate function
 */
export const navigateToActionId = (id, navigate) => {
  // Get the current path
  const currentPath = window.location.pathname

  // Split path into segments
  const pathSegments = currentPath.split('/').filter(segment => segment !== '')

  // Check if this is a valid missions path with at least:
  // ["v2", "{pam|ulam}", "missions", "{missionId}"]
  if (
    pathSegments.length >= 4 &&
    pathSegments[0] === 'v2' &&
    (pathSegments[1] === 'pam' || pathSegments[1] === 'ulam') &&
    pathSegments[2] === 'missions'
  ) {
    // Extract the product type (pam or ulam) and mission ID
    const userType = pathSegments[1]
    const missionId = pathSegments[3]

    // Construct the new URL ensuring we only have missionId and the new id
    const newUrl = `/v2/${userType}/missions/${missionId}/${id}`

    // Navigate to the new URL
    navigate(newUrl)
  } else {
    console.error('Invalid path format. Expected /v2/{pam|ulam}/missions/{missionId}[/{oldId}]')
  }
}
