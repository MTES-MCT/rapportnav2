import { describe, expect, it, vi } from 'vitest'
import { fireEvent, render, screen, waitFor } from '../../../../../test-utils.tsx'
import MissionGeneralInformationControlUnitResource from '../mission-general-information-control-unit-resource.tsx'
import { FieldProps } from 'formik'
import { ControlUnitResource, ControlUnitResourceType } from '../../../common/types/control-unit-types.ts'

// The rsuite picker is hard to drive in jsdom: swap it for a native select exposing the same onChange contract.
vi.mock('@mtes-mct/monitor-ui', async importOriginal => {
  const actual = await importOriginal<typeof import('@mtes-mct/monitor-ui')>()
  return {
    ...actual,
    Select: ({
      name,
      value,
      options,
      onChange
    }: {
      name: string
      value?: number
      options: { value: number; label: string }[]
      onChange: (next?: number) => void
    }) => (
      <select data-testid={name} value={value ?? ''} onChange={e => onChange(Number(e.target.value))}>
        <option value="" />
        {options.map(o => (
          <option key={o.value} value={o.value}>
            {o.label}
          </option>
        ))}
      </select>
    )
  }
})

const controlUnitResources: ControlUnitResource[] = [
  { id: 1, name: 'Voiture A', controlUnitId: 100, type: ControlUnitResourceType.CAR },
  { id: 2, name: 'Moto B', controlUnitId: 100, type: ControlUnitResourceType.MOTORCYCLE }
]

describe('MissionGeneralInformationControlUnitResource - changing the selected resource', () => {
  it('does not carry the previous resource usage value over to the newly selected resource', async () => {
    const setFieldValue = vi.fn()
    const value = [{ ...controlUnitResources[0], nbKms: 120 }]
    const fieldFormik = {
      field: { name: 'resources', value, onChange: vi.fn(), onBlur: vi.fn() },
      form: { values: {}, setFieldValue, setFieldTouched: vi.fn(), setFieldError: vi.fn() },
      meta: { value, touched: false, initialValue: value, initialTouched: false }
    } as unknown as FieldProps<ControlUnitResource[]>

    render(
      <MissionGeneralInformationControlUnitResource
        name="resources"
        fieldFormik={fieldFormik}
        controlUnitResources={controlUnitResources}
        isMissionFinished={false}
      />
    )

    fireEvent.change(screen.getByTestId('resources.0.id'), { target: { value: '2' } })

    await waitFor(() =>
      expect(setFieldValue).toHaveBeenLastCalledWith('resources', [
        expect.objectContaining({ id: 2, type: ControlUnitResourceType.MOTORCYCLE, nbKms: undefined })
      ])
    )
    expect(setFieldValue).not.toHaveBeenCalledWith('resources', [expect.objectContaining({ id: 2, nbKms: 120 })])
  })
})
