import { beforeEach, describe, expect, it, vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import { useMissionFinished } from '../../../../common/hooks/use-mission-finished'
import { useOnlineManager } from '../../../../common/hooks/use-online-manager'
import { ActionType } from '../../../../common/types/action-type'
import { MissionAction } from '../../../../common/types/mission-action'
import { useSati } from '../../../../sati/hooks/use-sati'
import { useMissionActionGenericControl } from '../../../hooks/use-mission-action-generic-control'
import MissionActionItemSectorFishingControl from '../mission-action-item-control-sector-fishing'

const tabsProps = vi.hoisted(() => vi.fn())
const datePickerProps = vi.hoisted(() => vi.fn())
const formikEffectProps = vi.hoisted(() => vi.fn())

vi.mock('@mtes-mct/monitor-ui', async importOriginal => ({
  ...(await importOriginal<typeof import('@mtes-mct/monitor-ui')>()),
  FormikEffect: (props: unknown) => {
    formikEffectProps(props)
    return null
  }
}))
vi.mock('../../../../common/components/ui/styled-tab', () => ({
  default: (props: unknown) => {
    tabsProps(props)
    return <div data-testid="tabs" />
  }
}))
vi.mock('../../../../common/components/elements/mission-bound-formik-date-range-picker', () => ({
  default: (props: unknown) => {
    datePickerProps(props)
    return <div data-testid="date-range-picker" />
  }
}))
vi.mock('../../../../common/hooks/use-mission-finished', () => ({ useMissionFinished: vi.fn() }))
vi.mock('../../../../common/hooks/use-online-manager', () => ({ useOnlineManager: vi.fn() }))
vi.mock('../../../../common/hooks/use-form-validation-reporter', () => ({
  useFormValidationReporter: () => ({ onFormError: vi.fn() })
}))
vi.mock('../../../../sati/hooks/use-sati', () => ({ useSati: vi.fn() }))
vi.mock('../../../hooks/use-mission-action-generic-control', () => ({ useMissionActionGenericControl: vi.fn() }))
vi.mock('../../../validation-schema/control-sector-fishing', () => ({
  getControlSectorFishingSchema: vi.fn(() => ({ sectorEstablishmentType: 'schema' }))
}))
vi.mock('../../ui/mission-action-control-sector-fishing-form', () => ({ default: () => null }))
vi.mock('../../../../sati/components/layout/control-conclusion-layout', () => ({ default: () => null }))

const action = {
  id: 'action-id',
  ownerId: 'mission-id',
  actionType: ActionType.CONTROL_SECTOR_FISHING,
  controlsToComplete: ['ADMINISTRATIVE']
} as unknown as MissionAction

const initValue = { observations: 'obs', sati: { principalInspector: { agentId: undefined } } }
const handleSubmit = vi.fn()

const renderComponent = (onChange = vi.fn()) =>
  render(<MissionActionItemSectorFishingControl action={action} onChange={onChange} />)

const renderedKeys = (): string[] => tabsProps.mock.calls.at(-1)?.[0].items.map((i: { key: string }) => i.key)

describe('MissionActionItemSectorFishingControl', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.mocked(useSati).mockReturnValue({ isSatiEnabled: true })
    vi.mocked(useMissionFinished).mockReturnValue(false)
    vi.mocked(useOnlineManager).mockReturnValue({ isOnline: true } as ReturnType<typeof useOnlineManager>)
    vi.mocked(useMissionActionGenericControl).mockReturnValue({
      initValue,
      handleSubmit,
      validationSchema: undefined
    } as unknown as ReturnType<typeof useMissionActionGenericControl>)
  })

  it('should render nothing until the form is initialized', () => {
    vi.mocked(useMissionActionGenericControl).mockReturnValue({
      initValue: undefined,
      handleSubmit,
      validationSchema: undefined
    } as unknown as ReturnType<typeof useMissionActionGenericControl>)
    renderComponent()

    expect(screen.queryByTestId('action-generic-control')).not.toBeInTheDocument()
    expect(screen.queryByTestId('tabs')).not.toBeInTheDocument()
  })

  it('should render the date range picker and the tabs once initialized', () => {
    renderComponent()

    expect(screen.getByTestId('action-generic-control')).toBeInTheDocument()
    expect(screen.getByTestId('date-range-picker')).toBeInTheDocument()
    expect(screen.getByTestId('tabs')).toBeInTheDocument()
    expect(datePickerProps.mock.calls[0][0]).toMatchObject({ missionId: 'mission-id', disabled: false, title: '' })
  })

  it('should disable the date range picker offline', () => {
    vi.mocked(useOnlineManager).mockReturnValue({ isOnline: false } as ReturnType<typeof useOnlineManager>)
    renderComponent()

    const props = datePickerProps.mock.calls[0][0]
    expect(props.disabled).toBe(true)
    expect(props.title).toContain('hors ligne')
  })

  it('should show every tab when SATI is enabled', () => {
    renderComponent()

    expect(renderedKeys()).toEqual(['infos', 'propeche', 'files', 'conclusion'])
  })

  it('should hide the propeche and files tabs when SATI is disabled', () => {
    vi.mocked(useSati).mockReturnValue({ isSatiEnabled: false })
    renderComponent()

    expect(renderedKeys()).toEqual(['infos', 'conclusion'])
  })

  it('should open on the infos tab', () => {
    renderComponent()

    expect(tabsProps.mock.calls[0][0].defaultActiveKey).toBe('infos')
  })

  it('should give the tabs the formik values rather than the raw action data', () => {
    renderComponent()

    const { params } = tabsProps.mock.calls[0][0]
    expect(params.values).toEqual(initValue)
    expect(params.values).not.toBe(action.data)
    expect(params.controlsToComplete).toEqual(['ADMINISTRATIVE'])
    expect(params.formik.values).toEqual(initValue)
  })

  it('should configure the generic control hook for the fishing sector form', () => {
    const onChange = vi.fn()
    renderComponent(onChange)

    expect(useMissionActionGenericControl).toHaveBeenCalledWith(
      action,
      onChange,
      { sectorEstablishmentType: 'schema' },
      false,
      [
        'incidentDuringOperation',
        'isControlDuringSecurityDay',
        'isWithinDepartment',
        'hasDivingDuringOperation',
        'isSeizureSleepingFishingGear',
        'signature',
        'isOutOfUnit'
      ]
    )
  })

  it('should submit the form values on change', async () => {
    renderComponent()

    const { onChange } = formikEffectProps.mock.calls[0][0]
    await onChange({ observations: 'next' })
    expect(handleSubmit).toHaveBeenCalledWith({ observations: 'next' })
  })
})
