
import {
    createBrowserRouter
  } from "react-router-dom";
import ErrorPage from '../error-page';
import Mission from '../pam/mission/mission';
import Login from "../auth/login";
import Home from "../home";
import MissionsPage from "../pam/missions/missions-page";

export const router = createBrowserRouter([
    {
      path: "/",
      element: <Home />,
      errorElement: <ErrorPage />
    },
    {
      path: 'login',
      element: <Login />
    },
    {
      path: "pam/missions",
      element: <MissionsPage />,
      errorElement: <ErrorPage />
    },
    {
      path: "pam/missions/:missionsId",
      element: <Mission />,
    },
]);