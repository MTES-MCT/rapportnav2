import { describe, expect, it } from 'vitest'
import { InquiryConclusionType, InquiryOriginType, InquiryTargetType } from '../../../common/types/inquiry'
import { useInquiry } from '../use-inquiry'

describe('useInquiry', () => {
  const inquiry = useInquiry()

  it('should return available control types', () => {
    expect(inquiry.availableControlTypes.length).toEqual(11)
  })

  describe('getInquiryOriginType', () => {
    it('should return origin type label when type is provided', () => {
      expect(inquiry.getInquiryOriginType(InquiryOriginType.CNSP_REPORTING)).toBe('Signalement CNSP')
    })

    it('should return empty string when no type is provided', () => {
      expect(inquiry.getInquiryOriginType()).toBe('')
    })
  })

  describe('getInquiryTargetType', () => {
    it('should return target type label when type is provided', () => {
      expect(inquiry.getInquiryTargetType(InquiryTargetType.VEHICLE)).toBe('Navire')
    })

    it('should return empty string when no type is provided', () => {
      expect(inquiry.getInquiryTargetType()).toBe('')
    })
  })

  describe('getInquiryConclusionType', () => {
    it('should return conclusion type label when type is provided', () => {
      expect(inquiry.getInquiryConclusionType(InquiryConclusionType.NO_FOLLOW_UP)).toBe('Sans suite')
    })

    it('should return empty string when no type is provided', () => {
      expect(inquiry.getInquiryConclusionType()).toBe('')
    })
  })

  describe('options arrays', () => {
    it('should return inquiry origin options', () => {
      expect(inquiry.inquiryOriginOptions).toHaveLength(Object.keys(InquiryOriginType).length)
      expect(inquiry.inquiryOriginOptions[0]).toHaveProperty('label')
      expect(inquiry.inquiryOriginOptions[0]).toHaveProperty('value')
    })

    it('should return inquiry target options', () => {
      expect(inquiry.inquiryTargetOptions).toHaveLength(Object.keys(InquiryTargetType).length)
      expect(inquiry.inquiryTargetOptions[0]).toHaveProperty('label')
      expect(inquiry.inquiryTargetOptions[0]).toHaveProperty('value')
    })

    it('should return inquiry conclusion options', () => {
      expect(inquiry.inquiryConclusionOptions).toHaveLength(Object.keys(InquiryConclusionType).length)
      expect(inquiry.inquiryConclusionOptions[0]).toHaveProperty('label')
      expect(inquiry.inquiryConclusionOptions[0]).toHaveProperty('value')
    })

    it('should return false when inquiry is undefined', () => {
      expect(inquiry.isClosable(undefined)).toBe(false)
    })

    it('should return incomplete status when inquiry is undefined', () => {
      expect(inquiry.getStatusReport(undefined)).toEqual({
        text: 'À compléter',
        icon: expect.any(Function),
        color: expect.any(String)
      })
    })

    it('should return incomplete status when inquiry is not valid', () => {
      const invalidInquiry = {} as any
      expect(inquiry.getStatusReport(invalidInquiry)).toEqual({
        text: 'À compléter',
        icon: expect.any(Function),
        color: expect.any(String)
      })
    })

    it('should return complete status when inquiry type Vehicle is valid and all actions are complete', () => {
      const validInquiry = {
        //agentId: 'myAgent',
        type: InquiryTargetType.VEHICLE,
        isSignedByInspector: false,
        vessel: {
          vesselId: 233434
        },
        establishment: {
          name: null
        },
        origin: InquiryOriginType.CNSP_REPORTING,
        endDateTimeUtc: '2022-08-11T12:00:00Z',
        startDateTimeUtc: '2022-08-07T12:00:00Z',
        actions: [
          {
            completenessForStats: {
              status: 'COMPLETE'
            }
          }
        ]
      } as any

      expect(inquiry.getStatusReport(validInquiry)).toEqual({
        text: 'Données à jour',
        icon: expect.any(Function),
        color: expect.any(String)
      })
    })

    it('should return complete status when inquiry type COMPANY is valid and all actions are complete', () => {
      const validInquiry = {
        agentId: 'myAgent',
        type: InquiryTargetType.COMPANY,
        isSignedByInspector: false,
        establishment: {
          name: '233434'
        },
        vessel: {
          vesselId: null
        },
        origin: InquiryOriginType.CNSP_REPORTING,
        endDateTimeUtc: '2022-08-11T12:00:00Z',
        startDateTimeUtc: '2022-08-07T12:00:00Z',
        actions: [
          {
            completenessForStats: {
              status: 'COMPLETE'
            }
          }
        ]
      } as any

      expect(inquiry.getStatusReport(validInquiry)).toEqual({
        text: 'Données à jour',
        icon: expect.any(Function),
        color: expect.any(String)
      })
    })
  })
})
