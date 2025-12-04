import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent, waitFor } from '../../../../../../test-utils'
import MissionTargetItem from '../mission-target-item'
import { ControlType } from '@common/types/control-types'
import { MissionSourceEnum } from '@common/types/env-mission-types'
import { TargetType } from '../../../../common/types/target-types.ts'

// Mock useTarget hook
const mockGetAvailableControlTypes = vi.fn()

vi.mock('../../../hooks/use-target', () => ({
  useTarget: () => ({
    getAvailableControlTypes: mockGetAvailableControlTypes
  })
}))

describe('MissionTargetItem', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    mockGetAvailableControlTypes.mockReturnValue([ControlType.SECURITY, ControlType.NAVIGATION])
  })

  it('renders target title', () => {
    const target = {
      id: '1',
      vesselName: 'Test Vessel',
      source: MissionSourceEnum.MONITORFISH
    }

    render(
      <MissionTargetItem
        name="targets[0]"
        targetType={TargetType.VEHICLE}
        fieldFormik={{ field: { value: target } } as any}
        actionNumberOfControls={5}
      />,
      { formikValues: { targets: [target] } }
    )

    expect(screen.getByTestId('target-title')).toBeInTheDocument()
  })

  it('renders target actions', () => {
    const target = {
      id: '1',
      vesselName: 'Test Vessel',
      source: MissionSourceEnum.MONITORFISH
    }

    render(
      <MissionTargetItem
        name="targets[0]"
        targetType={TargetType.VEHICLE}
        fieldFormik={{ field: { value: target } } as any}
        actionNumberOfControls={5}
      />,
      { formikValues: { targets: [target] } }
    )

    expect(screen.getByTestId('mission-target-action')).toBeInTheDocument()
  })

  it('does not display external data when not available', () => {
    const target = {
      id: '1',
      vesselName: 'Test Vessel',
      source: MissionSourceEnum.MONITORFISH
    }

    render(
      <MissionTargetItem
        name="targets[0]"
        targetType={TargetType.VEHICLE}
        fieldFormik={{ field: { value: target } } as any}
        actionNumberOfControls={5}
      />,
      { formikValues: { targets: [target] } }
    )

    const showDetailButton = screen.getByTestId('show-target')
    fireEvent.click(showDetailButton)

    expect(screen.queryByTestId('mission-target-external-data')).not.toBeInTheDocument()
  })

  it('shows target form when edit button is clicked', async () => {
    const target = {
      id: '1',
      vesselName: 'Test Vessel',
      source: MissionSourceEnum.MONITORFISH
    }

    render(
      <MissionTargetItem
        name="targets[0]"
        targetType={TargetType.VEHICLE}
        fieldFormik={{ field: { value: target } } as any}
        actionNumberOfControls={5}
      />,
      { formikValues: { targets: [target] } }
    )

    const editButton = screen.getByTestId('edit-target')
    fireEvent.click(editButton)

    await waitFor(() => {
      expect(screen.getByTestId('mission-target-form')).toBeInTheDocument()
    })
  })

  it('shows target form when add infraction button is clicked', () => {
    const target = {
      id: '1',
      vesselName: 'Test Vessel',
      source: MissionSourceEnum.MONITORFISH
    }

    render(
      <MissionTargetItem
        name="targets[0]"
        targetType={TargetType.VEHICLE}
        fieldFormik={{ field: { value: target } } as any}
        actionNumberOfControls={5}
      />,
      { formikValues: { targets: [target] } }
    )

    const editButton = screen.getByTestId('edit-target')
    fireEvent.click(editButton)
    const addInfractionButton = screen.getByTestId('validate-infraction')
    fireEvent.click(addInfractionButton)

    expect(screen.getByTestId('mission-target-form')).toBeInTheDocument()
  })

  it('calls onDelete when delete button is clicked', async () => {
    const target = {
      id: '1',
      vesselName: 'Test Vessel',
      source: MissionSourceEnum.MONITORFISH
    }

    const mockOnDelete = vi.fn()

    render(
      <MissionTargetItem
        index={0}
        name="targets[0]"
        targetType={TargetType.VEHICLE}
        fieldFormik={{ field: { value: target } } as any}
        actionNumberOfControls={5}
        onDelete={mockOnDelete}
      />,
      { formikValues: { targets: [target] } }
    )

    const deleteButton = screen.getByTestId('delete-target')
    fireEvent.click(deleteButton)

    await waitFor(() => {
      expect(mockOnDelete).toHaveBeenCalledWith(0)
    })
  })

  it('does not call onDelete when index is falsy', async () => {
    const target = {
      id: '1',
      vesselName: 'Test Vessel',
      source: MissionSourceEnum.MONITORFISH
    }

    const mockOnDelete = vi.fn()

    render(
      <MissionTargetItem
        index={undefined}
        name="targets[0]"
        targetType={TargetType.VEHICLE}
        fieldFormik={{ field: { value: target } } as any}
        actionNumberOfControls={5}
        onDelete={mockOnDelete}
      />,
      { formikValues: { targets: [target] } }
    )

    const deleteButton = screen.getByTestId('delete-target')
    fireEvent.click(deleteButton)

    expect(mockOnDelete).not.toHaveBeenCalled()
  })

  it('does not call onDelete when target has no id', () => {
    const target = {
      vesselName: 'Test Vessel',
      source: MissionSourceEnum.MONITORFISH
    }

    const mockOnDelete = vi.fn()

    render(
      <MissionTargetItem
        name="targets[0]"
        targetType={TargetType.VEHICLE}
        fieldFormik={{ field: { value: target } } as any}
        actionNumberOfControls={5}
        onDelete={mockOnDelete}
      />,
      { formikValues: { targets: [target] } }
    )

    const deleteButton = screen.getByTestId('delete-target')
    fireEvent.click(deleteButton)

    expect(mockOnDelete).not.toHaveBeenCalled()
  })

  it('disables add infraction for non-vehicle MonitorEnv targets', () => {
    const target = {
      id: '1',
      companyName: 'Test Company',
      source: MissionSourceEnum.MONITORENV
    }

    render(
      <MissionTargetItem
        name="targets[0]"
        targetType={TargetType.COMPANY}
        fieldFormik={{ field: { value: target } } as any}
        actionNumberOfControls={5}
      />,
      { formikValues: { targets: [target] } }
    )

    expect(screen.queryByTestId('edit-target')).toBeNull()
  })

  it('enables add infraction for non-MonitorEnv sources', () => {
    const target = {
      id: '1',
      companyName: 'Test Company',
      source: MissionSourceEnum.MONITORFISH
    }

    render(
      <MissionTargetItem
        name="targets[0]"
        targetType={TargetType.COMPANY}
        fieldFormik={{ field: { value: target } } as any}
        actionNumberOfControls={5}
      />,
      { formikValues: { targets: [target] } }
    )

    const editButton = screen.getByTestId('edit-target')
    fireEvent.click(editButton)
    const addInfractionButton = screen.getByTestId('validate-infraction')
    expect(addInfractionButton).not.toBeDisabled()
  })

  it('renders infraction list', () => {
    const target = {
      id: '1',
      vesselName: 'Test Vessel',
      source: MissionSourceEnum.MONITORFISH
    }

    render(
      <MissionTargetItem
        name="targets[0]"
        targetType={TargetType.VEHICLE}
        fieldFormik={{ field: { value: target } } as any}
        actionNumberOfControls={5}
      />,
      { formikValues: { targets: [target] } }
    )

    expect(screen.getByTestId('mission-target-infraction-list')).toBeInTheDocument()
  })

  it('passes available control types to form and infraction list', () => {
    const target = {
      id: '1',
      vesselName: 'Test Vessel',
      source: MissionSourceEnum.MONITORFISH
    }

    const availableControlTypes = [ControlType.SECURITY, ControlType.NAVIGATION]

    render(
      <MissionTargetItem
        name="targets[0]"
        targetType={TargetType.VEHICLE}
        fieldFormik={{ field: { value: target } } as any}
        actionNumberOfControls={5}
        availableControlTypes={availableControlTypes}
      />,
      { formikValues: { targets: [target] } }
    )

    expect(mockGetAvailableControlTypes).toHaveBeenCalledWith(target, availableControlTypes)
  })

  it('does not call onDelete when onDelete prop is not provided', () => {
    const target = {
      id: '1',
      vesselName: 'Test Vessel',
      source: MissionSourceEnum.MONITORFISH
    }

    render(
      <MissionTargetItem
        name="targets[0]"
        targetType={TargetType.VEHICLE}
        fieldFormik={{ field: { value: target } } as any}
        actionNumberOfControls={5}
      />,
      { formikValues: { targets: [target] } }
    )

    const deleteButton = screen.getByTestId('delete-target')

    // Should not throw error when onDelete is not provided
    expect(() => fireEvent.click(deleteButton)).not.toThrow()
  })
})
