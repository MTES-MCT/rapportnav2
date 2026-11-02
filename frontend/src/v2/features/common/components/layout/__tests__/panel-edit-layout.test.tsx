import { fireEvent, render, screen } from '../../../../../../test-utils'
import { describe, expect, it, vi } from 'vitest'
import PanelEditLayout, { PanelEditChildrenParams } from '../panel-edit-layout'

const renderLayout = (props: { defaultEdit?: boolean; title?: string } = {}) => {
  const children = vi.fn(({ edit, setEdit }: PanelEditChildrenParams) => (
    <div>
      <span data-testid="mode">{edit ? 'edit' : 'read'}</span>
      <button onClick={() => setEdit(false)}>close</button>
    </div>
  ))
  const utils = render(<PanelEditLayout {...props}>{children}</PanelEditLayout>)
  return { children, ...utils }
}

describe('PanelEditLayout', () => {
  it('should render the title', () => {
    renderLayout({ title: 'Mon panneau' })

    expect(screen.getByText('Mon panneau')).toBeInTheDocument()
  })

  it('should start in edit mode by default and not show the edit button', () => {
    renderLayout()

    expect(screen.getByTestId('mode')).toHaveTextContent('edit')
    expect(screen.queryByTitle('Editer')).not.toBeInTheDocument()
  })

  it('should start read only when defaultEdit is false and show the edit button', () => {
    renderLayout({ defaultEdit: false })

    expect(screen.getByTestId('mode')).toHaveTextContent('read')
    expect(screen.getByTitle('Editer')).toBeInTheDocument()
  })

  it('should give edit and setEdit to its children', () => {
    const { children } = renderLayout()

    expect(children).toHaveBeenCalledWith({ edit: true, setEdit: expect.any(Function) })
  })

  it('should leave edit mode when a child calls setEdit(false)', () => {
    renderLayout()

    fireEvent.click(screen.getByText('close'))

    expect(screen.getByTestId('mode')).toHaveTextContent('read')
    expect(screen.getByTitle('Editer')).toBeInTheDocument()
  })

  it('should go back to edit mode when clicking the edit button', () => {
    renderLayout({ defaultEdit: false })

    fireEvent.click(screen.getByTitle('Editer'))

    expect(screen.getByTestId('mode')).toHaveTextContent('edit')
    expect(screen.queryByTitle('Editer')).not.toBeInTheDocument()
  })

  it('should follow defaultEdit when it changes', () => {
    const { rerender, children } = renderLayout({ defaultEdit: true })
    expect(screen.getByTestId('mode')).toHaveTextContent('edit')

    rerender(<PanelEditLayout defaultEdit={false}>{children}</PanelEditLayout>)

    expect(screen.getByTestId('mode')).toHaveTextContent('read')
  })

  it('should keep the user choice when defaultEdit does not change', () => {
    const { rerender, children } = renderLayout({ defaultEdit: false })
    fireEvent.click(screen.getByTitle('Editer'))
    expect(screen.getByTestId('mode')).toHaveTextContent('edit')

    rerender(<PanelEditLayout defaultEdit={false}>{children}</PanelEditLayout>)

    expect(screen.getByTestId('mode')).toHaveTextContent('edit')
  })
})
