import { LocalDate, Expense } from '../model';

export class Utils {

    static deepFreeze(object: any) {
        try {
            const objectValues = [];
            for (const property of Object.getOwnPropertyNames(object)) {
                const value = object[property];
                const descriptor = Object.getOwnPropertyDescriptor(object, property);
                if (typeof value === 'object' && descriptor.writable) {
                    objectValues.push(value);
                }
            }
            Object.freeze(object);
            for (const value of objectValues) {
                Utils.deepFreeze(value);
            }
        } catch (error) {
            console.log('An error occurred while deep freezing the object: ', error);
        }
    }

    static localDateToJsDate(localDate: LocalDate) {
        return new Date(localDate.year, localDate.month - 1, localDate.day);
    }

    static jsDateToLocalDate(date: Date): LocalDate {
        return new LocalDate(date.getFullYear(), date.getMonth() + 1, date.getDate());
    }

    static scrollPage() {
        document.getElementById('page').scrollTop = 0;
    }

    static sumExpenses(expense: Expense[]) {
        return this.sum(expense.map(e => e.cents));
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

    static percent(base: number, qty: number): string {
        if (typeof base !== 'number' || typeof qty !== 'number' || base === 0) {
            return '0%';
        }
        return (qty * 100 / base).toFixed(2) + '%';
    }

}
