import { HttpClient } from '@angular/common/http';
import { Injectable, signal, computed } from '@angular/core';

export interface UserSession {
  username: string;
  email: string;
  role: 'ADMIN' | 'MANAGER' | 'STAFF' | 'SUPPLIER';
  firstName: string;
  lastName: string;
  token?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly SESSION_KEY = 'ims-session';

  private  apiUrl= "http://localhost:8082/api/v1/auth";

  
  // Current session signal
  currentUser = signal<UserSession | null>(null);

  // Computed properties
  isAuthenticated = computed(() => this.currentUser() !== null);
  userRole = computed(() => this.currentUser()?.role || null);

  constructor(private http: HttpClient) {
    this.loadSession();
  }

  register(data:any){
    return this.http.post(
      `${this.apiUrl}/register`,
      data
    );
  }


  // Load session from local storage or set mock session if empty
  private loadSession(): void {
    if (typeof window !== 'undefined') {
      const savedSession = localStorage.getItem(this.SESSION_KEY);
      if (savedSession) {
        try {
          this.currentUser.set(JSON.parse(savedSession));
          return;
        } catch (e) {
          console.error('Error parsing session from local storage', e);
        }
      }
    }
  }

  login(session: UserSession): void {
    this.currentUser.set(session);
    if (typeof window !== 'undefined') {
      localStorage.setItem(this.SESSION_KEY, JSON.stringify(session));
    }
  }
  loginuser(data:any){
    return this.http.post(
      `${this.apiUrl}/login`,
      data
    );
  }

  logout(): void {
    this.currentUser.set(null);
    if (typeof window !== 'undefined') {
      localStorage.removeItem(this.SESSION_KEY);
    }
  }

  // Mock function to switch roles instantly for testing/demo purposes
  switchMockRole(role: 'ADMIN' | 'MANAGER' | 'STAFF' | 'SUPPLIER'): void {
    const current = this.currentUser();
    const updated: UserSession = { 
      username: current?.username || `${role.toLowerCase()}_test`,
      email: current?.email || `${role.toLowerCase()}@ims.com`,
      role,
      firstName: this.getMockNameForRole(role).first,
      lastName: this.getMockNameForRole(role).last
    };
    this.login(updated);
  }

  private getMockNameForRole(role: string): { first: string; last: string } {
    switch (role) {
      case 'ADMIN': return { first: 'Alex', last: 'Admin' };
      case 'MANAGER': return { first: 'Marc', last: 'Manager' };
      case 'STAFF': return { first: 'Sam', last: 'Staff' };
      case 'SUPPLIER': return { first: 'Steve', last: 'Supplier' };
      default: return { first: 'User', last: 'Test' };
    }
  }

  hasRole(roles: string[]): boolean {
    const currentRole = this.userRole();
    return currentRole ? roles.includes(currentRole) : false;
  }
}
