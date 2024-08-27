import { render } from '../../../../test-utils.tsx'
import Text from './text.tsx'

describe('Text', () => {
  it('should render truncate text', () => {
    const text = render(
      <div style={{ width: 10 }}>
        <Text data-testid="truncate-comment" as="h3" fontStyle="italic" truncate>
          This is my very long long long long comment
        </Text>
      </div>
    ).getByTestId('truncate-comment')
    expect(text).toHaveStyle(`overflow: hidden`)
    expect(text).toHaveStyle(`white-space: nowrap`)
    expect(text).toHaveStyle(`text-overflow: ellipsis`)
  })
})
