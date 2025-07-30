import { describe, expect, it } from 'vitest'
import { InquiryStatusType } from '../../../../common/types/inquiry'
import InquiryStatusTag from '../inquiry-status-tag'
import { render } from './../../../../../../test-utils'

describe('InquiryStatusTag', () => {
  it('renders should mathc snapshot', () => {
    const wrapper = render(<InquiryStatusTag status={InquiryStatusType.IN_PROGRESS} />)
    expect(wrapper).toMatchSnapshot()
  })
})
