import { AfterViewInit, Component } from '@angular/core';
import { NavbarComponent } from './components/navbar/navbar';
import { CatalogComponent } from './pages/catalog/catalog';
import { CartPanelComponent } from './components/cart-panel/cart-panel';
import { CheckoutComponent } from './pages/checkout/checkout';
import { CommonModule } from '@angular/common';
import { AboutComponent } from './pages/about/about';
import { FooterComponent } from './components/footer/footer';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, NavbarComponent, CatalogComponent, CartPanelComponent, CheckoutComponent, AboutComponent, FooterComponent],
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class App implements AfterViewInit {

  cartOpen = false;
  searchText = '';
  showCheckout = false;
  selectedGender = 0;
  selectedCategory = 0;
  showAbout = true;
  productModalOpen = false;

  ngAfterViewInit(): void {
    this.scrollToTop('auto');
  }

  toggleCart() {
    this.cartOpen = !this.cartOpen;
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
    this.scrollToTop();
  }

  onSearchChanged(text: string) {
    this.searchText = text;

    if (this.showAbout && text.trim()) {
      this.showAbout = false;
      this.showCheckout = false;
      this.cartOpen = false;
      this.selectedGender = -1;
      this.selectedCategory = 0;
      this.scrollToTop();
    }
  }

  openCheckout() {
    localStorage.removeItem('checkoutData');
    this.showCheckout = true;
    this.cartOpen = false;
    this.scrollToTop();
  }

  onGenderChanged(genderId: number) {
    this.selectedGender = genderId;
    this.selectedCategory = 0;
    this.showAbout = false;
    this.showCheckout = false;
    this.cartOpen = false;
    this.scrollToTop();
  }

  onCategoryChanged(categoryId: number) {
    this.selectedCategory = categoryId;
    this.showAbout = false;
    this.showCheckout = false;
    this.cartOpen = false;
    this.scrollToTop();
  }

  goToAbout() {
    this.showAbout = true;
    this.showCheckout = false;
    this.cartOpen = false;
    this.selectedGender = 0;
    this.selectedCategory = 0;
    this.searchText = '';
    this.scrollToTop();
  }

  goToProductCatalog(product: any) {
    this.showAbout = false;
    this.showCheckout = false;
    this.cartOpen = false;

    this.selectedGender = product.genderId ?? 0;
    this.selectedCategory = 0;
    this.searchText = '';
    this.scrollToTop();
  }

  onProductModalChange(open: boolean) {
    this.productModalOpen = open;
  }

  private scrollToTop(behavior: ScrollBehavior = 'smooth') {
    setTimeout(() => {
      window.scrollTo({ top: 0, behavior });
    });
  }
}
