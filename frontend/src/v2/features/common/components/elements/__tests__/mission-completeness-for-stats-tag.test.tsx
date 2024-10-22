import { CompletenessForStatsStatusEnum, MissionStatusEnum } from '@common/types/mission-types.ts'
import { render, screen } from '../../../../../../test-utils'
import MissionCompletenessForStatsTag from '../mission-completeness-for-stats-tag'

describe('MissionCompletenessForStatsTag component', () => {
  test('renders "À compléter" when missionStatus is undefined', () => {
    render(
      <MissionCompletenessForStatsTag
        completenessForStats={{ status: CompletenessForStatsStatusEnum.INCOMPLETE }}
        missionStatus={undefined}
      />
    )
    const tagElement = screen.getByText('À compléter')
    expect(tagElement).toBeInTheDocument()
  })
  test('renders "À compléter" when completenessForStats is undefined', () => {
    render(
      <MissionCompletenessForStatsTag completenessForStats={undefined} missionStatus={MissionStatusEnum.UPCOMING} />
    )
    const tagElement = screen.getByText('À compléter')
    expect(tagElement).toBeInTheDocument()
  })
  test('renders "Données à jour" when missionStatus is IN_PROGRESS and completenessForStats is COMPLETE', () => {
    render(
      <MissionCompletenessForStatsTag
        completenessForStats={{ status: CompletenessForStatsStatusEnum.COMPLETE }}
        missionStatus={MissionStatusEnum.IN_PROGRESS}
      />
    )
    const tagElement = screen.getByText('Données à jour')
    expect(tagElement).toBeInTheDocument()
  })
  test('renders "À compléter" when missionStatus is IN_PROGRESS and completenessForStats is COMPLETE', () => {
    render(
      <MissionCompletenessForStatsTag
        completenessForStats={{ status: CompletenessForStatsStatusEnum.INCOMPLETE }}
        missionStatus={MissionStatusEnum.IN_PROGRESS}
      />
    )
    const tagElement = screen.getByText('À compléter')
    expect(tagElement).toBeInTheDocument()
  })
  test('renders "Complété" when missionStatus is ENDED and completenessForStats is COMPLETE', () => {
    render(
      <MissionCompletenessForStatsTag
        completenessForStats={{ status: CompletenessForStatsStatusEnum.COMPLETE }}
        missionStatus={MissionStatusEnum.ENDED}
      />
    )
    const tagElement = screen.getByText('Complété')
    expect(tagElement).toBeInTheDocument()
  })
  test('renders "À compléter" when missionStatus is ENDED and completenessForStats is COMPLETE', () => {
    render(
      <MissionCompletenessForStatsTag
        missionStatus={MissionStatusEnum.ENDED}
        completenessForStats={{ status: CompletenessForStatsStatusEnum.INCOMPLETE }}
      />
    )
    const tagElement = screen.getByText('À compléter')
    expect(tagElement).toBeInTheDocument()
  })
})
