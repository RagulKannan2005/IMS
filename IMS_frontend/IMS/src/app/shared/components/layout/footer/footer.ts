import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  standalone: true,
  template: `
    <footer class="footer">
      <div>© 2026 Inventory Management System. All rights reserved.</div>
      <div>
        Version 1.0.0 (SaaS Edition) | 
        System Status: <span class="status-pill success" style="padding: 2px 8px; font-size: 9px; margin-left: 4px;">Online</span>
      </div>
    </footer>
  `
})
export class Footer {}
