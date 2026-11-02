import { FormikProps } from 'formik'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import { render, screen } from '../../../../../../test-utils'
import { MissionActionData } from '../../../../common/types/mission-action-data'
import { ActionControlInput } from '../../../../mission-action/types/action-type'
import FishingSectorControlInfos from '../fishing-sector-control-infos'

const formProps = vi.hoisted(() => vi.fn())

vi.mock('../../../../mission-action/components/ui/mission-action-control-sector-fishing-form', () => ({
  default: (props: unknown) => {
    formProps(props)
    return <div data-testid="sector-fishing-form" />
  }
}))

describe('FishingSectorControlInfos', () => {
  beforeEach(() => vi.clearAllMocks())

  it('should render the sector fishing form with the given formik', () => {
    const formik = { values: {} } as FormikProps<ActionControlInput>
    render(<FishingSectorControlInfos values={{} as MissionActionData} formik={formik} />)

    expect(screen.getByTestId('sector-fishing-form')).toBeInTheDocument()
    expect(formProps.mock.calls[0][0].formik).toBe(formik)
  })
})
