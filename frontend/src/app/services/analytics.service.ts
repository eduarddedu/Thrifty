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
            account.labels = labels;
            account.categories = this.buildEnrichedCategories(expenses, categories);
            account.dateRange = this.getDateRange(expenses);
            account.mapYearBalance = this.getMapYearBalance(expenses);
            account.balance = this.sumExpenses(expenses);
        } catch (error) {
            console.log('Error building account: ', error);
        }
        return account;
    }

    private buildEnrichedCategories(expenses: Expense[], categories: Category[]): Category[] {
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
            c.balance = this.sumExpenses(c.expenses);
            c.mapYearBalance = this.getMapYearBalance(c.expenses);
            c.labels = this.getUniqueLabels(c.expenses);
        });
        return enrichedCategories;
    }

    public sumExpenses(expenses: Expense[] = []): number {
        const rawSum = this.sum(expenses.map(e => e.amount));
        return Math.floor(rawSum * 100) / 100;
    }

    private sum(series: Array<number> = []): number {
        return series.reduce((accumulator: number, currentValue: number) => accumulator + currentValue, 0);
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

    private getMapYearBalance(expenses: Expense[]): { [key: number]: number } {
        const mapYearBalance = {};
        for (const expense of expenses) {
            let sum = mapYearBalance[expense.createdOn.year] || 0;
            sum += expense.amount;
            mapYearBalance[expense.createdOn.year] = sum;
        }
        return mapYearBalance;
    }

}
