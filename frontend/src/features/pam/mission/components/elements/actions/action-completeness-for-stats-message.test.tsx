import { render, screen } from '../../../../../../test-utils.tsx'
import { CompletenessForStatsStatusEnum } from '../../../../../common/types/mission-types.ts'
import { THEME } from '@mtes-mct/monitor-ui'
import { hexToRgb } from '../../../../../common/utils/colors.ts'
import { MissionSourceEnum } from '../../../../../common/types/env-mission-types.ts'
import ActionCompletenessForStatsMessage from './action-completeness-for-stats-message.tsx'

describe('ActionCompletenessForStatsMessage', () => {
  test('renders that everything is complete when mission has ended and data is complete', () => {
    render(
      <ActionCompletenessForStatsMessage
        isMissionFinished={true}
        completenessForStats={{
          status: CompletenessForStatsStatusEnum.COMPLETE,
          sources: undefined
        }}
      />
    )
    const element = screen.getByText('Les champs indispensables aux statistiques sont remplis.')
    expect(element).toBeInTheDocument()
    expect(getComputedStyle(element).color).toBe(hexToRgb(THEME.color.mediumSeaGreen))
  })
  test('renders data missing text in red when mission is ended', () => {
    render(
      <ActionCompletenessForStatsMessage
        isMissionFinished={true}
        completenessForStats={{
          status: CompletenessForStatsStatusEnum.INCOMPLETE,
          sources: [MissionSourceEnum.RAPPORTNAV]
        }}
      />
    )
    const element = screen.getByText("Des champs indispensables sont à remplir par l'unité.")
    expect(element).toBeInTheDocument()
    expect(getComputedStyle(element).color).toBe(hexToRgb(THEME.color.maximumRed))
  })
  test('renders data missing text in charcoal when mission is ongoing', () => {
    render(
      <ActionCompletenessForStatsMessage
        completenessForStats={{
          status: CompletenessForStatsStatusEnum.INCOMPLETE,
          sources: [MissionSourceEnum.RAPPORTNAV]
        }}
      />
    )
    const element = screen.getByText("Des champs indispensables sont à remplir par l'unité.")
    expect(element).toBeInTheDocument()
    expect(getComputedStyle(element).color).toBe(hexToRgb(THEME.color.charcoal))
  })
  test('renders data missing text from CACEM', () => {
    render(
      <ActionCompletenessForStatsMessage
        completenessForStats={{
          status: CompletenessForStatsStatusEnum.INCOMPLETE,
          sources: [MissionSourceEnum.MONITORENV]
        }}
      />
    )
    const element = screen.getByText('Des champs indispensables sont à remplir par le CACEM.')
    expect(element).toBeInTheDocument()
  })
  test('renders data missing text from CNSP', () => {
    render(
      <ActionCompletenessForStatsMessage
        completenessForStats={{
          status: CompletenessForStatsStatusEnum.INCOMPLETE,
          sources: [MissionSourceEnum.MONITORFISH]
        }}
      />
    )
    const element = screen.getByText('Des champs indispensables sont à remplir par le CNSP.')
    expect(element).toBeInTheDocument()
  })
  test('renders the incomplete data icon', () => {
    render(
      <ActionCompletenessForStatsMessage
        completenessForStats={{
          status: CompletenessForStatsStatusEnum.INCOMPLETE,
          sources: [MissionSourceEnum.RAPPORTNAV]
        }}
      />
    )
    expect(screen.getByTestId('report-incomplete')).toBeInTheDocument()
  })
  test('renders the complete data icon', () => {
    render(
      <ActionCompletenessForStatsMessage
        completenessForStats={{
          status: CompletenessForStatsStatusEnum.COMPLETE,
          sources: undefined
        }}
      />
    )
    expect(screen.getByTestId('report-complete')).toBeInTheDocument()
  })
})
