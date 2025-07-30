import { Icon } from '@mtes-mct/monitor-ui'
import { describe, expect, it } from 'vitest'
import { render } from '../../../../../../test-utils'
import InquiryStatusCompleteness from '../inquiry-status-completeness'

describe('InquiryStatusCompleteness', () => {
  it('renders should match snapshot', () => {
    const wrapper = render(<InquiryStatusCompleteness status={{ icon: Icon.Account, text: 'status', color: 'red' }} />)
    expect(wrapper).toMatchSnapshot()
  })
})
