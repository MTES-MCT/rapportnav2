import { vi } from 'vitest'
import { render, screen } from '../../../test-utils.tsx'
import InquiryListPage from '../inquiry-list-page.tsx'
import useInquiriesQuery from '../../features/inquiry/services/use-inquiries.tsx'
import useCreateInquiryMutation from '../../features/inquiry/services/use-create-inquiry.tsx'

vi.mock('../../features/inquiry/services/use-inquiries.tsx', () => ({
  default: vi.fn()
}))

vi.mock('../../features/inquiry/services/use-create-inquiry.tsx', () => ({
  default: vi.fn()
}))

describe('InquiryListPage', () => {
  beforeEach(() => {
    vi.clearAllMocks()

    vi.mocked(useInquiriesQuery).mockReturnValue({
      isLoading: false,
      data: []
    } as any)

    vi.mocked(useCreateInquiryMutation).mockReturnValue({
      mutateAsync: vi.fn()
    } as any)
  })

  it('should render page title', () => {
    render(<InquiryListPage />)
    expect(
      screen.getByText('Mes contrôles croisés (vérifications post contrôle terrain et enquêtes)')
    ).toBeInTheDocument()
  })

  it('should render create inquiry button', () => {
    render(<InquiryListPage />)
    expect(screen.getByText('Créer un contrôle croisé')).toBeInTheDocument()
  })

  it('should show empty state when no inquiries', () => {
    render(<InquiryListPage />)
    expect(screen.getByText('Aucune mission pour cette période de temps.')).toBeInTheDocument()
  })

  it('should show loading state', () => {
    vi.mocked(useInquiriesQuery).mockReturnValue({
      isLoading: true,
      data: undefined
    } as any)
    render(<InquiryListPage />)
    expect(screen.getByTestId('mission-list-loader')).toBeInTheDocument()
  })

  it('should render date range navigator with year timeframe', () => {
    render(<InquiryListPage />)
    expect(screen.getByTestId('date-range-navigator')).toBeInTheDocument()
  })
})
