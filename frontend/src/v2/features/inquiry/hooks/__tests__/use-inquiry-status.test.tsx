import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { describe, expect, it } from 'vitest'
import { InquiryStatusType } from '../../../common/types/inquiry'
import { useInquiryStatus } from '../use-inquiry-status'

describe('useInquiryStatus', () => {
  it('should return correct values for IN PROGRESS status', () => {
    const result = useInquiryStatus(InquiryStatusType.IN_PROGRESS)
    expect(result).toEqual({
      text: 'En cours',
      icon: Icon.Clock,
      color: THEME.color.blueGray
    })
  })

  it('should return correct values for CLOSED status', () => {
    const result = useInquiryStatus(InquiryStatusType.CLOSED)
    expect(result).toEqual({
      text: 'Terminée',
      icon: Icon.Confirm,
      color: THEME.color.charcoal
    })
  })
})
