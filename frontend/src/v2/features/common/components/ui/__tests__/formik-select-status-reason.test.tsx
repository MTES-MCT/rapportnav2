import { Formik } from 'formik'
import { vi } from 'vitest'
import { render, screen, fireEvent } from '../../../../../../test-utils'
import FormikSelectStatusReason from '../formik-select-status-reason'
import { ActionStatusType } from '@common/types/action-types.ts'

const handleSubmit = vi.fn()

describe('FormikSelectStatusReason', () => {
  it('should match the snapshot', () => {
    const wrapper = render(
      <Formik initialValues={{ reason: '' }} onSubmit={handleSubmit}>
        <FormikSelectStatusReason name="reason" label="Motif" />
      </Formik>
    )
    expect(wrapper).toMatchSnapshot()
  })

  describe('reasons', () => {
    it('should not show reasons for UNKNOWN status', () => {
      render(<FormikSelectStatusReason name="reason" label="Motif" status={ActionStatusType.UNKNOWN} />)
      expect(screen.queryByText('Motif')).toBeNull()
    })
    it('should not show reasons for NAVIGATING status', () => {
      render(<FormikSelectStatusReason name="reason" label="Motif" status={ActionStatusType.NAVIGATING} />)
      expect(screen.queryByText('Motif')).toBeNull()
    })
    it('should not show reasons for ANCHORED status', () => {
      render(<FormikSelectStatusReason name="reason" label="Motif" status={ActionStatusType.ANCHORED} />)
      expect(screen.queryByText('Motif')).toBeNull()
    })
    it('should show reasons for DOCKED status', () => {
      render(
        <Formik initialValues={{ reason: '' }} onSubmit={handleSubmit}>
          <FormikSelectStatusReason name="reason" label="Motif" status={ActionStatusType.DOCKED} />
        </Formik>
      )
      expect(screen.queryByText('Motif')).not.toBeNull()
      fireEvent.click(screen.getByTestId('picker-toggle-input'))
      expect(screen.getByText('Maintenance')).toBeInTheDocument()
      expect(screen.getByText('Météo')).toBeInTheDocument()
      expect(screen.getByText('Représentation')).toBeInTheDocument()
      expect(screen.getByText('Administration')).toBeInTheDocument()
      expect(screen.getByText('MCO/Logistique')).toBeInTheDocument()
      expect(screen.getByText('Contrôle portuaire')).toBeInTheDocument()
    })
    it('should show reasons for UNAVAILABLE status', () => {
      render(
        <Formik initialValues={{ reason: '' }} onSubmit={handleSubmit}>
          <FormikSelectStatusReason name="reason" label="Motif" status={ActionStatusType.UNAVAILABLE} />
        </Formik>
      )
      expect(screen.queryByText('Motif')).not.toBeNull()
      fireEvent.click(screen.getByTestId('picker-toggle-input'))
      expect(screen.getByText('Technique')).toBeInTheDocument()
      expect(screen.getByText('Personnel')).toBeInTheDocument()
      expect(screen.getByText('Autre')).toBeInTheDocument()
    })
  })
})
