import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardService, StaffDashboardDto } from '../../../core/services/dashboard.service';
import { TaskService, DailyTask } from '../../../core/services/task.service';
import { StockMovementResponseDto } from '../../../shared/models/interfaces';

@Component({
  selector: 'app-staff-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './staff-dashboard.html'
})
export class StaffDashboard implements OnInit {
  dashboardData = signal<StaffDashboardDto | null>(null);

  stats = computed(() => {
    const data = this.dashboardData();
    if (!data) {
      return [
        { title: 'Stock In Today', value: '...', subtext: 'Loading...', icon: 'stock-in', color: '#10b981' },
        { title: 'Stock Out Today', value: '...', subtext: 'Loading...', icon: 'stock-out', color: '#fb7185' },
        { title: 'Movements Today', value: '...', subtext: 'Loading...', icon: 'stock', color: '#f59e0b' }
      ];
    }
    return [
      { title: 'Stock In Today', value: `${data.stockInToday} Items`, subtext: 'Inbound logs today', icon: 'stock-in', color: '#10b981' },
      { title: 'Stock Out Today', value: `${data.stockOutToday} Items`, subtext: 'Outbound logs today', icon: 'stock-out', color: '#fb7185' },
      { title: 'Movements Today', value: `${data.stockMovementsToday.length} Logs`, subtext: 'Total logged transactions', icon: 'stock', color: '#f59e0b' }
    ];
  });

  dailyTasks = signal<DailyTask[]>([]);

  movements = computed<StockMovementResponseDto[]>(() => {
    return this.dashboardData()?.stockMovementsToday || [];
  });

  constructor(
    private dashboardService: DashboardService,
    private taskService: TaskService
  ) {}

  ngOnInit(): void {
    this.dashboardService.getStaffDashboard().subscribe({
      next: (data) => {
        this.dashboardData.set(data);
      },
      error: (err) => {
        console.error('Failed to load staff dashboard stats', err);
      }
    });

    this.loadTasks();
  }

  loadTasks(): void {
    this.taskService.getAllTasks().subscribe({
      next: (tasks) => {
        this.dailyTasks.set(tasks);
      },
      error: (err) => {
        console.error('Failed to load daily tasks', err);
      }
    });
  }

  toggleTask(id: number): void {
    this.taskService.toggleTask(id).subscribe({
      next: (updatedTask) => {
        const tasks = this.dailyTasks().map(t => t.id === id ? updatedTask : t);
        this.dailyTasks.set(tasks);
      },
      error: (err) => {
        console.error('Failed to toggle task', err);
      }
    });
  }
}
