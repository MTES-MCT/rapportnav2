class AuthToken {
  LOCAL_STORAGE_JWT = 'jwt'

  set(token: string): void {
    try {
      localStorage.setItem(this.LOCAL_STORAGE_JWT, token)
    } catch (error) {
      // Handle the error here (e.g., log or display an error message)
      console.error('Error while setting token in localStorage:', error)
    }
  }

  get(): string | null {
    try {
      return localStorage.getItem(this.LOCAL_STORAGE_JWT)
    } catch (error) {
      // Handle the error here (e.g., log or display an error message)
      console.error('Error while getting token from localStorage:', error)
      return null
    }
  }

  ermove(): void {
    try {
      localStorage.removeItem(this.LOCAL_STORAGE_JWT)
    } catch (error) {
      // Handle the error here (e.g., log or display an error message)
      console.error('Error while removing token from localStorage:', error)
    }
  }
}

export default AuthToken
