import { RouterProvider } from './router/router-provider'
import { router } from './router/router'
import UIThemeWrapper from './ui/ui-theme-wrapper'
import { ApolloProvider } from '@apollo/client'
import client from './apollo-client'

const App: React.FC = () => {
  return (
    <ApolloProvider client={client}>
      <UIThemeWrapper>
        <RouterProvider router={router} />
      </UIThemeWrapper>
    </ApolloProvider>
  )
}

export default App
