import { Icon, IconProps, THEME } from '@mtes-mct/monitor-ui'
import { FunctionComponent } from 'react'
import { InquiryStatusType } from '../../common/types/inquiry'

type Component = {
  text: string
  color: string
  icon: FunctionComponent<IconProps>
}

type StatusComponent = { [key in InquiryStatusType]: Component }

const INQUIRY_STATUS: StatusComponent = {
  [InquiryStatusType.IN_PROGRESS]: {
    text: 'En cours',
    icon: Icon.Clock,
    color: THEME.color.blueGray
  },
  [InquiryStatusType.CLOSED]: {
    text: 'Terminée',
    icon: Icon.Confirm,
    color: THEME.color.charcoal
  }
}

export function useInquiryStatus(status: InquiryStatusType): Component {
  return INQUIRY_STATUS[status]
}
