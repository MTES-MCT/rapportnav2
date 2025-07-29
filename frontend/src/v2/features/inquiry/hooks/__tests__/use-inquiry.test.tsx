import { ControlType } from '@common/types/control-types'
import { describe, expect, it } from 'vitest'
import {
  InquiryConclusionType,
  InquiryOriginType,
  InquiryStatusType,
  InquiryTargetType
} from '../../../common/types/inquiry'
import { useInquiry } from '../use-inquiry'

describe('useInquiry', () => {
  const inquiry = useInquiry()

  it('should return available control types', () => {
    expect(inquiry.availableControlTypes).toEqual([
      ControlType.SECURITY,
      ControlType.NAVIGATION,
      ControlType.GENS_DE_MER,
      ControlType.ADMINISTRATIVE
    ])
  })

  describe('getInquiryOriginType', () => {
    it('should return origin type label when type is provided', () => {
      expect(inquiry.getInquiryOriginType(InquiryOriginType.CNSP_REPORTING)).toBe('Signalement CNSP')
    })

    it('should return empty string when no type is provided', () => {
      expect(inquiry.getInquiryOriginType()).toBe('')
    })
  })

  describe('getInquiryStatusType', () => {
    it('should return status type label when type is provided', () => {
      expect(inquiry.getInquiryStatusType(InquiryStatusType.NEW)).toBe('Nouveau contrôle croisé')
    })

    it('should return empty string when no type is provided', () => {
      expect(inquiry.getInquiryStatusType()).toBe('')
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

    it('should return inquiry status options excluding CLOSED status', () => {
      expect(inquiry.inquiryStatusOptions).toHaveLength(Object.keys(InquiryStatusType).length - 1)
      expect(inquiry.inquiryStatusOptions.find(option => option.value === InquiryStatusType.CLOSED)).toBeUndefined()
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
  })
})
