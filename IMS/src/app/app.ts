import { Component, signal } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { Register } from '../components/register/register';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet,Register],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  title = "IMS";
  constructor(private router: Router) {}
}
