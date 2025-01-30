import { Icon } from '@mtes-mct/monitor-ui'
import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import { ActionGroupType, ActionType } from '../../../../common/types/action-type'
import MissionTimelineDropdownWrapper from '../mission-timeline-dropdown-wrapper'

const dropdownItems = [
  {
    type: ActionGroupType.CONTROL_GROUP,
    icon: Icon.ControlUnit,
    dropdownText: 'Ajouter des contrôles',
    subItems: [{ type: ActionType.CONTROL, dropdownText: 'Contrôle de navire' }]
  }
]

const handleSelect = vi.fn()

describe('MissionTimelineDropdownWrapper', () => {
  it('should match the snapshot', () => {
    const wrapper = render(<MissionTimelineDropdownWrapper dropdownItems={dropdownItems} onSelect={handleSelect} />)
    expect(wrapper).toMatchSnapshot()
  })
})
