import { MissionReportTypeEnum } from '../../common/types/mission-types.ts'
import { ControlUnit } from '@common/types/control-unit-types.ts'


export function useControlUnitResourceLabel(
  controlUnits?: ControlUnit[],
  missionReportType?: MissionReportTypeEnum,
  currentControlUnitId?: number
): string {

  if(missionReportType === MissionReportTypeEnum.OFFICE_REPORT) return 'N/A'

  if (!controlUnits || controlUnits.length === 0) return '--'

  const currentControlUnit = controlUnits.find(unit => unit.id === currentControlUnitId)

  const resources = currentControlUnit?.resources

  if (!resources || resources.length === 0) {
    return '--';
  }

  return (resources ?? []).map(resource => resource.name).join(', ');
}
