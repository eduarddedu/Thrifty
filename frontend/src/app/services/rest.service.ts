import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject, forkJoin } from 'rxjs';
import { tap, switchMap } from 'rxjs/operators';
import { of } from 'rxjs/index';

import { Account, Category, Label, Expense } from '../model';
import { Utils } from '../util/utils';


@Injectable()
export class RestService {

    private account: Account;

    private subject: Subject<Account> = new Subject();

    public dataReady = this.subject.asObservable();

    constructor(private http: HttpClient) { }

    public getAccount(): Observable<Account> {
        if (this.account) {
            console.log('reading cached data');
            return of(this.account);
        } else {
            console.log('fetching data');
            return forkJoin(
                this.http.get<Expense[]>('expenses'),
                this.http.get<Label[]>('labels'),
                this.http.get<Category[]>('categories'))
            .pipe(
                switchMap(data => {
                    this.account = Utils.buildAccount(data[0], data[1], data[2]);
                    Utils.deepFreeze(this.account);
                    this.subject.next(this.account);
                    return of(this.account);
                }));
        }
    }

    public createCategory(category: Category) {
        return this.http.post('categories', JSON.stringify(category)).pipe(tap(() => this.cleanCache()));
    }

    public updateCategory(id: number, category: Category) {
        return this.http.put(`categories`, JSON.stringify(category)).pipe(tap(() => this.cleanCache()));
    }

    public deleteCategory(id: number): Observable<any> {
        return this.http.delete(`categories/${id}`).pipe(tap(() => this.cleanCache()));
    }

    public createExpense(expense: Expense): Observable<any> {
        return this.http.post('expenses', JSON.stringify(expense)).pipe(tap(() => this.cleanCache()));
    }

    public updateExpense(expense: Expense): Observable<any> {
        return this.http.put(`expenses`, JSON.stringify(expense))
        .pipe(tap(() => this.cleanCache()));
    }

    public deleteExpense(id: number): Observable<any> {
        return this.http.delete(`expenses/${id}`).pipe(tap(() => this.cleanCache()));
    }

    public createLabel(label: Label) {
        return this.http.post('labels', JSON.stringify(label)).pipe(tap(() => this.cleanCache()));
    }

    public updateLabel(id: number, label: Label) {
        return this.http.put(`labels`, JSON.stringify(label)).pipe(tap(() => this.cleanCache()));
    }

    public deleteLabel(id: number): Observable<any> {
        return this.http.delete(`labels/${id}`).pipe(tap(() => this.cleanCache()));
    }

    private cleanCache() {
        this.account = null;
        this.getAccount().subscribe(() => console.log('Reimported account after changes'));
    }

}
