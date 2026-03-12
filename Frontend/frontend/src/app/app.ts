import { Component } from '@angular/core';
import { NavbarComponent } from './components/navbar/navbar';
import { CatalogComponent } from './pages/catalog/catalog';
import { CartPanelComponent } from './components/cart-panel/cart-panel';
import { CheckoutComponent } from './pages/checkout/checkout';
import { CommonModule } from '@angular/common';
import { AboutComponent } from './pages/about/about';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, NavbarComponent, CatalogComponent, CartPanelComponent, CheckoutComponent, AboutComponent],
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class App {

  cartOpen = false;
  searchText = '';
  showCheckout = false;
  selectedGender = 0;
  selectedCategory = 0;
  showAbout = true;

  toggleCart() {
    this.cartOpen = !this.cartOpen;
  }
  goToAbout() {
  this.showAbout = true;
  this.showCheckout = false;
  this.cartOpen = false;
}

  closeCart() {
    this.cartOpen = false;
  }

  goToProductFromAbout(product: any) {
  this.showAbout = false;
  this.showCheckout = false;
  this.cartOpen = false;
  this.searchText = product.name;
  this.selectedGender = 0;
  this.selectedCategory = 0;
}

  onSearchChanged(text: string) {
    this.searchText = text;
  }

  onGenderChanged(genderId: number) {
  this.selectedGender = genderId;
  this.showAbout = false;
}

  onCategoryChanged(categoryId: number) {
    this.selectedCategory = categoryId;
  }

  openCheckout() {
    this.showCheckout = true;
    this.cartOpen = false;
  }

}