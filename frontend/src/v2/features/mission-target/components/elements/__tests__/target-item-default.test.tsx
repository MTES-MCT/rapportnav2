import { ControlType } from '@common/types/control-types'
import { MissionSourceEnum } from '@common/types/env-mission-types'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { fireEvent, render, screen, waitFor } from '../../../../../../test-utils.tsx'
import { TargetType } from '../../../../common/types/target-types.ts'
import TargetItemDefault from '../target-item-default.tsx'

const mockGetAvailableControlTypes = vi.fn()

vi.mock('../../../hooks/use-target', () => ({
  useTarget: () => ({
    getAvailableControlTypes: mockGetAvailableControlTypes
  })
}))

vi.mock('../mission-target-form', () => ({
  default: ({ onClose }: any) => (
    <div>
      <button data-testid="close-form" onClick={onClose}>
        Close
      </button>
    </div>
  )
}))

vi.mock('../mission-target-infraction-list', () => ({
  default: () => <div />
}))

const defaultTarget = {
  id: '1',
  vesselName: 'Test Vessel',
  source: MissionSourceEnum.MONITORFISH
}

const defaultProps = {
  name: 'targets[0]',
  targetType: TargetType.VEHICLE,
  fieldFormik: { field: { value: defaultTarget } } as any
}

describe('TargetItemDefault', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    mockGetAvailableControlTypes.mockReturnValue([ControlType.SECURITY, ControlType.NAVIGATION])
  })

  it('renders the root container', () => {
    render(<TargetItemDefault {...defaultProps} />, { formikValues: { targets: [defaultTarget] } })
    expect(screen.getByTestId('target-item')).toBeInTheDocument()
  })

  it('renders the add infraction button initially', () => {
    render(<TargetItemDefault {...defaultProps} />, { formikValues: { targets: [defaultTarget] } })

    expect(screen.getByTestId('show-target')).toBeInTheDocument()
  })

  it('renders the infraction list', () => {
    render(<TargetItemDefault {...defaultProps} />, { formikValues: { targets: [defaultTarget] } })

    expect(screen.getByTestId('mission-target-infraction-list')).toBeInTheDocument()
  })

  it('hides the add infraction button and shows the form when button is clicked', async () => {
    render(<TargetItemDefault {...defaultProps} />, { formikValues: { targets: [defaultTarget] } })

    fireEvent.click(screen.getByTestId('show-target'))

    await waitFor(() => {
      expect(screen.queryByTestId('show-target')).not.toBeInTheDocument()
      expect(screen.getByTestId('mission-target-form')).toBeInTheDocument()
    })
  })

  it('hides the form and restores the button when form is closed', async () => {
    render(<TargetItemDefault {...defaultProps} />, { formikValues: { targets: [defaultTarget] } })

    fireEvent.click(screen.getByTestId('show-target'))

    await waitFor(() => {
      expect(screen.getByTestId('mission-target-form')).toBeInTheDocument()
    })

    fireEvent.click(screen.getByTestId('close-form'))

    await waitFor(() => {
      expect(screen.queryByTestId('mission-target-form')).not.toBeInTheDocument()
      expect(screen.getByTestId('show-target')).toBeInTheDocument()
    })
  })

  it('calls getAvailableControlTypes with the target and provided availableControlTypes', () => {
    const availableControlTypes = [ControlType.SECURITY]

    render(<TargetItemDefault {...defaultProps} availableControlTypes={availableControlTypes} />, {
      formikValues: { targets: [defaultTarget] }
    })

    expect(mockGetAvailableControlTypes).toHaveBeenCalledWith(defaultTarget, availableControlTypes)
  })

  it('calls getAvailableControlTypes with undefined when no availableControlTypes provided', () => {
    render(<TargetItemDefault {...defaultProps} />, { formikValues: { targets: [defaultTarget] } })

    expect(mockGetAvailableControlTypes).toHaveBeenCalledWith(defaultTarget, undefined)
  })

  it('does not render the form before the button is clicked', () => {
    render(<TargetItemDefault {...defaultProps} />, { formikValues: { targets: [defaultTarget] } })

    expect(screen.queryByTestId('mission-target-form')).not.toBeInTheDocument()
  })
})
