import { Expense } from './expense';
import { DateRange } from '../dateRange';

export class ExpenseGroup {
    expenses: Expense[] = [];
    mapYearBalance: Map<number, number> = new Map();
    balance = 0;

    get dateRange(): DateRange {
        const length = this.expenses.length;
        if (length > 0) {
            return {
                startDate: this.expenses[length - 1].createdOn,
                endDate: this.expenses[0].createdOn
            };
        }
    }

    get yearsSeries(): number[] {
        return Array.from(this.mapYearBalance.keys()).sort((a, b) => a - b);
    }
}
