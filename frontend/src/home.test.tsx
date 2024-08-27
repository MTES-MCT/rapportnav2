import { render } from './test-utils.tsx'
import Home from './home.tsx'
import { RootState } from './redux/store.ts'
import { configureStore } from '@reduxjs/toolkit'
import { Provider } from 'react-redux'

const mockStore = (state: Partial<RootState>) => {
  return configureStore({
    preloadedState: state,
    reducer: {
      auth: (state = { user: null }) => state,
    },
  });
};

describe('Home', () => {
  test('should render', () => {

    const store = mockStore({ auth: { user: { userId: 1, roles: ['USER'] } } });
    const { container } = render(
      <Provider store={store}><Home /></Provider>
    )
    expect(container.firstElementChild).not.toBeNull()
  })
})
