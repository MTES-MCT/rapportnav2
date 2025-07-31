import { describe, expect, it } from 'vitest'
import InquirySummaryItem from '../inquiry-timeline-summary-item'
import { render } from './../../../../../../test-utils'

describe('InquiryTimelineSummaryItem', () => {
  it('render should match snapshot', () => {
    const wrapper = render(<InquirySummaryItem label="my label" value={'value of my summary'} />)
    expect(wrapper).toMatchSnapshot()
  })
})
