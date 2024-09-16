import { ControlAdministrative, ControlGensDeMer, ControlResult } from '@common/types/control-types'
import { Params } from 'react-router-dom'
import { vi } from 'vitest'
import { fireEvent, render, screen } from '../../../../../../test-utils'
import * as useControlHook from '../../../hooks/control/use-control'
import ControlGensDeMerForm from './control-gens-de-mer-form'

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

describe('ControlGensDeMer', () => {
  afterEach(() => {
    vi.clearAllMocks()
    vi.resetAllMocks()
  })

  it('should render control gens de mers', () => {
    const data = {
      id: '',
      amountOfControls: 0
    } as ControlGensDeMer
    render(<ControlGensDeMerForm data={data} />)
    expect(screen.getByText('Décision d’effectif conforme au nombre de personnes à bord')).toBeInTheDocument()
  })

  it('it should have control gens de mer title check when should complete control is true', () => {
    const data = {
      id: '',
      amountOfControls: 0
    } as ControlGensDeMer
    const wrapper = render(<ControlGensDeMerForm data={data} shouldCompleteControl={false} />)
    const checkbox = wrapper.container.querySelectorAll("input[type='checkbox']")[0] as HTMLInputElement
    expect(checkbox).toBeChecked()
  })

  it('it should display required red ', () => {
    const wrapper = render(<ControlGensDeMerForm shouldCompleteControl={true} />)
    expect(wrapper.getAllByTestId('control-title-required-control')).not.toBeNull()
  })

  it('it should show unit should confirm toogle', () => {
    const data = {
      id: '',
      amountOfControls: 0
    } as ControlAdministrative
    render(<ControlGensDeMerForm data={data} unitShouldConfirm={true} />)
    expect(screen.getByText('Contrôle confirmé par l’unité')).toBeInTheDocument()
  })

  it('it should trigger delete control', async () => {
    const useControlSpy = vi.spyOn(useControlHook, 'useControl')
    useControlSpy.mockReturnValue({
      isRequired: false,
      controlIsChecked: false,
      updateControl: updateControlMock,
      toggleControl: toogleControlMock,
      controlChanged: controlChangedMock,
      controlEnvChanged: vi.fn()
    })
    const data = {
      id: 'scscss',
      observations: 'myObservations',
      amountOfControls: 0,
      compliantSecurityDocuments: ControlResult.NOT_CONTROLLED,
      upToDateNavigationPermit: ControlResult.NO,
      compliantOperatingPermit: ControlResult.NOT_CONCERNED
    } as ControlAdministrative

    const wrapper = render(<ControlGensDeMerForm data={data} shouldCompleteControl={true} unitShouldConfirm={false} />)
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
      controlChanged: controlChangedMock,
      controlEnvChanged: vi.fn()
    })
    const wrapper = render(<ControlGensDeMerForm shouldCompleteControl={false} unitShouldConfirm={false} />)
    const radio = wrapper.container.querySelectorAll("input[type='radio']")[0] as HTMLInputElement
    fireEvent.click(radio)
    expect(controlChangedMock).toHaveBeenCalled() //TODO: check number of times
  })
})
