import { Component, signal, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../../core/services/auth.service';
import { ThemeService } from '../../../../core/services/theme.service';
import { NotificationService, NotificationResponseDto } from '../../../../core/services/notification.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './navbar.html'
})
export class Navbar implements OnInit {
  showNotifications = signal(false);
  showProfileMenu = signal(false);

  notifications = signal<NotificationResponseDto[]>([]);
  unreadCount = signal<number>(0);



  constructor(
    public authService: AuthService,
    public themeService: ThemeService,
    private notificationService: NotificationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadNotifications();
    
    // Periodically check notifications (every 30 seconds)
    setInterval(() => {
      this.loadNotifications();
    }, 30000);
  }

  loadNotifications(): void {
    if (this.authService.isAuthenticated()) {
      this.notificationService.getNotifications().subscribe({
        next: (list) => {
          this.notifications.set(list);
          this.unreadCount.set(list.filter(n => !n.isRead).length);
        },
        error: (err) => {
          console.error('Failed to load notifications', err);
        }
      });
    }
  }

  toggleNotifications(): void {
    this.showNotifications.update(val => !val);
    this.showProfileMenu.set(false);
    if (this.showNotifications()) {
      this.loadNotifications();
    }
  }

  toggleProfileMenu(): void {
    this.showProfileMenu.update(val => !val);
    this.showNotifications.set(false);
  }

  markAsRead(id: number): void {
    this.notificationService.markAsRead(id).subscribe({
      next: () => {
        this.loadNotifications();
      }
    });
  }

  markAllAsRead(): void {
    this.notificationService.markAllAsRead().subscribe({
      next: () => {
        this.loadNotifications();
      }
    });
  }



  logout(): void {
    this.authService.logout();
    this.showProfileMenu.set(false);
    this.router.navigate(['/login']);
  }
}
