import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface DailyTask {
  id: number;
  text: string;
  done: boolean;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private readonly apiUrl = 'http://localhost:8082/api/v1/tasks';

  constructor(private http: HttpClient) {}

  getAllTasks(): Observable<DailyTask[]> {
    return this.http.get<DailyTask[]>(this.apiUrl);
  }

  createTask(task: { text: string; done: boolean }): Observable<DailyTask> {
    return this.http.post<DailyTask>(this.apiUrl, task);
  }

  toggleTask(id: number): Observable<DailyTask> {
    return this.http.put<DailyTask>(`${this.apiUrl}/${id}/toggle`, {});
  }
}
