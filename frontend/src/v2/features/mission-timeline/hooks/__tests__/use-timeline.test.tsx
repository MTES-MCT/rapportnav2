import { ActionStatusType } from '@common/types/action-types'
import { renderHook } from '@testing-library/react'
import { ActionType } from '../../../common/types/action-type.ts'
import { MissionAction } from '../../../common/types/mission-action.ts'
import { MissionSourceEnum, CompletenessForStatsStatusEnum } from '../../../common/types/mission-types.ts'
import { useTimeline } from '../use-timeline'

const envAction: MissionAction = {
  source: MissionSourceEnum.MONITORENV,
  id: 'env-id',
  missionId: '1',
  actionType: ActionType.SURVEILLANCE,
  isCompleteForStats: true,
  status: ActionStatusType.UNAVAILABLE,
  summaryTags: ['tag1'],
  controlsToComplete: [],
  completenessForStats: { status: CompletenessForStatsStatusEnum.COMPLETE },
  data: {
    startDateTimeUtc: '2025-01-22T13:00:00Z',
    endDateTimeUtc: '2025-01-22T14:30:00Z',
    completedBy: 'KLP',
    facade: 'NAMO',
    department: '29',
    openBy: 'PPP',
    observations: 'obs',
    availableControlTypesForInfraction: []
  }
}

const navAction: MissionAction = {
  source: MissionSourceEnum.RAPPORT_NAV,
  id: 'nav-id',
  missionId: '2',
  actionType: ActionType.ANTI_POLLUTION,
  isCompleteForStats: false,
  status: ActionStatusType.ANCHORED,
  summaryTags: ['tag2'],
  controlsToComplete: [],
  completenessForStats: { status: CompletenessForStatsStatusEnum.INCOMPLETE },
  data: {
    startDateTimeUtc: '2025-01-22T10:00:00Z',
    endDateTimeUtc: '2025-01-22T11:00:00Z',
    observations: 'nav obs',
    controlMethod: 'method',
    vesselIdentifier: 'id',
    vesselType: 'type',
    vesselSize: 'size',
    isVesselRescue: true,
    isPersonRescue: false,
    reason: 'reason',
    nbrOfHours: 2
  }
}

const fishAction: MissionAction = {
  source: MissionSourceEnum.MONITORFISH,
  id: 'fish-id',
  missionId: '3',
  actionType: ActionType.CONTROL_FISH,
  isCompleteForStats: false,
  status: ActionStatusType.PENDING,
  summaryTags: ['tag3'],
  controlsToComplete: [],
  completenessForStats: { status: CompletenessForStatsStatusEnum.INCOMPLETE },
  data: {
    startDateTimeUtc: '2025-01-22T08:00:00Z',
    endDateTimeUtc: '2025-01-22T09:00:00Z',
    fishActionType: 'type',
    vesselId: 'vessel-id',
    vesselName: 'vessel-name',
    observationsByUnit: 'fish obs'
  }
}

describe('useTimeline', () => {
  it('should return missionActionTimeline for env action', () => {
    const { result } = renderHook(() => useTimeline())
    const response = result.current.getTimeLineFromEnvAction(envAction)
    expect(response.id).toBe('env-id')
    expect(response.startDateTimeUtc).toBe('2025-01-22T13:00:00Z')
  })

  it('should return missionActionTimeline for nav action', () => {
    const { result } = renderHook(() => useTimeline())
    const response = result.current.getTimeLineFromNavAction(navAction)
    expect(response.id).toBe('nav-id')
    expect(response.nbrOfHours).toBe(2)
    expect(response.isVesselRescue).toBe(true)
    expect(response.startDateTimeUtc).toBe('2025-01-22T10:00:00Z')
  })

  it('should return missionActionTimeline for fish action', () => {
    const { result } = renderHook(() => useTimeline())
    const response = result.current.getTimeLineFromFishAction(fishAction)
    expect(response.id).toBe('fish-id')
    expect(response.fishActionType).toBe('type')
    expect(response.vesselId).toBe('vessel-id')
    expect(response.observations).toBe('fish obs')
  })

  it('should return timeline actions sorted by startDateTimeUtc desc', () => {
    const { result } = renderHook(() => useTimeline())
    const actions = [fishAction, navAction, envAction]
    const timeline = result.current.getTimeLineAction(actions)
    expect(timeline).toHaveLength(3)
    expect(timeline[0].id).toBe('env-id') // latest date
    expect(timeline[1].id).toBe('nav-id')
    expect(timeline[2].id).toBe('fish-id') // earliest date
  })

  it('should return empty array if no actions provided', () => {
    const { result } = renderHook(() => useTimeline())
    const timeline = result.current.getTimeLineAction(undefined)
    expect(timeline).toEqual([])
  })

  it('should detect incomplete action', () => {
    const { result } = renderHook(() => useTimeline())
    const incomplete = { completenessForStats: { status: CompletenessForStatsStatusEnum.INCOMPLETE } }
    expect(result.current.isIncomplete(incomplete as any)).toBe(true)
    const complete = { completenessForStats: { status: CompletenessForStatsStatusEnum.COMPLETE } }
    expect(result.current.isIncomplete(complete as any)).toBe(false)
    expect(result.current.isIncomplete(undefined)).toBe(false)
  })
})
