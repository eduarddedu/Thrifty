import { LocalDate, Expense } from '../model';

export class Utils {

    static deepFreeze(object: any) {
        try {
            const properties = Object.getOwnPropertyNames(object);
            for (const prop of properties) {
                const value = object[prop];
                const descriptor = Object.getOwnPropertyDescriptor(object, prop);
                if (typeof value === 'object' && descriptor.writable) {
                    Utils.deepFreeze(value);
                    object[prop] = value;
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

    static sumExpenses(expense: Expense[]) {
        return this.sum(expense.map(e => e.amount));
    }

    static sum(series: Array<number> = []): number {
        return series.reduce((accumulator: number, currentValue: number) => accumulator + currentValue, 0);
    }

    static getYearsSeries(expenses: Expense[]): number[] {
        const years: number[] = expenses.map(exp => exp.createdOn.year);
        const uniqueYears: number[] = Array.from(new Set(years));
        uniqueYears.sort((a, b) => a - b);
        return uniqueYears;
    }

    static getPercentageString(base: number, qty: number): string {
        if (base === 0) {
            return '0%';
        }
        return (qty * 100 / base).toFixed(2) + '%';
    }

}
