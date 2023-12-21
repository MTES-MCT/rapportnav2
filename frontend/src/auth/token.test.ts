import AuthToken from "./token";

// TODO move this  elsewhere
// Mock localStorage for testing purposes
const localStorageMock = (() => {
  let store: { [key: string]: string } = {};
  return {
    getItem: (key: string) => store[key] || null,
    setItem: (key: string, value: string) => {
      store[key] = value.toString();
    },
    removeItem: (key: string) => {
      delete store[key];
    },
    clear: () => {
      store = {};
    },
  };
})();
Object.defineProperty(window, 'localStorage', {value: localStorageMock});

describe('AuthToken', () => {
  afterEach(() => {
    localStorageMock.clear();
  });

  describe('set', () => {
    it('should set the token in localStorage', () => {
      const authToken = new AuthToken();
      const token = 'your_jwt_token_here';

      authToken.set(token);
      expect(localStorage.getItem('jwt')).toBe(token);
    });
  });

  describe('get', () => {
    it('should return the token from localStorage', () => {
      const authToken = new AuthToken();
      const token = 'your_jwt_token_here';
      localStorage.setItem('jwt', token);

      const retrievedToken = authToken.get();
      expect(retrievedToken).toBe(token);
    });

    it('should return null if the token is not set', () => {
      const authToken = new AuthToken();

      const retrievedToken = authToken.get();
      expect(retrievedToken).toBeNull();
    });
  });

  describe('remove', () => {
    it('should remove the token from localStorage', () => {
      const authToken = new AuthToken();
      const token = 'your_jwt_token_here';
      localStorage.setItem('jwt', token);

      expect(localStorage.getItem('jwt')).toBe(token);

      authToken.remove();

      expect(localStorage.getItem('jwt')).toBeNull();
    });

    it('should not throw an error when token is not set', () => {
      const authToken = new AuthToken();

      expect(() => {
        authToken.remove();
      }).not.toThrow();
    });
  });
});
