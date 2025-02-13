import { ActionStatusType } from '@common/types/action-types'
import { renderHook } from '@testing-library/react'
import { ActionType } from '../../../../features/common/types/action-type'
import { MissionAction } from '../../../../features/common/types/mission-action'
import { useTimeline } from '../use-timeline'

const action: MissionAction = {
  source: 'MONITORENV',
  id: '4c7fae0f-a6f1-419b-adee-615ec08f40a7',
  missionId: 20309,
  actionType: ActionType.SURVEILLANCE,
  isCompleteForStats: true,
  status: ActionStatusType.UNAVAILABLE,
  summaryTags: ['Sans PV', 'Sans infraction'],
  controlsToComplete: [],
  data: {
    startDateTimeUtc: '2025-01-22T13:00:00Z',
    endDateTimeUtc: '2025-01-22T14:30:00Z',
    completedBy: 'KLP',
    facade: 'NAMO',
    department: '29',
    openBy: 'PPP',
    observations: 'Surveillance rade de Brest - Mission embarquée (zodiac). \n\nRAS',
    formattedControlPlans: [
      {
        theme: 'Espèce protégée et leur habitat (faune et flore)',
        subThemes: ['Dérangement / perturbation intentionnelle des espèces animales protégées'],
        tags: []
      },
      {
        theme: 'Pêche de loisir (autre que PAP)',
        subThemes: ['Pêche sous-marine', 'Pêche embarquée'],
        tags: []
      }
    ],
    availableControlTypesForInfraction: []
  }
}

describe('useTimeline', () => {
  it('should return missionActionTimeline for env action', () => {
    const { result } = renderHook(() => useTimeline())
    const response = result.current.getTimeLineFromEnvAction(action)
    expect(response.formattedControlPlans).toHaveLength(2)
  })
})
