import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '../../../../../../test-utils'
import MissionTargetAction from '../mission-target-action'
import { MissionSourceEnum } from '@common/types/env-mission-types'

describe('MissionTargetAction', () => {
  const mockEdit = vi.fn()
  const mockDelete = vi.fn()
  const mockAddInfraction = vi.fn()
  const mockShowDetail = vi.fn()

  const baseProps = {
    onEdit: mockEdit,
    onDelete: mockDelete,
    onAddInfraction: mockAddInfraction,
    onShowDetail: mockShowDetail,
    showDetail: false
  }

  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('renders edit and delete buttons when source !== MONITORENV', () => {
    render(<MissionTargetAction {...baseProps} source={MissionSourceEnum.POSEIDON_CNSP} />)

    expect(screen.getByRole('edit-target')).toBeInTheDocument()
    expect(screen.getByRole('delete-target')).toBeInTheDocument()
    expect(screen.getByRole('show-target')).toBeInTheDocument()
  })

  it('does NOT render edit/delete buttons when source === MONITORENV', () => {
    render(<MissionTargetAction {...baseProps} source={MissionSourceEnum.MONITORENV} />)

    expect(screen.queryByRole('edit-target')).not.toBeInTheDocument()
    expect(screen.queryByRole('delete-target')).not.toBeInTheDocument()

    // show target exists instead
    expect(screen.getByRole('display-target')).toBeInTheDocument()
  })

  it('calls onAddInfraction when add button is clicked', () => {
    render(<MissionTargetAction {...baseProps} source={MissionSourceEnum.OTHER} />)

    fireEvent.click(screen.getByRole('show-target'))
    expect(mockAddInfraction).toHaveBeenCalled()
  })

  it('disables Add Infraction button when disabledAdd=true', () => {
    render(<MissionTargetAction {...baseProps} disabledAdd source={MissionSourceEnum.OTHER} />)

    expect(screen.getByRole('show-target')).toBeDisabled()
  })

  it('calls onEdit when edit button is clicked', () => {
    render(<MissionTargetAction {...baseProps} source={MissionSourceEnum.OTHER} />)

    fireEvent.click(screen.getByRole('edit-target'))
    expect(mockEdit).toHaveBeenCalled()
  })

  it('calls onDelete when delete button is clicked', () => {
    render(<MissionTargetAction {...baseProps} source={MissionSourceEnum.OTHER} />)

    fireEvent.click(screen.getByRole('delete-target'))
    expect(mockDelete).toHaveBeenCalled()
  })

  it('calls onShowDetail when display button is clicked (MONITORENV)', () => {
    render(<MissionTargetAction {...baseProps} source={MissionSourceEnum.MONITORENV} />)

    fireEvent.click(screen.getByRole('display-target'))
    expect(mockShowDetail).toHaveBeenCalled()
  })

  it('renders Hide icon when showDetail=true', () => {
    render(<MissionTargetAction {...baseProps} showDetail={true} source={MissionSourceEnum.MONITORENV} />)

    expect(screen.getByRole('display-target')).toBeInTheDocument()
  })
})
