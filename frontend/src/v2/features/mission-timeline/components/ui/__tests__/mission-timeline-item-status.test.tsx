import { render, screen } from '../../../../../../test-utils'
import MissionTimelineItemStatus from '../mission-timeline-item-status.tsx'
import { ActionType } from '../../../../common/types/action-type.ts'
import { CompletenessForStatsStatusEnum } from '@common/types/mission-types.ts'

describe('MissionTimelineItemStatus', () => {
  it('should display a message when unsync', () => {
    render(<MissionTimelineItemStatus type={ActionType.NOTE} isUnsync={true} />)
    expect(
      screen.getByTitle(
        'Cet évènement contient peut-être des données manquantes indispensables pour les statistiques. Le status sera actualisé quand vous repasserez en ligne.'
      )
    ).toBeInTheDocument()
  })
  it('should display a message when incomplete', () => {
    render(
      <MissionTimelineItemStatus
        type={ActionType.NOTE}
        isUnsync={false}
        completenessForStats={{ status: CompletenessForStatsStatusEnum.INCOMPLETE }}
      />
    )
    expect(
      screen.getByTitle('Cet évènement contient des données manquantes indispensables pour les statistiques.')
    ).toBeInTheDocument()
  })
  it('should display the status otherwise', () => {
    render(
      <MissionTimelineItemStatus
        type={ActionType.NOTE}
        isUnsync={false}
        completenessForStats={{ status: CompletenessForStatsStatusEnum.COMPLETE }}
      />
    )
    expect(screen.getByTestId('timeline-item-status')).toBeInTheDocument()
  })
})
