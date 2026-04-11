export function isTokenExpired(token: string): boolean {
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const expirationMs = payload.exp * 1000;
        return Date.now() >= expirationMs;
    } catch {
        return true;
    }
}
