import { renderHook } from '@testing-library/react'
import { describe, expect } from 'vitest'
import { MissionReportTypeEnum } from '../../../common/types/mission-types.ts'
import { useUlamTimelineRegistry } from '../use-ulam-timeline-registry.tsx'

describe('useUlamTimelineRegistry', () => {
  it('should return no timeline items for EXTERNAL_REINFORCEMENT_TIME_REPORT', () => {
    const { result } = renderHook(() => useUlamTimelineRegistry())
    const response = result.current.getTimelineDropdownItems(MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT)
    expect(response.length).toEqual(0)
  })

  it('should return  timeline items only for OFFICE_REPORT', () => {
    const { result } = renderHook(() => useUlamTimelineRegistry())
    const response = result.current.getTimelineDropdownItems(MissionReportTypeEnum.OFFICE_REPORT)
    expect(response.map(t => t.type)).toEqual([
      'ADMINISTRATIVE_GROUP',
      'COMMUNICATION',
      'TRAINING',
      'NOTE',
      'UNIT_MANAGEMENT_GROUP'
    ])
  })

  it('should return every timeline items ', () => {
    const { result } = renderHook(() => useUlamTimelineRegistry())
    const response = result.current.getTimelineDropdownItems(MissionReportTypeEnum.FIELD_REPORT)
    expect(response.length).toEqual(9)
  })
})
