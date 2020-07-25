import { LocalDate, Account, Expense, Category, Label, DateRange } from '../model';

export class Utils {

    static deepFreeze(object): object {
        try {
            const propNames = Object.getOwnPropertyNames(object);
            for (const name of propNames) {
                const value = object[name];
                if (typeof value === 'object' && Object.getOwnPropertyDescriptor(object, name).writable) {
                    Utils.deepFreeze(value);
                    object[name] = value;
                }
            }
            return Object.freeze(object);
        } catch (error) {
            console.log('An error occurred while deep freezing the object: ', error);
        }
    }

    static localDateToJsDate(localDate: LocalDate) {
        return new Date(localDate.year, localDate.month - 1, localDate.day);
    }

    static jsDateToLocalDate(date: Date): LocalDate {
        return {
            year: date.getFullYear(),
            month: date.getMonth() + 1,
            day: date.getDate()
        };
    }

    static localDateToIsoDate(date: LocalDate) {
        function prependZero(x: number): string {
            let prefix = x < 10 ? '0' : '';
            return prefix += x;
        }
        return [date.year, prependZero(date.month), prependZero(date.day)].join('-');
    }

    static scrollPage() {
        document.getElementById('page').scrollTop = 0;
    }

    static sumExpenses(expenses: Expense[] = []): number {
        const rawSum = Utils.sum(expenses.map(e => e.amount));
        return Math.floor(rawSum * 100) / 100;
    }

    static buildAccount(expenses: Expense[], labels: Label[], categories: Category[]): Account {
        const account: Account = {};
        try {
            const expensesWithoutCategory = [];
            const mapLabelIdLabel: Map<number, Label> = new Map();
            labels.forEach(label => mapLabelIdLabel.set(label.id, label));
            for (const expense of expenses) {
                if (!expense.category) {
                    expensesWithoutCategory.push(expense);
                }
            }
            account.expenses = expenses;
            account.labels = Array.from(mapLabelIdLabel.values());
            account.categories = Utils.enrichedCategories(expenses, categories);
            if (expensesWithoutCategory.length > 0) {
                account.other = Utils.createUncategorized(expensesWithoutCategory);
            }
            account.dateRange = Utils.getDateRange(expenses);
            account.mapYearBalance = Utils.computeMapYearBalance(expenses);
            account.balance = Utils.sumExpenses(expenses);
        } catch (error) {
            console.log('Error building account: ', error);
        }
        return account;
    }

    private static sum(values: Array<number> = []): number {
        return values.reduce((accumulator: number, currentValue: number) => accumulator + currentValue, 0);
    }

    private static enrichedCategories(expenses: Expense[], categories: Category[]): Category[] {
        const mapCatIdCategory: Map<number, Category> = new Map();
        categories.forEach(category => {
            category.expenses = category.expenses || [];
            mapCatIdCategory.set(category.id, category);
        });
        for (const expense of expenses) {
                const category = expense.category;
                const inMapCategory = mapCatIdCategory.get(category.id);
                mapCatIdCategory.set(category.id, inMapCategory);
                inMapCategory.expenses = inMapCategory.expenses || [];
                inMapCategory.expenses.push(expense);
        }
        const enrichedCategories: Category[] = Array.from(mapCatIdCategory.values());
        enrichedCategories.forEach(c => {
            c.balance = Utils.sumExpenses(c.expenses);
            c.mapYearBalance = Utils.computeMapYearBalance(c.expenses);
            c.labels = Utils.getUniqueLabels(c.expenses);
        });
        return enrichedCategories;
    }

    private static getUniqueLabels(expenses: Expense[] = []): Label[] {
        const mapLabelIdLabel: Map<number, Label> = new Map();
        for (const expense of expenses) {
            for (const label of expense.labels) {
                mapLabelIdLabel.set(label.id, label);
            }
        }
        return Array.from(mapLabelIdLabel.values());
    }
    private static getDateRange(expenses: Expense[]): DateRange {
        const dateRange = <DateRange>{};
        if (expenses.length > 1) {
            dateRange.startDate = expenses[expenses.length - 1].createdOn;
            dateRange.endDate = expenses[0].createdOn;
        } else if (expenses.length === 1) {
            dateRange.startDate = dateRange.endDate = expenses[0].createdOn;
        }
        return dateRange;
    }

    private static computeMapYearBalance(expenses: Expense[]): { [key: number]: number } {
        const mapYearBalance = {};
        for (const expense of expenses) {
            let sum = mapYearBalance[expense.createdOn.year] || 0;
            sum += expense.amount;
            mapYearBalance[expense.createdOn.year] = sum;
        }
        return mapYearBalance;
    }

    private static createUncategorized(expenses: Expense[]): Category {
        return Object.assign(<Category>{
            id: -1,
            name: 'Other',
            description: 'Expenses not linked to a category',
            expenses: expenses,
            balance: Utils.sum(expenses.map(e => e.amount)),
            labels: []
        });
    }

}
