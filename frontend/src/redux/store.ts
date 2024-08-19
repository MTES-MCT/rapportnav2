import { configureStore } from '@reduxjs/toolkit';
import authReducer from '../features/auth/auth.ts';

export const store = configureStore({
  reducer: {
    auth: authReducer,
  },
});

// Typages pour le store
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
