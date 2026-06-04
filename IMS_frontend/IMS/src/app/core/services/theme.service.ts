import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  private readonly THEME_KEY = 'ims-theme';
  theme = signal<'dark' | 'light'>('dark');

  constructor() {
    // Check local storage or system preference
    if (typeof window !== 'undefined') {
      const savedTheme = localStorage.getItem(this.THEME_KEY) as 'dark' | 'light' | null;
      const systemPrefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
      
      const initialTheme = savedTheme || (systemPrefersDark ? 'dark' : 'light');
      this.setTheme(initialTheme);
    }
  }

  toggleTheme(): void {
    const nextTheme = this.theme() === 'dark' ? 'light' : 'dark';
    this.setTheme(nextTheme);
  }

  private setTheme(newTheme: 'dark' | 'light'): void {
    this.theme.set(newTheme);
    if (typeof window !== 'undefined') {
      localStorage.setItem(this.THEME_KEY, newTheme);
      document.documentElement.setAttribute('data-theme', newTheme);
    }
  }
}
