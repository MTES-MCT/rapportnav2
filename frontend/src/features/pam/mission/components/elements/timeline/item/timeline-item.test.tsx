import { render, screen } from '../../../../../../../test-utils.tsx'
import MissionTimelineItem from './timeline-item.tsx'
import { ActionTypeEnum, MissionSourceEnum } from '../../../../../../common/types/env-mission-types.ts'
import { Action } from '../../../../../../common/types/action-types.ts'

const props = (action: Action) => ({
  action,
  onClick: vi.fn()
})
describe('MissionTimelineItem', () => {
  test('should null when actionSource is not supported ', () => {
    const action = { source: undefined, type: undefined }
    const { container } = render(<MissionTimelineItem {...props(action)} />)
    expect(container.firstElementChild).toBeNull()
  })

  describe('Env', () => {
    test('should null when actionSource is not supported ', () => {
      const action = { source: MissionSourceEnum.MONITORENV, type: undefined }
      const { container } = render(<MissionTimelineItem {...props(action)} />)
      expect(container.firstElementChild).toBeNull()
    })
    test('should show content for Control', () => {
      const action = { source: MissionSourceEnum.MONITORENV, type: ActionTypeEnum.CONTROL }
      render(<MissionTimelineItem {...props(action)} />)
      expect(screen.getByText('ajouté par CACEM')).toBeInTheDocument()
    })
  })

  describe('Fish', () => {
    test('should null when actionSource is not supported ', () => {
      const action = { source: MissionSourceEnum.MONITORFISH, type: undefined }
      const { container } = render(<MissionTimelineItem {...props(action)} />)
      expect(container.firstElementChild).toBeNull()
    })
    test('should show content for Control', () => {
      const action = { source: MissionSourceEnum.MONITORFISH, type: ActionTypeEnum.CONTROL }
      render(<MissionTimelineItem {...props(action)} />)
      expect(screen.getByText('ajouté par CNSP')).toBeInTheDocument()
    })
  })

  describe('Nav', () => {
    test('should null when actionSource is not supported ', () => {
      const action = { source: MissionSourceEnum.RAPPORTNAV, type: undefined }
      const { container } = render(<MissionTimelineItem {...props(action)} />)
      expect(container.firstElementChild).toBeNull()
    })
    test('should show content for Control', () => {
      const action = { source: MissionSourceEnum.RAPPORTNAV, type: ActionTypeEnum.CONTROL }
      render(<MissionTimelineItem {...props(action)} />)
      expect(screen.getByText('Contrôles')).toBeInTheDocument()
    })
    test('should show content for Status', () => {
      const action = { source: MissionSourceEnum.RAPPORTNAV, type: ActionTypeEnum.STATUS }
      render(<MissionTimelineItem {...props(action)} />)
      expect(screen.getByText('Inconnu - début')).toBeInTheDocument()
    })
  })
})
