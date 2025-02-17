import { describe, expect, test } from 'vitest'
import { useControlUnitResourceLabel } from '../use-ulam-home-unit-resources.tsx'
import { MissionReportTypeEnum } from '../../../common/types/mission-types.ts'
import { ControlUnitResource } from '../../../common/types/control-unit-types.ts'

describe('useControlUnitResourceLabel', () => {
  test('returns "--" if missionReportType is FIELD_REPORT and resources are empty or undefined', () => {
    expect(useControlUnitResourceLabel([], MissionReportTypeEnum.FIELD_REPORT)).toBe('--');
    expect(useControlUnitResourceLabel(undefined, MissionReportTypeEnum.FIELD_REPORT)).toBe('--');
  });

  test('returns "N/A" if missionReportType is OFFICE_REPORT', () => {
    expect(useControlUnitResourceLabel([{ name: 'Resource A', id: 1, controlUnitId: 1 }], MissionReportTypeEnum.OFFICE_REPORT)).toBe('N/A');
    expect(useControlUnitResourceLabel([], MissionReportTypeEnum.OFFICE_REPORT)).toBe('N/A');
  });

  test('returns a single resource name if there is only one resource in the list', () => {
    const resources: ControlUnitResource[] = [{ name: 'Resource A' }];
    expect(useControlUnitResourceLabel(resources)).toBe('Resource A');
  });

  test('returns multiple resource names separated by ", "', () => {
    const resources: ControlUnitResource[] = [
      { name: 'Resource A' },
      { name: 'Resource B' },
      { name: 'Resource C' }
    ];
    expect(useControlUnitResourceLabel(resources)).toBe('Resource A, Resource B, Resource C');
  });

  test('returns a -- if resources are empty or undefined and missionReportType is not FIELD_REPORT or OFFICE_REPORT', () => {
    expect(useControlUnitResourceLabel([])).toBe('--');
    expect(useControlUnitResourceLabel(undefined)).toBe('--')
    expect(useControlUnitResourceLabel([], MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT)).toBe('--');
  });
});
