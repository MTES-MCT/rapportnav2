import { THEME } from '@mtes-mct/monitor-ui'
import { vi } from 'vitest'
import { fireEvent, render, screen } from '../../../../../../test-utils.tsx'
import MissionCrewMemberList from './mission-crew-member-list.tsx'

const handleEditCrew = vi.fn()
const handleDeleteCrew = vi.fn()
const crewList = [
  {
    id: '1',
    agent: {
      id: 'agent1',
      firstName: 'Ivan',
      lastName: 'Lapierre',
      services: []
    },
    comment: 'My very very long comment',
    role: {
      id: '2',
      title: 'Agent de pont'
    }
  },
  {
    id: '2',
    agent: {
      id: 'agent2',
      firstName: 'Joseph',
      lastName: 'Dupont',
      services: []
    },
    comment: undefined,
    role: {
      id: '1',
      title: 'second mecanicien'
    }
  }
]

describe('MissionCrewMemberList', () => {
  it('should render Agent firstname lastname', async () => {
    render(
      <MissionCrewMemberList crewList={crewList} handleEditCrew={handleEditCrew} handleDeleteCrew={handleDeleteCrew} />
    )
    expect(screen.getByText('Ivan Lapierre')).toBeInTheDocument()
    expect(screen.getByText('Joseph Dupont')).toBeInTheDocument()
  })

  it('should render comment and active comment icon', async () => {
    const wrapper = render(
      <MissionCrewMemberList
        crewList={[crewList[0]]}
        handleEditCrew={handleEditCrew}
        handleDeleteCrew={handleDeleteCrew}
      />
    )
    const commentICon = wrapper.getByTestId('crew-member-comment-icon')
    expect(commentICon).toHaveStyle(` color: ${THEME.color.charcoal}`)
    expect(wrapper.getByText('My very very long comment')).toBeInTheDocument()
  })

  it('should render comment inactive comment icon', async () => {
    const commentICon = render(
      <MissionCrewMemberList
        crewList={[crewList[1]]}
        handleEditCrew={handleEditCrew}
        handleDeleteCrew={handleDeleteCrew}
      />
    ).getByTestId('crew-member-comment-icon')
    expect(commentICon).toHaveStyle(` color: ${THEME.color.lightGray}`)
  })

  it('should render edit icon and click trigger handle edit method', async () => {
    const editIcon = render(
      <MissionCrewMemberList
        crewList={[crewList[0]]}
        handleEditCrew={handleEditCrew}
        handleDeleteCrew={handleDeleteCrew}
      />
    ).getByTestId('edit-crew-member-icon')
    fireEvent.click(editIcon)
    expect(handleEditCrew).toHaveBeenCalledTimes(1)
  })

  it('should render delete icon and click trigger handle delete method', async () => {
    const deleteIcon = render(
      <MissionCrewMemberList
        crewList={[crewList[0]]}
        handleEditCrew={handleEditCrew}
        handleDeleteCrew={handleDeleteCrew}
      />
    ).getByTestId('delete-crew-member-icon')
    fireEvent.click(deleteIcon)
    expect(handleDeleteCrew).toHaveBeenCalledTimes(1)
  })
})
