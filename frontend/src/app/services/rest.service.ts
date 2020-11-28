import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category, Label, Expense } from '../model';

@Injectable()
export class RestService {

    constructor(private http: HttpClient) { }

    public getExpenses(): Observable<Expense[]> {
        return this.http.get<Expense[]>('expenses');
    }

    public getCategories(): Observable<Category[]> {
        return this.http.get<Category[]>('categories');
    }

    public getLabels(): Observable<Label[]> {
        return this.http.get<Category[]>('labels');
    }

    public createCategory(category: Category) {
        return this.http.post('categories', JSON.stringify(category));
    }

    public updateCategory(category: Category) {
        return this.http.put(`categories`, JSON.stringify(category));
    }

    public deleteCategory(id: number): Observable<any> {
        return this.http.delete(`categories/${id}`);
    }

    public createExpense(expense: Expense): Observable<any> {console.log(expense);
        return this.http.post('expenses', JSON.stringify(expense));
    }

    public updateExpense(expense: Expense): Observable<any> {
        return this.http.put(`expenses`, JSON.stringify(expense));
    }

    public deleteExpense(id: number): Observable<any> {
        return this.http.delete(`expenses/${id}`);
    }

    public createLabel(label: Label) {
        return this.http.post('labels', JSON.stringify(label));
    }

    public updateLabel(label: Label) {
        return this.http.put(`labels`, JSON.stringify(label));
    }

    public deleteLabel(id: number): Observable<any> {
        return this.http.delete(`labels/${id}`);
    }

}
