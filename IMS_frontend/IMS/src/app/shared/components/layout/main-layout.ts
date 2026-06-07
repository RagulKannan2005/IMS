import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { Navbar } from './navbar/navbar';
import { Sidebar } from './sidebar/sidebar';
import { Footer } from './footer/footer';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, Navbar, Sidebar, Footer],
  template: `
    <div class="layout-wrapper">
      <!-- Left sidebar menu -->
      <app-sidebar></app-sidebar>
      
      <!-- Right panel main container -->
      <div class="main-container">
        <!-- Header navbar -->
        <app-navbar></app-navbar>
        
        <!-- Routed page content -->
        <main class="content-area">
          <router-outlet></router-outlet>
        </main>
        
        <!-- Footer info -->
        <app-footer></app-footer>
      </div>
    </div>
  `
})
export class MainLayout {}
