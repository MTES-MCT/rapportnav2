import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import apolloClient from '../../apollo-client.ts'

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
      localStorage.removeItem('jwt');
      state.user = null;
    },
    login: (state, action: PayloadAction<string>) => {
      const token = action.payload;
      localStorage.setItem('jwt', token);
      state.user = deserializeToken(token);
    }
  },
});

export const { checkLoginStatus, logout, login } = slice.actions;

export default slice.reducer;
