import { useGlobalRoutes } from '@router/use-global-routes'
import { FC, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'

const Home2: FC = () => {
  const navigate = useNavigate()
  const { homeUrl } = useGlobalRoutes()

  useEffect(() => {
    if (!homeUrl) return
    navigate(homeUrl, { replace: true })
  }, [homeUrl, navigate])

  return <div />
}

export default Home2
