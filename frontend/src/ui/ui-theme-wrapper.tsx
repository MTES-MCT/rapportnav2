import { ReactNode } from 'react'
import { GlobalStyle, THEME } from '@mtes-mct/monitor-ui'
import { CustomProvider as RsuiteCustomProvider } from 'rsuite'
import rsuiteFrFr from 'rsuite/locales/fr_FR'
import { ThemeProvider } from 'styled-components'

import 'react-toastify/dist/ReactToastify.css'
import 'rsuite/dist/rsuite.css'
import '@mtes-mct/monitor-ui/assets/stylesheets/rsuite-override.css'
import '../assets/css/index.css'

// const UntypedThemeProvider = ThemeProvider as any

interface UIThemeWrapperProps {
  children: ReactNode
}

const UIThemeWrapper: React.FC<UIThemeWrapperProps> = ({ children }) => {
  return (
    <ThemeProvider theme={THEME}>
      <GlobalStyle />
      <RsuiteCustomProvider locale={rsuiteFrFr}>{children}</RsuiteCustomProvider>
    </ThemeProvider>
  )
}

export default UIThemeWrapper
