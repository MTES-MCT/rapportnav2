import { render, screen } from '../../../../../../test-utils.tsx'
import { ActionType } from '../../../types/action-type.ts'
import TimelineItemStatus from '../timeline-item-status.tsx'
import { CompletenessForStatsStatusEnum } from '../../../types/mission-types.ts'

describe('TimelineItemStatus', () => {
  it('should display a message when unsync', () => {
    render(<TimelineItemStatus type={ActionType.NOTE} isUnsync={true} />)
    expect(
      screen.getByTitle(
        'Cet évènement contient peut-être des données manquantes indispensables pour les statistiques. Le status sera actualisé quand vous repasserez en ligne.'
      )
    ).toBeInTheDocument()
  })
  it('should display a message when incomplete', () => {
    render(
      <TimelineItemStatus
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
      <TimelineItemStatus
        type={ActionType.NOTE}
        isUnsync={false}
        completenessForStats={{ status: CompletenessForStatsStatusEnum.COMPLETE }}
      />
    )
    expect(screen.getByTestId('timeline-item-status')).toBeInTheDocument()
  })
})
