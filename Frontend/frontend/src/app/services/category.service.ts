import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CategoryNode {
  id: number;
  name: string;
  parentId: number | null;
  children: CategoryNode[];
}

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private apiUrl = '/api/public/categories/tree';

  constructor(private http: HttpClient) {}

  getCategories(): Observable<CategoryNode[]> {
    return this.http.get<CategoryNode[]>(this.apiUrl);
  }

}