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

    public dataChanged = this.subject.asObservable();

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
        const account: Account = <Account>{};
        try {
            expenses.forEach(e => e.amount *= 100);
            account.expenses = expenses;
            account.labels = this.getEnrichedLabels(expenses, labels);
            account.categories = this.getEnrichedCategories(expenses, categories, account.labels);
            account.dateRange = this.getDateRange(expenses);
            account.balance = Utils.sumExpenses(expenses);
            account.mapYearBalance = this.getAccountMapYearBalance(expenses);
            account.yearsSeries = this.getYearsSeries(account.dateRange);
        } catch (error) {
            console.log('Error building account: ', error);
        }
        return account;
    }

    private getYearsSeries(dateRange: DateRange): number[] {
        const years = [];
        let startYear = dateRange.startDate.year;
        const endYear = dateRange.endDate.year;
        while (startYear <= endYear) {
            years.push(startYear++);
        }
        return years;
    }

    private getAccountMapYearBalance(expenses: Expense[]): Map<number, number> {
        const mapYearBalance = new Map();
        expenses.forEach(ex => {
            let yearBalance = mapYearBalance.get(ex.createdOn.year) || 0;
            yearBalance += ex.amount;
            mapYearBalance.set(ex.createdOn.year, yearBalance);
        });
        return mapYearBalance;
    }

    private getEnrichedCategories(expenses: Expense[], categories: Category[], richLabels: Label[]): Category[] {
        const mapIdCategory: Map<number, Category> = new Map();
        categories.forEach(c => {
            const richCategory = Object.assign({ expenses: [], balance: 0, mapYearBalance: new Map() }, c);
            mapIdCategory.set(c.id, richCategory);
        });
        expenses.forEach(e => {
            const category: Category = mapIdCategory.get(e.category.id);
            category.expenses.push(e);
            category.balance += e.amount;
            const yearBalance = (category.mapYearBalance.get(e.createdOn.year) || 0) + e.amount;
            category.mapYearBalance.set(e.createdOn.year, yearBalance);
        });
        const richCategories: Category[] = Array.from(mapIdCategory.values());
        richCategories.forEach(c => {
            c.labels = this.pickLabels(c.expenses, richLabels);
            c.yearsSeries = Array.from(c.mapYearBalance.keys()).sort((a, b) => a - b);
        });
        return richCategories;
    }

    private getEnrichedLabels(expenses: Expense[], labels: Label[]): Label[] {
        const mapIdLabel = new Map<number, Label>();
        labels.forEach(label => {
            const richLabel = Object.assign({ expenses: [], mapYearBalance: new Map(), balance: 0 }, label);
            mapIdLabel.set(label.id, richLabel);
        });
        for (const expense of expenses) {
            for (const label of expense.labels) {
                const richLabel = mapIdLabel.get(label.id);
                richLabel.expenses.push(expense);
                richLabel.balance += expense.amount;
                const yearBalance = (richLabel.mapYearBalance.get(expense.createdOn.year) || 0) + expense.amount;
                richLabel.mapYearBalance.set(expense.createdOn.year, yearBalance);
            }
        }
        const richLabels = Array.from(mapIdLabel.values());
        richLabels.forEach(label => {
            label.categories = this.pickCategories(label.expenses);
            label.yearsSeries = Array.from(label.mapYearBalance.keys()).sort((a, b) => a - b);
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

    private pickLabels(expenses: Expense[] = [], richLabels: Label[]): Label[] {
        const map: Map<number, Label> = new Map();
        for (const expense of expenses) {
            for (const label of expense.labels) {
                map.set(label.id, richLabels.find(rl => rl.id === label.id));
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
