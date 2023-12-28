import { ReactNode } from 'react'
import { GlobalStyle, THEME, ThemeProvider } from '@mtes-mct/monitor-ui'
import { CustomProvider as RsuiteCustomProvider } from 'rsuite'
import rsuiteFrFr from 'rsuite/locales/fr_FR'

interface UIThemeWrapperProps {
  children: ReactNode
}

const UIThemeWrapper: React.FC<UIThemeWrapperProps> = ({children}) => {
  return (
    <ThemeProvider theme={THEME}>
      {/* <OnlyFontGlobalStyle /> */}
      <GlobalStyle/>
      <RsuiteCustomProvider locale={rsuiteFrFr}>{children}</RsuiteCustomProvider>
    </ThemeProvider>
  )
}

export default UIThemeWrapper
