import { describe, expect, test } from 'vitest'
import { useControlUnitResourceLabel } from '../use-ulam-home-unit-resources.tsx'
import { MissionReportTypeEnum } from '../../../common/types/mission-types.ts'

const controlUnit1 = {
  id: 1,
  name: 'ControlUnit1',
  administration: 'Administration',
  isArchived: false,
  resources: [
    { id: 1, controlUnitId: 1, name: 'Resource A' }
  ]
}

const controlUnit2 = {
  id: 2,
  name: 'ControlUnit2',
  administration: 'Administration',
  isArchived: false,
  resources: [
    { id: 1, controlUnitId: 2, name: 'Resource A' },
    { id: 2, controlUnitId: 2, name: 'Resource B' },
    { id: 3, controlUnitId: 2, name: 'Resource C' }
  ]
}

describe('useControlUnitResourceLabel', () => {

  // Teste lorsque les resources sont vides ou non définies avec un FIELD_REPORT
  test('returns "--" if missionReportType is FIELD_REPORT and resources are empty or undefined', () => {
    expect(useControlUnitResourceLabel([], MissionReportTypeEnum.FIELD_REPORT)).toBe('--')
    expect(useControlUnitResourceLabel(undefined, MissionReportTypeEnum.FIELD_REPORT)).toBe('--')
  })

  // Teste lorsque le type de rapport est OFFICE_REPORT
  test('returns "N/A" if missionReportType is OFFICE_REPORT or EXTERNAL_REINFORCEMENT_TIME', () => {
    expect(useControlUnitResourceLabel([controlUnit1], MissionReportTypeEnum.OFFICE_REPORT)).toBe('N/A')
    expect(useControlUnitResourceLabel([], MissionReportTypeEnum.OFFICE_REPORT)).toBe('N/A')
    expect(useControlUnitResourceLabel([], MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT)).toBe('N/A')
  })

  // Teste lorsque seule une ressource existe
  test('returns a single resource name if there is only one resource in the list', () => {
    expect(useControlUnitResourceLabel([controlUnit1], MissionReportTypeEnum.FIELD_REPORT, 1)).toBe('Resource A')
  })

  // Teste lorsque plusieurs ressources existent
  test('returns multiple resource names separated by ", "', () => {
    expect(useControlUnitResourceLabel([controlUnit2], MissionReportTypeEnum.FIELD_REPORT, 2)).toBe('Resource A, Resource B, Resource C')
  })

  // Teste lorsque les ressources sont vides et missionReportType n'est pas FIELD_REPORT ou OFFICE_REPORT
  test('returns "--" if resources are empty or undefined', () => {
    expect(useControlUnitResourceLabel([])).toBe('--')
    expect(useControlUnitResourceLabel(undefined)).toBe('--')
  })

})
