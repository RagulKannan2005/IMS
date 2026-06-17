import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-admindashboard',
  imports: [CommonModule, RouterLink, RouterOutlet],
  templateUrl: './admindashboard.html',
  styleUrl: './admindashboard.css',
})
export class Admindashboard {
  userMenu = false;
  inventoryMenu = false;
  orderMenu = false;

  toggleUsers() {
    this.userMenu = !this.userMenu;
  }

  toggleInventory() {
    this.inventoryMenu = !this.inventoryMenu;
  }

  toggleOrders() {
    this.orderMenu = !this.orderMenu;
  }
}
