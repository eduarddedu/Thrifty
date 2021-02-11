import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CategoryData, LabelData, ExpenseData } from '../model';

@Injectable()
export class RestService {

    constructor(private http: HttpClient) { }

    public getExpenseViews(): Observable<ExpenseData[]> {
        return this.http.get<ExpenseData[]>('view/expenses');
    }

    public getExpenses(): Observable<ExpenseData[]> {
        return this.http.get<ExpenseData[]>('expenses');
    }

    public getCategories(): Observable<CategoryData[]> {
        return this.http.get<CategoryData[]>('categories');
    }

    public getLabels(): Observable<LabelData[]> {
        return this.http.get<LabelData[]>('labels');
    }

    public createCategory(category: CategoryData) {
        return this.http.post('categories', JSON.stringify(category));
    }

    public updateCategory(category: CategoryData) {
        return this.http.put(`categories`, JSON.stringify(category));
    }

    public deleteCategory(id: number): Observable<any> {
        return this.http.delete(`categories/${id}`);
    }

    public createExpense(expense: ExpenseData): Observable<any> {
        return this.http.post('expenses', JSON.stringify(expense));
    }

    public updateExpense(expense: ExpenseData): Observable<any> {
        return this.http.put(`expenses`, JSON.stringify(expense));
    }

    public deleteExpense(id: number): Observable<any> {
        return this.http.delete(`expenses/${id}`);
    }

    public createLabel(label: LabelData) {
        return this.http.post('labels', JSON.stringify(label));
    }

    public updateLabel(label: LabelData) {
        return this.http.put(`labels`, JSON.stringify(label));
    }

    public deleteLabel(id: number): Observable<any> {
        return this.http.delete(`labels/${id}`);
    }

}
