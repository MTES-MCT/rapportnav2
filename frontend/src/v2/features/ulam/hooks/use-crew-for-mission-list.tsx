import { MissionCrew } from '@common/types/crew-types.ts';
import { THEME } from '@mtes-mct/monitor-ui';

export function useCrewForMissionList(missionCrew: MissionCrew[]): { text: string; style: object } {
  const result = missionCrew?.length
    ? missionCrew.map((crew) => `${crew.agent?.firstName || ''} ${crew.agent?.lastName || ''}`.trim()).join(', ')
    : '--';

  return { text: result };
}
