import { render, screen } from '../../../../../../../test-utils.tsx'
import ActionContainer from './timeline-item-container.tsx'
import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types.ts'

const props = (actionType: ActionTypeEnum, actionSource: MissionSourceEnum) => ({
  actionType,
  actionSource,
  children: <div data-testid={'action-container-content'}></div>
})
describe('ActionContainer', () => {
  test('should null when actionSource is not supported ', () => {
    const { container } = render(<ActionContainer {...props(ActionTypeEnum.CONTROL, undefined)} />)
    expect(container.firstElementChild).toBeNull()
  })

  describe('Env', () => {
    test('should null when actionSource is not supported ', () => {
      const { container } = render(<ActionContainer {...props(undefined, MissionSourceEnum.MONITORENV)} />)
      expect(container.firstElementChild).toBeNull()
    })
    test('should show content for Control', () => {
      render(<ActionContainer {...props(ActionTypeEnum.CONTROL, MissionSourceEnum.MONITORENV)} />)
      expect(screen.getByTestId('action-container-content')).toBeInTheDocument()
    })
  })

  describe('Fish', () => {
    test('should null when actionSource is not supported ', () => {
      const { container } = render(<ActionContainer {...props(undefined, MissionSourceEnum.MONITORFISH)} />)
      expect(container.firstElementChild).toBeNull()
    })
    test('should show content for Control', () => {
      render(<ActionContainer {...props(ActionTypeEnum.CONTROL, MissionSourceEnum.MONITORFISH)} />)
      expect(screen.getByTestId('action-container-content')).toBeInTheDocument()
    })
  })

  describe('Nav', () => {
    test('should null when actionSource is not supported ', () => {
      const { container } = render(<ActionContainer {...props(undefined, MissionSourceEnum.RAPPORTNAV)} />)
      expect(container.firstElementChild).toBeNull()
    })
    test('should show content for Control', () => {
      render(<ActionContainer {...props(ActionTypeEnum.CONTROL, MissionSourceEnum.RAPPORTNAV)} />)
      expect(screen.getByTestId('action-container-content')).toBeInTheDocument()
    })
    test('should show content for Status', () => {
      render(<ActionContainer {...props(ActionTypeEnum.STATUS, MissionSourceEnum.RAPPORTNAV)} />)
      expect(screen.getByTestId('action-container-content')).toBeInTheDocument()
    })
  })
})
