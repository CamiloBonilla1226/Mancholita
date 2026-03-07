import { Component, DoCheck, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartService } from '../../services/cart.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.scss']
})
export class NavbarComponent implements DoCheck {

  cartCount = 0;
  searchText = '';

  @Output() cartClicked = new EventEmitter<void>();
  @Output() searchChanged = new EventEmitter<string>();

  constructor(private cartService: CartService) {}

  ngDoCheck(): void {
    this.cartCount = this.cartService.getCount();
  }

  openCart() {
    this.cartClicked.emit();
  }

  onSearchChange() {
    this.searchChanged.emit(this.searchText);
  }

}