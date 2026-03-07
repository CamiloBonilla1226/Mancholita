import { Component } from '@angular/core';
import { NavbarComponent } from './components/navbar/navbar';
import { CatalogComponent } from './pages/catalog/catalog';
import { CartPanelComponent } from './components/cart-panel/cart-panel';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, NavbarComponent, CatalogComponent, CartPanelComponent],
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class App {

  cartOpen = false;

  toggleCart() {
    this.cartOpen = !this.cartOpen;
  }

}