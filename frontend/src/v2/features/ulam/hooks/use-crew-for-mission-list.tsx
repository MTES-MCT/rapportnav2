import { MissionCrew } from '@common/types/crew-types.ts';
import { THEME } from '@mtes-mct/monitor-ui';

const style = {
  color: THEME.color.charcoal,
  fontWeight: 'bold',
  fontSize: '12px',
  whiteSpace: 'nowrap',
  overflow: 'hidden',
  textOverflow: 'ellipsis',
};

export function useCrewForMissionList(missionCrew: MissionCrew[]): { text: string; style: object } {
  const result = missionCrew?.length
    ? missionCrew.map((crew) => `${crew.agent?.firstName || ''} ${crew.agent?.lastName || ''}`.trim()).join(', ')
    : '--';

  return { text: result, style };
}
