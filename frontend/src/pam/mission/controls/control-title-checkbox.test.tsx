import { vi } from 'vitest'
import { fireEvent, getByRole, render, screen } from '../../../test-utils'
import { ControlType } from '../../../types/control-types'
import ControlTitleCheckbox, { ControlTitleCheckboxProps } from './control-title-checkbox'

const props = (onChange: any, checked?: boolean = null) =>
  ({
    onChange,
    checked,
    shouldCompleteControl: false,
    controlType: ControlType.ADMINISTRATIVE
  } as ControlTitleCheckboxProps)

describe('ControlTitleCheckbox', () => {
  describe('changing the checkbox value', () => {
    const onChange = vi.fn()
    it('should call the onChange props when onChange not undefined', () => {
      render(<ControlTitleCheckbox {...props(onChange)} />)
      const checkbox = screen.getByRole('checkbox')
      fireEvent(
        checkbox,
        new MouseEvent('click', {
          bubbles: true,
          cancelable: true
        })
      )
      expect(onChange).toHaveBeenCalledTimes(1)
    })
    it('should not call the onChange props when onChange is undefined', () => {
      render(<ControlTitleCheckbox {...props(undefined)} />)
      const checkbox = screen.getByRole('checkbox')
      fireEvent(
        checkbox,
        new MouseEvent('click', {
          bubbles: true,
          cancelable: true
        })
      )
      expect(onChange).toHaveBeenCalledTimes(1)
    })
  })

  it('should be disabled when no onChange and not checked', () => {
    render(<ControlTitleCheckbox {...props(undefined, false)} />)
    const checkbox = screen.getByRole('checkbox')
    expect(checkbox).toBeDisabled()
  })
  it('should be enabled when no onChange and is checked', () => {
    render(<ControlTitleCheckbox {...props(undefined, true)} />)
    const checkbox = screen.getByRole('checkbox')
    expect(checkbox).not.toBeDisabled()
  })
  it('should be enabled when onChange and not checked', () => {
    render(<ControlTitleCheckbox {...props(vi.fn(), false)} />)
    const checkbox = screen.getByRole('checkbox')
    expect(checkbox).not.toBeDisabled()
  })
})
