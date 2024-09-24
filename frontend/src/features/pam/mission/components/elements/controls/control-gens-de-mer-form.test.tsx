import { ControlGensDeMer, ControlResult } from '@common/types/control-types'
import { Params } from 'react-router-dom'
import { vi } from 'vitest'
import { fireEvent, render, screen } from '../../../../../../test-utils'
import * as useControlHook from '../../../hooks/control/use-control'
import ControlGensDeMerForm from './control-gens-de-mer-form'

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

  it('should not create or update controls on render', () => {
    const data = {
      id: '',
      amountOfControls: 0
    } as ControlGensDeMer

    const useControlSpy = vi.spyOn(useControlHook, 'useControl')
    useControlSpy.mockReturnValue(useControlMockData({}))
    render(<ControlGensDeMerForm data={data} />)
    expect(updateControlMock).not.toHaveBeenCalled()
    expect(toggleControlMock).not.toHaveBeenCalled()
    expect(controlChangedMock).not.toHaveBeenCalled()
  })

  it('it should display infraction', async () => {
    const useControlSpy = vi.spyOn(useControlHook, 'useControl')
    useControlSpy.mockReturnValue(useControlMockData({}))
    const data = {
      id: '',
      amountOfControls: 0
    } as ControlGensDeMer
    render(<ControlGensDeMerForm data={data} />)
    expect(screen.getByText(`Ajouter une infraction administrative`)).toBeInTheDocument()
  })

  it('it should have control gens de mer title check when should complete control is true', () => {
    const useControlSpy = vi.spyOn(useControlHook, 'useControl')
    useControlSpy.mockReturnValue(useControlMockData({ controlIsChecked: true }))
    const data = {
      id: '',
      amountOfControls: 0
    } as ControlGensDeMer
    const wrapper = render(<ControlGensDeMerForm data={data} shouldCompleteControl={false} />)
    const checkbox = wrapper.container.querySelectorAll("input[type='checkbox']")[0] as HTMLInputElement
    expect(checkbox).toBeChecked()
  })

  it('it should display required red ', () => {
    const useControlSpy = vi.spyOn(useControlHook, 'useControl')
    useControlSpy.mockReturnValue(useControlMockData({ isRequired: true }))
    const wrapper = render(<ControlGensDeMerForm shouldCompleteControl={true} />)
    expect(wrapper.getAllByTestId('control-title-required-control')).not.toBeNull()
  })

  it('it should show unit should confirm toggle', () => {
    const useControlSpy = vi.spyOn(useControlHook, 'useControl')
    useControlSpy.mockReturnValue(useControlMockData({ isRequired: true }))
    const data = {
      id: '',
      amountOfControls: 0
    } as ControlGensDeMer
    render(<ControlGensDeMerForm data={data} unitShouldConfirm={true} />)
    expect(screen.getByText('Contrôle confirmé par l’unité')).toBeInTheDocument()
  })

  it('it should trigger delete control', async () => {
    const useControlSpy = vi.spyOn(useControlHook, 'useControl')
    useControlSpy.mockReturnValue(useControlMockData({ isRequired: false }))
    const data = {
      id: 'scscss',
      observations: 'myObservations',
      amountOfControls: 0,
      staffOutnumbered: ControlResult.NOT_CONTROLLED,
      upToDateMedicalCheck: ControlResult.NO,
      knowledgeOfFrenchLawAndLanguage: ControlResult.NOT_CONCERNED
    } as ControlGensDeMer

    const wrapper = render(<ControlGensDeMerForm data={data} shouldCompleteControl={true} unitShouldConfirm={false} />)
    const checkbox = wrapper.container.querySelectorAll("input[type='checkbox']")[0] as HTMLInputElement
    fireEvent.click(checkbox)
    expect(toggleControlMock).toHaveBeenCalledTimes(1)
  })

  it('it should update form and trigger use control controlChanged', async () => {
    const data = {
      id: 'scscss',
      observations: 'myObservations',
      amountOfControls: 0,
      staffOutnumbered: ControlResult.NOT_CONTROLLED,
      upToDateMedicalCheck: ControlResult.NO,
      knowledgeOfFrenchLawAndLanguage: ControlResult.NOT_CONCERNED
    } as ControlGensDeMer
    const useControlSpy = vi.spyOn(useControlHook, 'useControl')
    useControlSpy.mockReturnValue(useControlMockData({ isRequired: true }))
    const wrapper = render(<ControlGensDeMerForm data={data} shouldCompleteControl={false} unitShouldConfirm={false} />)
    const radio = wrapper.container.querySelectorAll("input[type='radio']")[0] as HTMLInputElement
    fireEvent.click(radio)
    expect(controlChangedMock).toHaveBeenCalled() //TODO: check number of times
  })
})
