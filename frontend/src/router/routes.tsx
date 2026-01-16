export const ROOT_PATH = '/'
export const LOGIN_PATH = '/login'
export const SIGNUP_PATH = '/signup'

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
  // ["{pam|ulam}", "missions", "{missionId}"]
  if (
    pathSegments.length >= 3 &&
    (pathSegments[0] === 'pam' || pathSegments[0] === 'ulam') &&
    pathSegments[1] === 'missions'
  ) {
    // Extract the product type (pam or ulam) and mission ID
    const userType = pathSegments[0]
    const missionId = pathSegments[2]

    // Construct the new URL ensuring we only have missionId and the new id
    const newUrl = `/${userType}/missions/${missionId}/${id}`

    // Navigate to the new URL
    navigate(newUrl)
  } else {
    console.error('Invalid path format. Expected /{pam|ulam}/missions/{missionId}[/{oldId}]')
  }
}
