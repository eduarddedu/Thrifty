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
        const map: Map<number, Category> = new Map();
        for (const category of categories) {
            map.set(category.id, category);
            category.expenses = [];
        }
        for (const expense of expenses) {
            map.get(expense.category.id).expenses.push(expense);
        }
        const enrichedCategories: Category[] = Array.from(map.values());
        enrichedCategories.forEach(c => {
            c.balance = Utils.addExpenseAmounts(c.expenses);
            c.labels = this.getUniqueLabels(c.expenses);
        });
        return enrichedCategories;
    }

    private getEnrichedLabels(expenses: Expense[], labels: Label[]): Label[] {
        const map: Map<number, Label> = new Map();
        for (const label of labels) {
            map.set(label.id, label);
            label.expenses = [];
            label.categories = [];
        }
        for (const expense of expenses) {
            for (const label of expense.labels) {
                map.get(label.id).expenses.push(expense);
                map.get(label.id).categories.push(expense.category);
            }
        }
        const enrichedLabels: Label[] = Array.from(map.values());
        enrichedLabels.forEach(l => {
            l.balance = Utils.addExpenseAmounts(l.expenses);
            l.categories = Array.from(new Set(l.categories));
        });
        return enrichedLabels;
    }

    private getUniqueLabels(expenses: Expense[] = []): Label[] {
        const mapLabelIdLabel: Map<number, Label> = new Map();
        for (const expense of expenses) {
            for (const label of expense.labels) {
                mapLabelIdLabel.set(label.id, label);
            }
        }
        return Array.from(mapLabelIdLabel.values());
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
