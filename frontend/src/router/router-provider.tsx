
import {
    RouterProvider as ReactDomRouterProvider,
    RouterProviderProps as ReactDomRouterProviderProps
  } from "react-router-dom";

export type RouterProviderProps = ReactDomRouterProviderProps

export const RouterProvider = (props: RouterProviderProps) => (
  <ReactDomRouterProvider {...props}  />
)

export default RouterProvider