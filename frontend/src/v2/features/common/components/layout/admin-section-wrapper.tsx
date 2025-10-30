import React, { JSX } from 'react'
import MissionPageError from '../ui/mission-page-error'
import MissionPageLoading from '../ui/mission-page-loading'

type AdminSectionWrapperProps = {
  children: JSX.Element
  isLoading?: boolean
  error?: { message?: string }
}

const AdminSectionWrapper: React.FC<AdminSectionWrapperProps> = ({
  children,
  error,

  isLoading
}: AdminSectionWrapperProps) => {
  if (isLoading) return <MissionPageLoading />
  if (error) return <MissionPageError error={error} />

  return (
    <div
      style={{
        margin: 0,
        display: 'flex',
        minHeight: '100vh',
        maxHeight: '100vh',
        flexDirection: 'column',
        overflowY: 'scroll'
      }}
    >
      {children}
    </div>
  )
}

export default AdminSectionWrapper
