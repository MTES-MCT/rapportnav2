import { Formik, useFormikContext } from 'formik'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { fireEvent, screen } from '@testing-library/react'
import { render } from '../../../../../../test-utils'
import { Sati } from '../../../../common/types/sati'
import { useSati } from '../../../hooks/use-sati'
import ControlConclusionLayout from '../control-conclusion-layout'

const principalProps = vi.hoisted(() => vi.fn())
const listProps = vi.hoisted(() => vi.fn())

vi.mock('../../../hooks/use-sati', () => ({ useSati: vi.fn() }))
vi.mock('../../../../mission-target/hooks/use-target', () => ({
  useTarget: () => ({ defaultControlTypes: [], getAvailableControlTypes: vi.fn() })
}))
vi.mock('../../../../mission-target/components/elements/target-item-default', () => ({
  default: () => <div data-testid="target-item" />
}))
vi.mock('../../ui/resource-form', () => ({
  default: ({ onChange }: { onChange: (r: unknown) => void }) => (
    <button data-testid="resource-form" onClick={() => onChange({ id: 7 })} />
  )
}))
vi.mock('../../ui/inspector-item-principal', () => ({
  default: (props: { onChange: (i: unknown) => void }) => {
    principalProps(props)
    return <button data-testid="inspector-principal" onClick={() => props.onChange({ agentId: 1 })} />
  }
}))
vi.mock('../../ui/inpector-list', () => ({
  default: (props: unknown) => {
    listProps(props)
    return <div data-testid="inspector-list" />
  }
}))

const sati = {
  actionId: 'action-id',
  resource: { id: 3 },
  principalInspector: { agentId: 1 },
  otherInspectors: [{ agentId: 2 }, { agentId: undefined }, { agentId: null }]
} as unknown as Sati

const Probe = () => {
  const { values } = useFormikContext<{ sati?: Sati }>()
  return <div data-testid="values">{JSON.stringify(values.sati ?? null)}</div>
}

const renderLayout = (props: Partial<React.ComponentProps<typeof ControlConclusionLayout>> = {}) =>
  render(
    <Formik initialValues={{ sati, targets: [] }} onSubmit={vi.fn()}>
      <>
        <ControlConclusionLayout sati={sati} {...props} />
        <Probe />
      </>
    </Formik>
  )

describe('ControlConclusionLayout', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.mocked(useSati).mockReturnValue({ isSatiEnabled: true })
  })

  it('should always render the conclusions label and the default target', () => {
    vi.mocked(useSati).mockReturnValue({ isSatiEnabled: false })
    renderLayout()

    expect(screen.getByText('Conclusions')).toBeInTheDocument()
  })

  describe('when SATI is enabled', () => {
    it('should render the resource, principal inspector and other inspectors', () => {
      renderLayout()

      expect(screen.getByTestId('resource-form')).toBeInTheDocument()
      expect(screen.getByTestId('inspector-principal')).toBeInTheDocument()
      expect(screen.getByTestId('inspector-list')).toBeInTheDocument()
    })

    it('should render the inspector and operator observations', () => {
      renderLayout()

      expect(screen.getByText("Observations de l'inspecteur")).toBeInTheDocument()
      expect(screen.getByText('Observations de l’opérateur (personne contrôlée)')).toBeInTheDocument()
      expect(screen.getByTestId('observations-master')).toBeInTheDocument()
      expect(screen.queryByText("Observation de l'unité sur le contrôle")).not.toBeInTheDocument()
    })

    it('should exclude the already used agents, ignoring empty ones', () => {
      renderLayout()

      expect(principalProps.mock.calls[0][0].excludedAgentIds).toEqual([1, 2])
      expect(listProps.mock.calls[0][0].excludedAgentIds).toEqual([1, 2])
      expect(listProps.mock.calls[0][0].inspectors).toBe(sati.otherInspectors)
    })

    it('should write the resource and the principal inspector into the form', () => {
      renderLayout()

      fireEvent.click(screen.getByTestId('resource-form'))
      fireEvent.click(screen.getByTestId('inspector-principal'))

      const next = JSON.parse(screen.getByTestId('values').textContent ?? 'null')
      expect(next.resource).toEqual({ id: 7 })
      expect(next.principalInspector).toEqual({ agentId: 1 })
    })

    it('should not crash without sati', () => {
      renderLayout({ sati: undefined })

      expect(principalProps.mock.calls[0][0].excludedAgentIds).toEqual([])
      expect(principalProps.mock.calls[0][0].inspector).toBeUndefined()
    })
  })

  describe('when SATI is disabled', () => {
    beforeEach(() => vi.mocked(useSati).mockReturnValue({ isSatiEnabled: false }))

    it('should hide the SATI sections', () => {
      renderLayout()

      expect(screen.queryByTestId('resource-form')).not.toBeInTheDocument()
      expect(screen.queryByTestId('inspector-principal')).not.toBeInTheDocument()
      expect(screen.queryByTestId('inspector-list')).not.toBeInTheDocument()
      expect(screen.queryByTestId('observations-master')).not.toBeInTheDocument()
    })

    it('should render the unit observations only', () => {
      renderLayout()

      expect(screen.getByText("Observation de l'unité sur le contrôle")).toBeInTheDocument()
      expect(screen.getByTestId('observations-by-unit')).toBeInTheDocument()
    })
  })

  it('should render the seizure and infraction summary slots when given', () => {
    renderLayout({
      seizure: <div data-testid="seizure" />,
      infractionSummary: <div data-testid="infraction-summary" />
    })

    expect(screen.getByTestId('seizure')).toBeInTheDocument()
    expect(screen.getByTestId('infraction-summary')).toBeInTheDocument()
  })

  it('should not render the seizure slot when absent', () => {
    renderLayout()

    expect(screen.queryByTestId('seizure')).not.toBeInTheDocument()
  })
})
