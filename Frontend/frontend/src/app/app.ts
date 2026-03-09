import { Component } from '@angular/core';
import { NavbarComponent } from './components/navbar/navbar';
import { CatalogComponent } from './pages/catalog/catalog';
import { CartPanelComponent } from './components/cart-panel/cart-panel';
import { CheckoutComponent } from './pages/checkout/checkout';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, NavbarComponent, CatalogComponent, CartPanelComponent, CheckoutComponent],
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class App {

  cartOpen = false;
  searchText = '';
  showCheckout = false;
  selectedGender = 0;
  selectedCategory = 0;

 

  toggleCart() {
    this.cartOpen = !this.cartOpen;
  }

  onSearchChanged(text: string) {
    this.searchText = text;
  }

  onGenderChanged(genderId: number) {
    this.selectedGender = genderId;
  }

  openCheckout() {
    this.showCheckout = true;
    this.cartOpen = false;
  }
  onCategoryChanged(categoryId: number) {
  this.selectedCategory = categoryId;
}
}