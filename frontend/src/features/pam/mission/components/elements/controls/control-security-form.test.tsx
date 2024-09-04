import { ControlAdministrative, ControlGensDeMer, ControlSecurity } from '@common/types/control-types'
import { Params } from 'react-router-dom'
import { vi } from 'vitest'
import { fireEvent, render, screen } from '../../../../../../test-utils'
import * as useControlHook from '../../../hooks/control/use-control'
import ControlSecurityForm from './control-security-form'

const updateControlMock = vi.fn()
const toogleControlMock = vi.fn()
const controlChangedMock = vi.fn()

vi.mock('react-router-dom', async importOriginal => {
  const actual = await importOriginal()
  return {
    ...actual,
    useParams: (): Readonly<Params<string>> => ({ missionId: '761', actionId: '3434' })
  }
})

describe('ControlSecurityForm', () => {
  afterEach(() => {
    vi.clearAllMocks()
    vi.resetAllMocks()
  })

  it('should render control security form', () => {
    const data = {
      id: '',
      amountOfControls: 0
    } as ControlGensDeMer
    render(<ControlSecurityForm data={data} />)
    expect(screen.getByText('Equipements et respect des normes de sécurité')).toBeInTheDocument()
  })

  it('it should have control security title check when should complete control is true', () => {
    const data = {
      id: '',
      amountOfControls: 0
    } as ControlGensDeMer
    const wrapper = render(<ControlSecurityForm data={data} shouldCompleteControl={false} />)
    const checkbox = wrapper.container.querySelectorAll("input[type='checkbox']")[0] as HTMLInputElement
    expect(checkbox).toBeChecked()
  })

  it('it should display required red ', () => {
    const wrapper = render(<ControlSecurityForm shouldCompleteControl={true} />)
    expect(wrapper.getAllByTestId('control-title-required-control')).not.toBeNull()
  })

  it('it should show unit should confirm toogle', () => {
    const data = {
      id: '',
      amountOfControls: 0
    } as ControlAdministrative
    render(<ControlSecurityForm data={data} unitShouldConfirm={true} />)
    expect(screen.getByText('Contrôle confirmé par l’unité')).toBeInTheDocument()
  })

  it('it should trigger delete control', async () => {
    const useControlSpy = vi.spyOn(useControlHook, 'useControl')
    useControlSpy.mockReturnValue({
      isRequired: false,
      controlIsChecked: false,
      updateControl: updateControlMock,
      toggleControl: toogleControlMock,
      controlChanged: controlChangedMock
    })
    const data = {
      id: 'scscss',
      observations: 'myObservations'
    } as ControlSecurity

    const wrapper = render(<ControlSecurityForm data={data} shouldCompleteControl={true} unitShouldConfirm={false} />)
    const checkbox = wrapper.container.querySelectorAll("input[type='checkbox']")[0] as HTMLInputElement
    fireEvent.click(checkbox)
    expect(toogleControlMock).toHaveBeenCalledTimes(1)
  })

  it('it should update form and trigger use control controlChanged', async () => {
    const useControlSpy = vi.spyOn(useControlHook, 'useControl')
    useControlSpy.mockReturnValue({
      isRequired: true,
      controlIsChecked: false,
      updateControl: updateControlMock,
      toggleControl: toogleControlMock,
      controlChanged: controlChangedMock
    })
    render(<ControlSecurityForm shouldCompleteControl={false} unitShouldConfirm={false} />)
    const observations = screen.getByLabelText(
      'Observations (hors infraction) sur la sécurité du navire (équipements…)'
    )
    fireEvent.change(observations, { target: { value: 'my observations' } })
    expect(controlChangedMock).toHaveBeenCalled() //TODO: check number of times
  })
})
