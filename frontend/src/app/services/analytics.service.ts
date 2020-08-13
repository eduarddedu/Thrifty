import { Injectable } from '@angular/core';
import { Observable, Subject, forkJoin } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { of } from 'rxjs/index';

import { RestService } from '../services/rest.service';
import { Account, Expense, Category, Label, DateRange } from '../model';
import { Utils } from '../util/utils';

@Injectable()
export class AnalyticsService {
    private account: Account;

    private subject: Subject<Account> = new Subject();

    public dataReady = this.subject.asObservable();

    public constructor(private rest: RestService) { }

    public loadAccount(): Observable<Account> {
        if (this.account) {
            return of(this.account);
        } else {
            return forkJoin(this.rest.getExpenses(), this.rest.getCategories(), this.rest.getLabels())
                .pipe(
                    switchMap(data => {
                        this.account = this.buildAccount(data[0], data[1], data[2]);
                        Utils.deepFreeze(this.account);
                        this.subject.next(this.account);
                        return of(this.account);
                    }));
        }
    }

    public reload() {
        this.account = null;
        this.loadAccount().subscribe(() => console.log('Reload account...'));
    }

    private buildAccount(expenses: Expense[], categories: Category[], labels: Label[]): Account {
        const account: Account = {};
        try {
            account.expenses = expenses;
            account.labels = this.getEnrichedLabels(expenses, labels);
            account.categories = this.getEnrichedCategories(expenses, categories);
            account.dateRange = this.getDateRange(expenses);
            account.balance = Utils.addExpenseAmounts(expenses);
        } catch (error) {
            console.log('Error building account: ', error);
        }
        return account;
    }

    private getEnrichedCategories(expenses: Expense[], categories: Category[]): Category[] {
        const mapRichCategories: Map<number, Category> = new Map();
        categories.forEach(c => {
            mapRichCategories.set(c.id, Object.assign({ expenses: [] }, c));
        });
        expenses.forEach(expense => {
            mapRichCategories.get(expense.category.id).expenses.push(expense);
        });
        const richCategories: Category[] = Array.from(mapRichCategories.values());
        richCategories.forEach(c => {
            c.balance = Utils.addExpenseAmounts(c.expenses);
            c.labels = this.pickLabels(c.expenses);
        });
        return richCategories;
    }

    private getEnrichedLabels(expenses: Expense[], labels: Label[]): Label[] {
        const mapRichLabels = new Map<number, Label>();
        labels.forEach(lb => {
            mapRichLabels.set(lb.id, Object.assign({ expenses: [] }, lb));
        });
        for (const expense of expenses) {
            for (const label of expense.labels) {
                mapRichLabels.get(label.id).expenses.push(expense);
            }
        }
        const richLabels = Array.from(mapRichLabels.values());
        richLabels.forEach(lb => {
            lb.balance = Utils.addExpenseAmounts(lb.expenses);
            lb.categories = this.pickCategories(lb.expenses);
        });
        return richLabels;
    }

    private pickCategories(expenses: Expense[]): Category[] {
        const map: Map<number, Category> = new Map();
        for (const expense of expenses) {
            map.set(expense.category.id, expense.category);
        }
        return Array.from(map.values());
    }

    private pickLabels(expenses: Expense[] = []): Label[] {
        const map: Map<number, Label> = new Map();
        for (const expense of expenses) {
            for (const label of expense.labels) {
                map.set(label.id, label);
            }
        }
        return Array.from(map.values());
    }
    private getDateRange(expenses: Expense[]): DateRange {
        const dateRange = <DateRange>{};
        if (expenses.length > 1) {
            dateRange.startDate = expenses[expenses.length - 1].createdOn;
            dateRange.endDate = expenses[0].createdOn;
        } else if (expenses.length === 1) {
            dateRange.startDate = dateRange.endDate = expenses[0].createdOn;
        }
        return dateRange;
    }

}
