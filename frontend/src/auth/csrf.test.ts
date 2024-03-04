import { csrfToken } from './csrf';

describe('csrfToken', () => {
  // Mocking document.cookie for testing
  const originalDocumentCookie = document.cookie;
  beforeAll(() => {
    Object.defineProperty(document, 'cookie', {
      writable: true,
      value: 'XSRF-TOKEN=abc123; other_cookie=def456',
    });
  });

  afterAll(() => {
    document.cookie = originalDocumentCookie;
  });

  it('should return the CSRF token if it exists in the cookie', () => {
    expect(csrfToken()).toEqual('abc123');
  });

  it('should return null if the CSRF token does not exist in the cookie', () => {
    // Simulate empty cookie
    document.cookie = '';
    expect(csrfToken()).toBeNull();
  });

  it('should return null if the CSRF token is not found in the cookie', () => {
    // Simulate cookie without XSRF-TOKEN
    document.cookie = 'other_cookie=def456';
    expect(csrfToken()).toBeNull();
  });

  it('should return the CSRF token even if there are other cookies in the string', () => {
    // Simulate cookie with multiple cookies
    document.cookie = 'XSRF-TOKEN=abc123; other_cookie=def456';
    expect(csrfToken()).toEqual('abc123');
  });

  it('should return the CSRF token if it has special characters', () => {
    // Simulate cookie with special characters in the token
    document.cookie = 'XSRF-TOKEN=ab!c_12*3; other_cookie=def456';
    expect(csrfToken()).toEqual('ab!c_12*3');
  });
});
