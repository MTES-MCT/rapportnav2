import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface User {
  userId: number;
  exp: string;
  sub: string;
  roles: [];
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

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    checkLoginStatus: (state) => {
      const token = localStorage.getItem('token');
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
      localStorage.removeItem('token');
      state.user = null;
    },
    login: (state, action: PayloadAction<string>) => {
      const token = action.payload;
      localStorage.setItem('token', token);
      state.user = deserializeToken(token);
    }
  },
});

export const { checkLoginStatus, logout, login } = authSlice.actions;

export default authSlice.reducer;
