import { Component } from '@angular/core';
import { CatalogComponent } from './pages/catalog/catalog';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CatalogComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {}