import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit'
import AuthToken from '../../auth/token.ts'

interface User {
  userId: number;
  exp: string;
  sub: string;
  roles: string[];
}

interface AuthState {
  user: User | null;
}

const initialState: AuthState = {
  user: null,
};

const deserializeToken = (token: string): User | null => {
  try {
    return JSON.parse(atob(token.split('.')[1]));
  } catch (error) {
    return null;
  }
};

const slice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    checkLoginStatus: (state) => {
      const token = localStorage.getItem('jwt');
      if (token) {
        const user = deserializeToken(token);
        if (user) {
          state.user = user;
        } else {
          state.user = null;
        }
      } else {
        state.user = null;
      }
    },
    logout: (state) => {
      state.user = null;
    },
    login: (state, action: PayloadAction<string>) => {
      const token = action.payload;
      localStorage.setItem('jwt', token);
      state.user = deserializeToken(token);
    }
  },
});

export const performLogout = createAsyncThunk(
  'auth/logout',
  async ({ navigate, apolloClient }: { navigate: any; apolloClient: any }, { dispatch }) => {
    const authToken = new AuthToken();

    authToken.remove();
    await apolloClient.clearStore();
    apolloClient.cache.evict({});
    dispatch(logout());
    navigate('/login', { replace: true });
  }
);


export const { checkLoginStatus, logout, login } = slice.actions;

export default slice.reducer;
