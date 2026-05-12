import { Formik } from 'formik'
import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import { FormikSearchPort } from '../formik-search-port'
import * as usePortService from '../../../services/use-port-service'

vi.mock('../../../services/use-port-service', () => ({
  usePortListAllQuery: vi.fn().mockReturnValue({ data: undefined })
}))

const handleSubmit = vi.fn()
describe('FormikSearchPort', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    vi.mocked(usePortService.usePortListAllQuery).mockReturnValue({ data: undefined } as any)
  })

  it('should call usePortListAllQuery on mount', () => {
    render(
      <Formik initialValues={{ portLocode: '' }} onSubmit={handleSubmit}>
        <FormikSearchPort name="portLocode" isLight={true} label="Port" />
      </Formik>
    )
    expect(usePortService.usePortListAllQuery).toHaveBeenCalled()
  })

  it('should render with port data', () => {
    vi.mocked(usePortService.usePortListAllQuery).mockReturnValue({
      data: [
        { locode: 'FRPAR', name: 'Paris' },
        { locode: 'FRLEH', name: 'Le Havre' }
      ]
    } as any)

    const { container } = render(
      <Formik initialValues={{ portLocode: '' }} onSubmit={handleSubmit}>
        <FormikSearchPort name="portLocode" isLight={true} label="Port" />
      </Formik>
    )
    expect(container).toBeTruthy()
  })
})
