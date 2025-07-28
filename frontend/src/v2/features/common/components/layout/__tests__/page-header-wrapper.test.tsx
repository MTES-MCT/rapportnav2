import { vi } from 'vitest'
import { render } from '../../../../../../test-utils'
import PageHeaderWrapper from '../page-header-wrapper'

const onClose = vi.fn()

describe('PageHeaderWrapper', () => {
  it('should match snapshot', async () => {
    const wrapper = render(
      <PageHeaderWrapper
        banner={<>Banner</>}
        date={<>Date</>}
        tags={<>tags</>}
        utcTime={<>UTC time</>}
        onClickClose={onClose}
      />
    )
    expect(wrapper).toMatchSnapshot()
  })
})
