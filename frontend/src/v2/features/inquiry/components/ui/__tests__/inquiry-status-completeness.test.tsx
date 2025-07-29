import { describe, expect, it } from 'vitest'
import { render } from '../../../../../../test-utils'
import { InquiryStatusType } from '../../../../common/types/inquiry'
import InquiryStatusCompleteness from '../inquiry-status-completeness'

describe('InquiryStatusCompleteness', () => {
  it('renders should match snapshot', () => {
    const wrapper = render(<InquiryStatusCompleteness status={InquiryStatusType.NEW} />)
    expect(wrapper).toMatchSnapshot()
  })
})
