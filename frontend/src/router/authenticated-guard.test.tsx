import React from 'react';
import { vi } from 'vitest'
import { Provider } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import MissionsPage from '../pam/missions/missions-page';
import AuthenticatedGuard from './authenticated-guard';
import { RootState } from '../redux/store';
import { screen, waitFor } from '@testing-library/react'
import { render } from '@testing-library/react'
import SignUp from '../auth/signup.tsx'
import Login from '../auth/login.tsx'
import AdminPage from '../admin/admin-page.tsx'
import useAuth from '../auth/use-auth.tsx'

// Mock react-router-dom and useNavigate
const mockNavigate = vi.fn();
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  };
});

vi.mock('../auth/use-auth', () => ({
  default: vi.fn()
}))

const mockStore = (state: Partial<RootState>) => {
  return configureStore({
    preloadedState: state,
    reducer: {
      auth: (state = { user: null }) => state,
    },
  });
};

describe('AuthenticatedGuard', () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it('renders MissionsPage when user navigates to /pam/missions and is logged in', async () => {
    // Configurer le store avec un utilisateur connecté
    const store = mockStore({ auth: { user: { userId: 1, roles: ['ADMIN'] } } });

    ;(useAuth as any).mockReturnValue({
      isAuthenticated: false,
      logout: vi.fn()
    })

    render(
      <Provider store={store}>
        <MemoryRouter initialEntries={['/pam/missions']}>
          <Routes>
            <Route
              path="/pam/missions"
              element={
                <AuthenticatedGuard>
                  <AdminPage/>
                </AuthenticatedGuard>
              }
            />
          </Routes>
        </MemoryRouter>
      </Provider>
    );

    await waitFor(() => {
      expect(screen.getByText('Hello admin')).toBeTruthy();
    });
  });

  test('redirects to login when user is not logged in', async () => {
    const store = mockStore({ auth: { user: null } });

    render(
      <Provider store={store}>
        <MemoryRouter initialEntries={['/pam/missions']}>
          <Routes>
            <Route
              path="/pam/missions"
              element={
                <AuthenticatedGuard>
                  <MissionsPage />
                </AuthenticatedGuard>
              }
            />
          </Routes>
        </MemoryRouter>
      </Provider>
    );

    await waitFor(() => {
      expect(mockNavigate).toHaveBeenCalledWith('/login', { replace: true });
    });
  });

  test('throws an error when user does not have admin privileges and adminOnly is true', async () => {
    const store = mockStore({ auth: { user: { userId: 1, roles: ['USER'] } } });

    expect(() => {
      render(
        <Provider store={store}>
          <MemoryRouter initialEntries={['/admin']}>
            <Routes>
              <Route
                path="/admin"
                element={
                  <AuthenticatedGuard adminOnly={true}>
                    <div>Admin Page</div>
                  </AuthenticatedGuard>
                }
              />
            </Routes>
          </MemoryRouter>
        </Provider>
      );
    }).toThrow("Access denied: User does not have the necessary admin privileges.");
  });
});
