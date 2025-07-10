import { render, screen, fireEvent } from '../../../../../../test-utils.tsx'
import ControlSelection from './control-selection.tsx'
import { VesselTypeEnum } from '@common/types/mission-types.ts'
import { missionTypeEnum } from '@common/types/env-mission-types.ts'

describe('ControlSelection', () => {
  test('renders control selection options', () => {
    render(<ControlSelection onSelect={vi.fn()} />)

    // Check if control type radio buttons are rendered
    expect(screen.getByLabelText(missionTypeEnum.SEA.libelle)).toBeInTheDocument()
    expect(screen.getByLabelText(missionTypeEnum.LAND.libelle)).toBeInTheDocument()

    // Check if control options are rendered
    expect(screen.getByText('navire de pêche professionnelle')).toBeInTheDocument()
    expect(screen.getByText('navire de commerce')).toBeInTheDocument()
    expect(screen.getByText('navire de service')).toBeInTheDocument()
    expect(screen.getByText('navire de plaisance professionnelle')).toBeInTheDocument()
    expect(screen.getByText('navire de plaisance de loisir')).toBeInTheDocument()
  })

  test('calls onSelect when a control is clicked', () => {
    const onSelectMock = vi.fn()
    render(<ControlSelection onSelect={onSelectMock} />)

    // Click on a control option
    fireEvent.click(screen.getByText('navire de pêche professionnelle'))

    // Check if onSelect is called with the correct arguments
    expect(onSelectMock).toHaveBeenCalledWith(missionTypeEnum.SEA.code, VesselTypeEnum.FISHING)
  })
})
