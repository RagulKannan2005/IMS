import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-under-construction',
  imports: [CommonModule],
  template: `
    <div style="padding: 4rem 2rem; text-align: center; background: white; border-radius: 8px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); margin: 2rem;">
      <div style="font-size: 4rem; margin-bottom: 1.5rem;">🚧</div>
      <h2 style="font-size: 2.25rem; color: #1f2937; font-weight: 700; margin-bottom: 1rem;">Feature Under Construction</h2>
      <p style="color: #6b7280; font-size: 1.125rem; max-width: 500px; margin: 0 auto;">
        We are actively working on this page. This feature will be available in a future release of the Inventory Management System.
      </p>
    </div>
  `
})
export class UnderConstruction {}
