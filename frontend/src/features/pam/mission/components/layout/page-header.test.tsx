import { render, screen } from '../../../../../test-utils.tsx'
import { Mission, MissionStatusEnum } from '../../../../common/types/mission-types.ts'
import MissionPageHeader from './page-header.tsx'
import { vi } from 'vitest'

describe('MissionPageHeader', () => {
  describe('the banner', () => {
    test('should be visible when the mission is finished', () => {
      const finishedMission = { status: MissionStatusEnum.ENDED } as Mission
      render(<MissionPageHeader mission={finishedMission} onClickClose={vi.fn()} onClickExport={vi.fn()} />)
      const element = screen.getByText('Masquer')
      expect(element).toBeInTheDocument()
    })
    test('should not be visible when the mission is not finished', () => {
      const finishedMission = { status: MissionStatusEnum.IN_PROGRESS } as Mission
      render(<MissionPageHeader mission={finishedMission} onClickClose={vi.fn()} onClickExport={vi.fn()} />)
      const element = screen.queryByText('Masquer')
      expect(element).toBeNull()
    })
  })
})
