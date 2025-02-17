import { ControlUnitResource } from '../../common/types/control-unit-types.ts'
import { MissionReportTypeEnum } from '../../common/types/mission-types.ts'


export function useControlUnitResourceLabel(
  resources?: ControlUnitResource[],
  missionReportType?: MissionReportTypeEnum
): string {

  if (missionReportType !== MissionReportTypeEnum.OFFICE_REPORT && (!resources || resources.length === 0)) {
    return '--';
  }

  if(missionReportType === MissionReportTypeEnum.OFFICE_REPORT) return 'N/A'

  return (resources ?? []).map(resource => resource.name).join(', ');
}
