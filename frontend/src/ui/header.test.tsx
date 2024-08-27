import { fireEvent, render, screen } from '../test-utils'
import Header from './header'
import { vi } from 'vitest'
import { RootState } from '../redux/store.ts'
import { configureStore } from '@reduxjs/toolkit'
import { Provider } from 'react-redux'
import { performLogout } from '../features/auth/slice.ts'

const mockStore = (state: Partial<RootState>) => {
  return configureStore({
    preloadedState: state,
    reducer: {
      auth: (state = { user: null }) => state,
    },
  });
};

vi.mock('../features/auth/slice.ts', () => {
  const actual = vi.importActual('../features/auth/slice.ts');
  return {
    ...actual,
    performLogout: vi.fn()
  };
});

describe('Header', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('should render a logout button when user is authenticated', () => {

    const store = mockStore({ auth: { user: { userId: 1, roles: ['USER'] } } });

    render(
      <Provider store={store}><Header /></Provider>
    )
    const logoutButton = screen.getByText('Logout')
    expect(logoutButton).toBeInTheDocument()
  })

  it('should call the logout function when the logout button is clicked', () => {
    const store = mockStore({ auth: { user: { userId: 1, roles: ['USER'] } } });

    const dispatch = vi.fn();
    store.dispatch = dispatch;

    render(
      <Provider store={store}><Header /></Provider>
    )
    const logoutButton = screen.getByText('Logout')

    fireEvent.click(logoutButton)

    expect(dispatch).toHaveBeenCalledWith(performLogout(expect.anything()));
  })

  it('should not render the logout button when user is not authenticated', () => {
    const store = mockStore({ auth: { user: null } });

    render(
      <Provider store={store}><Header /></Provider>
    )
    const logoutButton = screen.queryByText('Logout')
    expect(logoutButton).not.toBeInTheDocument()
  })
})
