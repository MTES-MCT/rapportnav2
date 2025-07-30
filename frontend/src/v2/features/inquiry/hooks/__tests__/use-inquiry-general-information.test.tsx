import { UTCDate } from '@date-fns/utc'
import { describe, expect, it, vi } from 'vitest'
import { renderHook } from '../../../../../test-utils'
import { Inquiry, InquiryTargetType } from '../../../common/types/inquiry'
import { useInquiryGeneralInformation } from '../use-inquiry-general-information'

describe('useInquiryGeneralInformation', () => {
  const mockInquiry = {
    id: '1qsdqsDQS',
    type: InquiryTargetType.VEHICLE,
    vesselId: 1,
    startDateTimeUtc: '2023-01-01T00:00:00Z',
    endDateTimeUtc: '2023-01-02T00:00:00Z',
    isSignedByInspector: false
  } as Inquiry

  const mockOnChange = vi.fn()

  it('should remove vesselId when type is COMPANY', async () => {
    const { result } = renderHook(() => useInquiryGeneralInformation(mockInquiry, mockOnChange))

    const input = {
      ...mockInquiry,
      type: InquiryTargetType.COMPANY,
      dates: [new UTCDate('2023-01-01T00:00:00Z'), new UTCDate('2023-01-02T00:00:00Z')]
    }

    await result.current.handleSubmit(input)

    expect(mockOnChange).toHaveBeenCalledWith({
      ...mockInquiry,
      type: InquiryTargetType.COMPANY,
      vesselId: undefined,
      startDateTimeUtc: '2023-01-01T00:00:00.000Z',
      endDateTimeUtc: '2023-01-02T00:00:00.000Z'
    })
  })
})
