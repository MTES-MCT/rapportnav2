import { render, screen } from '../test-utils'
import PageWrapper from './page-wrapper'
import useAuth from '../auth/use-auth'
import { vi } from 'vitest'
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


describe('PageWrapper component', () => {
  it('should not render the sidebar when not authenticated', () => {
    const store = mockStore({ auth: { user: null } });

    render(
      <Provider store={store}>
      <PageWrapper>
        <div data-testid="child">Child Content</div>
      </PageWrapper>
      </Provider>
    )

    const sidebarElement = screen.queryByTitle('missions')
    expect(sidebarElement).toBeNull()
  })
  it('should not render the sidebar when the showMenu prop is false', () => {
    const store = mockStore({ auth: { user: { userId: 1, roles: ['USER'] } } });

    render(
      <Provider store={store}>
        <PageWrapper showMenu={false}>
          <div data-testid="child">Child Content</div>
        </PageWrapper>
      </Provider>
    )

    const sidebarElement = screen.queryByTitle('missions')
    expect(sidebarElement).toBeNull()
  })
  it('should render the sidebar when the showMenu prop is true and authenticated', () => {
    const store = mockStore({ auth: { user: { userId: 1, roles: ['USER'] } } });

    render(
      <Provider store={store}>
        <PageWrapper>
          <div data-testid="child">Child Content</div>
        </PageWrapper>
      </Provider>
    )

    const sidebarElement = screen.queryByTitle('missions')
    expect(sidebarElement).not.toBeNull()
  })
  it('should render a custom header when provided', () => {
    const store = mockStore({ auth: { user: { userId: 1, roles: ['USER'] } } });

    render(
      <Provider store={store}>
        <PageWrapper header={<p>custom-header</p>}>
          <div data-testid="child">Child Content</div>
        </PageWrapper>
      </Provider>
    )

    const headerElement = screen.getByText('custom-header')

    expect(headerElement).toBeInTheDocument()
  })
  it('should render the default header when no extra header provided', () => {
    const store = mockStore({ auth: { user: { userId: 1, roles: ['USER'] } } });

    render(
      <Provider store={store}>
        <PageWrapper>
          <div data-testid="child">Child Content</div>
        </PageWrapper>
      </Provider>
    )

    const headerElement = screen.getByText('Rapport Nav')

    expect(headerElement).toBeInTheDocument()
  })
  it('should render the sidebar when authenticated', () => {
    const store = mockStore({ auth: { user: { userId: 1, roles: ['USER'] } } });

    render(
      <Provider store={store}>
        <PageWrapper>
          <div data-testid="child">Child Content</div>
        </PageWrapper>
      </Provider>
    )

    const sidebarElement = screen.queryByTitle('missions')

    expect(sidebarElement).toBeInTheDocument()
  })
})
