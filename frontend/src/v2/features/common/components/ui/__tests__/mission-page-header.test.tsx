import { MissionSourceEnum } from '@common/types/env-mission-types'
import { vi } from 'vitest'
import { render, screen, waitFor } from '../../../../../../test-utils'
import { CompletenessForStatsStatusEnum, Mission2, MissionStatusEnum } from '../../../types/mission-types'
import MissionPageHeader from '../mission-page-header'

const onClose = vi.fn()

describe('MissionPageHeader', () => {
  it('should show banner message', async () => {
    const mission = {
      status: MissionStatusEnum.ENDED,
      completenessForStats: {
        sources: [MissionSourceEnum.MONITORENV],
        status: CompletenessForStatsStatusEnum.COMPLETE
      }
    } as Mission2

    render(<MissionPageHeader onClickClose={onClose} mission={mission} />)
    waitFor(() => expect(screen.getByText('La mission est terminée et ses données sont complètes')).toBeInTheDocument())
  })
})
