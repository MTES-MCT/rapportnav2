import { describe, expect, it, vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import { Inquiry, InquiryOriginType, InquiryTargetType } from '../../../../common/types/inquiry'
import InquiryGeneralInfoForm from '../inquiry-general-information-form'

// Mock hooks
vi.mock('../../../../common/hooks/use-control-check', () => ({
  usecontrolCheck: () => ({
    controlCheckRadioBooleanOptions: [
      { label: 'Oui', value: true },
      { label: 'Non', value: false }
    ]
  })
}))

vi.mock('../../../hooks/use-inquiry', () => ({
  useInquiry: () => ({
    inquiryOriginOptions: [
      { label: 'Contrôle d\'opportunité', value: InquiryOriginType.OPPORTUNITY_CONTROL }
    ],
    inquiryTargetOptions: [
      { label: 'Navire', value: InquiryTargetType.VEHICLE },
      { label: 'Etablissement', value: InquiryTargetType.COMPANY }
    ]
  })
}))

let mockAgents: any[] | undefined = [
  { id: 1, firstName: 'John', lastName: 'Doe' },
  { id: 2, firstName: 'Jane', lastName: 'Smith' }
]

vi.mock('../../../../common/services/use-agents', () => ({
  default: () => ({ data: mockAgents })
}))

const mockHandleSubmit = vi.fn()
let mockInitValue: any = {
  id: '1',
  type: InquiryTargetType.VEHICLE,
  dates: [new Date('2023-01-01'), new Date('2023-01-02')],
  origin: InquiryOriginType.OPPORTUNITY_CONTROL,
  agentId: 1,
  isSignedByInspector: false
}

vi.mock('../../../hooks/use-inquiry-general-information', () => ({
  useInquiryGeneralInformation: () => ({
    initValue: mockInitValue,
    handleSubmit: mockHandleSubmit,
    validationSchema: undefined
  })
}))

// Stub complex UI sub-components
vi.mock('@mtes-mct/monitor-ui', async () => {
  const actual = await vi.importActual('@mtes-mct/monitor-ui')
  return {
    ...actual,
    FormikEffect: () => null,
    FormikSelect: ({ label, name, options }: any) => (
      <div data-testid={`formik-select-${name}`} data-options={JSON.stringify(options)}>
        {label}
      </div>
    ),
    FormikMultiRadio: ({ label, name }: any) => <div data-testid={`formik-radio-${name}`}>{label}</div>
  }
})

vi.mock('../../../../common/components/ui/formik-date-range-picker', () => ({
  FormikDateRangePicker: ({ name }: any) => <div data-testid={`date-range-picker-${name}`}>Date Range</div>
}))

vi.mock('../../../../common/components/ui/formik-search-vessel', () => ({
  FormikSearchVessel: ({ name }: any) => <div data-testid="search-vessel">{name}</div>
}))

vi.mock('../../../../common/components/ui/formik-establishment', () => ({
  FormikEstablishment: ({ name }: any) => <div data-testid="establishment">{name}</div>
}))

const inquiry: Inquiry = {
  id: '1',
  type: InquiryTargetType.VEHICLE,
  startDateTimeUtc: '2023-01-01T00:00:00Z',
  endDateTimeUtc: '2023-01-02T00:00:00Z',
  origin: InquiryOriginType.OPPORTUNITY_CONTROL,
  agentId: 1,
  isSignedByInspector: false
}

const onChange = vi.fn()

describe('InquiryGeneralInfoForm', () => {
  afterEach(() => {
    vi.clearAllMocks()
    mockInitValue = {
      id: '1',
      type: InquiryTargetType.VEHICLE,
      dates: [new Date('2023-01-01'), new Date('2023-01-02')],
      origin: InquiryOriginType.OPPORTUNITY_CONTROL,
      agentId: 1,
      isSignedByInspector: false
    }
    mockAgents = [
      { id: 1, firstName: 'John', lastName: 'Doe' },
      { id: 2, firstName: 'Jane', lastName: 'Smith' }
    ]
  })

  it('renders the form with all base fields and vessel search when type is VEHICLE', () => {
    const { getByTestId, queryByTestId } = render(
      <InquiryGeneralInfoForm inquiry={inquiry} onChange={onChange} />
    )
    expect(getByTestId('formik-select-origin')).toBeInTheDocument()
    expect(getByTestId('formik-select-agentId')).toBeInTheDocument()
    expect(getByTestId('formik-select-type')).toBeInTheDocument()
    expect(getByTestId('formik-radio-isSignedByInspector')).toBeInTheDocument()
    expect(getByTestId('date-range-picker-dates')).toBeInTheDocument()
    expect(getByTestId('search-vessel')).toBeInTheDocument()
    expect(queryByTestId('establishment')).not.toBeInTheDocument()
  })

  it('shows establishment and hides vessel when type is COMPANY', () => {
    mockInitValue = { ...mockInitValue, type: InquiryTargetType.COMPANY }
    const { getByTestId, queryByTestId } = render(
      <InquiryGeneralInfoForm inquiry={inquiry} onChange={onChange} />
    )
    expect(getByTestId('establishment')).toBeInTheDocument()
    expect(queryByTestId('search-vessel')).not.toBeInTheDocument()
  })

  it('does not render form content when initValue is undefined', () => {
    mockInitValue = undefined
    const { container, queryByTestId } = render(
      <InquiryGeneralInfoForm inquiry={inquiry} onChange={onChange} />
    )
    expect(queryByTestId('formik-select-origin')).not.toBeInTheDocument()
    expect(container.querySelector('form')).toBeTruthy()
  })

  it('passes correctly mapped agent options to the agent select', () => {
    const { getByTestId } = render(<InquiryGeneralInfoForm inquiry={inquiry} onChange={onChange} />)
    const agentSelect = getByTestId('formik-select-agentId')
    const options = JSON.parse(agentSelect.getAttribute('data-options')!)
    expect(options).toEqual([
      { value: 1, label: 'John Doe' },
      { value: 2, label: 'Jane Smith' }
    ])
  })

  it('does not crash when agents data is undefined', () => {
    mockAgents = undefined
    const { getByTestId } = render(<InquiryGeneralInfoForm inquiry={inquiry} onChange={onChange} />)
    const agentSelect = getByTestId('formik-select-agentId')
    const options = JSON.parse(agentSelect.getAttribute('data-options')!)
    expect(options).toEqual([])
  })
})
