import { ControlNavigation } from '@common/types/control-types'
import { Params } from 'react-router-dom'
import { vi } from 'vitest'
import { fireEvent, render, screen } from '../../../../../../test-utils'
import * as useControlHook from '../../../hooks/control/use-control'
import ControlNavigationForm from './control-navigation-form'

const useControlMockData = (data: any) => ({
  isRequired: false,
  controlIsChecked: false,
  updateControl: updateControlMock,
  toggleControl: toggleControlMock,
  controlChanged: controlChangedMock,
  controlEnvChanged: vi.fn(),
  ...data
})

const updateControlMock = vi.fn()
const toggleControlMock = vi.fn()
const controlChangedMock = vi.fn()

vi.mock('react-router-dom', async importOriginal => {
  const actual = await importOriginal()
  return {
    ...actual,
    useParams: (): Readonly<Params<string>> => ({ missionId: '761', actionId: '3434' })
  }
})

describe('ControlNavigationForm', () => {
  afterEach(() => {
    vi.clearAllMocks()
    vi.resetAllMocks()
  })

  it('should render control navigation form', () => {
    const data = {
      id: '',
      amountOfControls: 0
    } as ControlNavigation
    render(<ControlNavigationForm data={data} />)
    expect(screen.getByText('Respect des règles de navigation')).toBeInTheDocument()
  })

  it('should not create or update controls on render', () => {
    const data = {
      id: '',
      amountOfControls: 0
    } as ControlNavigation

    const useControlSpy = vi.spyOn(useControlHook, 'useControl')
    useControlSpy.mockReturnValue(useControlMockData({}))
    render(<ControlNavigationForm data={data} />)
    expect(updateControlMock).not.toHaveBeenCalled()
    expect(toggleControlMock).not.toHaveBeenCalled()
    expect(controlChangedMock).not.toHaveBeenCalled()
  })

  it('it should have control navigation title check when should complete control is true', () => {
    const data = {
      id: '',
      amountOfControls: 0
    } as ControlNavigation
    const useControlSpy = vi.spyOn(useControlHook, 'useControl')
    useControlSpy.mockReturnValue(useControlMockData({ controlIsChecked: true }))
    const wrapper = render(<ControlNavigationForm data={data} shouldCompleteControl={false} />)
    const checkbox = wrapper.container.querySelectorAll("input[type='checkbox']")[0] as HTMLInputElement
    expect(checkbox).toBeChecked()
  })

  it('it should display required red ', () => {
    const useControlSpy = vi.spyOn(useControlHook, 'useControl')
    useControlSpy.mockReturnValue(useControlMockData({ isRequired: true }))
    const wrapper = render(<ControlNavigationForm shouldCompleteControl={true} />)
    expect(wrapper.getAllByTestId('control-title-required-control')).not.toBeNull()
  })

  it('it should show unit should confirm toggle', () => {
    const data = {
      id: '',
      amountOfControls: 0
    } as ControlNavigation
    const useControlSpy = vi.spyOn(useControlHook, 'useControl')
    useControlSpy.mockReturnValue(useControlMockData({ isRequired: true }))
    render(<ControlNavigationForm data={data} unitShouldConfirm={true} />)
    expect(screen.getByText('Contrôle confirmé par l’unité')).toBeInTheDocument()
  })

  it('it should trigger delete control', async () => {
    const useControlSpy = vi.spyOn(useControlHook, 'useControl')
    useControlSpy.mockReturnValue(useControlMockData({}))
    const data = {
      id: 'scscss',
      observations: 'myObservations'
    } as ControlNavigation

    const wrapper = render(<ControlNavigationForm data={data} shouldCompleteControl={true} unitShouldConfirm={false} />)
    const checkbox = wrapper.container.querySelectorAll("input[type='checkbox']")[0] as HTMLInputElement
    fireEvent.click(checkbox)
    expect(toggleControlMock).toHaveBeenCalledTimes(1)
  })

  it('it should update form and trigger use control controlChanged', async () => {
    const data = {
      id: 'scscss',
      observations: 'myObservations'
    } as ControlNavigation
    const useControlSpy = vi.spyOn(useControlHook, 'useControl')
    useControlSpy.mockReturnValue(useControlMockData({}))
    render(<ControlNavigationForm data={data} shouldCompleteControl={false} unitShouldConfirm={false} />)
    const observations = screen.getByLabelText('Observations (hors infraction) sur les règles de navigation')
    fireEvent.change(observations, { target: { value: 'my observations' } })
    expect(controlChangedMock).toHaveBeenCalled() //TODO: check number of times
  })
})
