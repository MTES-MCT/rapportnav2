import { MissionSourceEnum } from '@common/types/env-mission-types'
import { vi } from 'vitest'
import { render, screen, waitFor } from '../../../../../../test-utils'
import { CompletenessForStatsStatusEnum, Mission2, MissionStatusEnum } from '../../../types/mission-types'
import MissionPageHeader from '../mission-page-header'

const onClose = vi.fn()
const onExport = vi.fn()

describe('MissionPageHeader', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.clearAllTimers()
  })
  it('should match the snapshot', () => {
    const mission = {
      status: MissionStatusEnum.IN_PROGRESS,
      completenessForStats: {
        sources: [MissionSourceEnum.MONITORENV],
        status: CompletenessForStatsStatusEnum.INCOMPLETE
      },
      envData: { startDateTimeUtc: '2025-02-15T04:50:09Z', missionSource: MissionSourceEnum.RAPPORTNAV }
    } as Mission2

    const wrapper = render(<MissionPageHeader onClickClose={onClose} onClickExport={onExport} mission={mission} />)
    expect(wrapper).toMatchSnapshot()
  })

  it('should show export only of mission completenessForStat is complete', () => {
    const mission = {
      completenessForStats: {
        sources: [MissionSourceEnum.MONITORENV],
        status: CompletenessForStatsStatusEnum.COMPLETE
      },
      envData: { startDateTimeUtc: '2025-02-15T04:50:09Z', missionSource: MissionSourceEnum.RAPPORTNAV }
    } as Mission2

    render(<MissionPageHeader onClickClose={onClose} onClickExport={onExport} mission={mission} />)
    expect(screen.getByText('Exporter le rapport de la mission')).toBeInTheDocument()
  })

  it('should show banner message', async () => {
    const mission = {
      status: MissionStatusEnum.ENDED,
      completenessForStats: {
        sources: [MissionSourceEnum.MONITORENV],
        status: CompletenessForStatsStatusEnum.COMPLETE
      }
    } as Mission2

    render(<MissionPageHeader onClickClose={onClose} onClickExport={onExport} mission={mission} />)
    waitFor(() => expect(screen.getByText('La mission est terminée et ses données sont complètes')).toBeInTheDocument())
  })
})
