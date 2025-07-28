import { Icon } from '@mtes-mct/monitor-ui'
import { OwnerType } from '../v2/features/common/types/owner-type'

export const ROOT_PATH = '/'
export const LOGIN_PATH = '/login'
export const SIGNUP_PATH = '/signup'
export const PAM_HOME_PATH = '/pam/missions'
export const PAM_V2_HOME_PATH = '/v2/pam/missions'
export const ULAM_V2_HOME_PATH = '/v2/ulam/missions'
// admin routes
export const ADMIN_CREW_PATH = '/admin/crews'

export const PAM_SIDEBAR_ITEMS = [
  {
    key: 'list',
    url: OwnerType.MISSION,
    icon: Icon.MissionAction
  }
]

export const ULAM_SIDEBAR_ITEMS = [
  {
    key: 'list',
    url: OwnerType.MISSION,
    icon: Icon.MissionAction
  },
  {
    key: 'inquiries',
    url: OwnerType.INQUIRY,
    icon: Icon.Archive
  }
]
