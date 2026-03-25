import { describe, expect, it, vi } from 'vitest'
import { render, screen } from '../../../../../test-utils.tsx'
import MissionGeneralInformationControlUnitResource from '../mission-general-information-control-unit-resource.tsx'
import { FieldProps } from 'formik'
import { ControlUnitResource } from '../../../common/types/control-unit-types.ts'

const mockControlUnitResources: ControlUnitResource[] = [
  { id: 1, name: 'Resource 1', controlUnitId: 100 },
  { id: 2, name: 'Resource 2', controlUnitId: 100 }
]

const createMockFieldFormik = (
  value: ControlUnitResource[] = [],
  isResourcesNotUsed?: boolean
): FieldProps<ControlUnitResource[]> =>
  ({
    field: {
      name: 'resources',
      value,
      onChange: vi.fn(),
      onBlur: vi.fn()
    },
    form: {
      values: { isResourcesNotUsed },
      setFieldValue: vi.fn(),
      setFieldTouched: vi.fn(),
      setFieldError: vi.fn()
    },
    meta: {
      value,
      error: undefined,
      touched: false,
      initialValue: value,
      initialTouched: false,
      initialError: undefined
    }
  }) as unknown as FieldProps<ControlUnitResource[]>

describe('MissionGeneralInformationControlUnitResource', () => {
  it('renders the resources select field', () => {
    const fieldFormik = createMockFieldFormik()
    render(
      <MissionGeneralInformationControlUnitResource
        name="resources"
        fieldFormik={fieldFormik}
        controlUnitResources={mockControlUnitResources}
        isMissionFinished={false}
      />
    )

    expect(screen.getByText('Moyen(s) utilisé(s)')).toBeInTheDocument()
    expect(screen.getByText('Ajouter un moyen')).toBeInTheDocument()
  })

  describe('warning message display', () => {
    it('shows warning when mission is finished, no resources selected, and isResourcesNotUsed is false', () => {
      const fieldFormik = createMockFieldFormik([], false)
      render(
        <MissionGeneralInformationControlUnitResource
          name="resources"
          fieldFormik={fieldFormik}
          controlUnitResources={mockControlUnitResources}
          isMissionFinished={true}
        />
      )

      expect(
        screen.getByText('Veuillez renseigner la liste de ressources participant à la mission')
      ).toBeInTheDocument()
    })

    it('shows warning when mission is finished, no resources selected, and isResourcesNotUsed is undefined', () => {
      const fieldFormik = createMockFieldFormik([], undefined)
      render(
        <MissionGeneralInformationControlUnitResource
          name="resources"
          fieldFormik={fieldFormik}
          controlUnitResources={mockControlUnitResources}
          isMissionFinished={true}
        />
      )

      expect(
        screen.getByText('Veuillez renseigner la liste de ressources participant à la mission')
      ).toBeInTheDocument()
    })

    it('does not show warning when mission is NOT finished', () => {
      const fieldFormik = createMockFieldFormik([], false)
      render(
        <MissionGeneralInformationControlUnitResource
          name="resources"
          fieldFormik={fieldFormik}
          controlUnitResources={mockControlUnitResources}
          isMissionFinished={false}
        />
      )

      expect(
        screen.queryByText('Veuillez renseigner la liste de ressources participant à la mission')
      ).not.toBeInTheDocument()
    })

    it('does not show warning when resources are selected', () => {
      const fieldFormik = createMockFieldFormik([{ id: 1, name: 'Resource 1', controlUnitId: 100 }], false)
      render(
        <MissionGeneralInformationControlUnitResource
          name="resources"
          fieldFormik={fieldFormik}
          controlUnitResources={mockControlUnitResources}
          isMissionFinished={true}
        />
      )

      expect(
        screen.queryByText('Veuillez renseigner la liste de ressources participant à la mission')
      ).not.toBeInTheDocument()
    })

    it('does not show warning when isResourcesNotUsed is true', () => {
      const fieldFormik = createMockFieldFormik([], true)
      render(
        <MissionGeneralInformationControlUnitResource
          name="resources"
          fieldFormik={fieldFormik}
          controlUnitResources={mockControlUnitResources}
          isMissionFinished={true}
        />
      )

      expect(
        screen.queryByText('Veuillez renseigner la liste de ressources participant à la mission')
      ).not.toBeInTheDocument()
    })
  })

  describe('disabled state', () => {
    it('disables the add button when disabled prop is true', () => {
      const fieldFormik = createMockFieldFormik()
      render(
        <MissionGeneralInformationControlUnitResource
          name="resources"
          disabled={true}
          fieldFormik={fieldFormik}
          controlUnitResources={mockControlUnitResources}
          isMissionFinished={false}
        />
      )

      expect(screen.getByText('Ajouter un moyen').closest('button')).toBeDisabled()
    })
  })
})
