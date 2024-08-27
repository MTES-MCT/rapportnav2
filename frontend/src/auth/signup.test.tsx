import { render, screen } from '../test-utils.tsx'
import SignUp from './signup.tsx'
import { Provider } from 'react-redux'
import { RootState } from '../redux/store.ts'
import { configureStore } from '@reduxjs/toolkit'

const mockStore = (state: Partial<RootState>) => {
  return configureStore({
    preloadedState: state,
    reducer: {
      auth: (state = { user: null }) => state,
    },
  });
};
describe('SignUp', () => {
  test('should render', () => {
    const store = mockStore({ auth: { user: null } });
    render(
      <Provider store={store}><SignUp /></Provider>
    )
    expect(screen.getByText("S'inscrire")).toBeInTheDocument()
  })
})
