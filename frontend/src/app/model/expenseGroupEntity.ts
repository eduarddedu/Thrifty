import { Expense } from './expense';
import { DateRange } from './dateRange';

export class ExpenseGroupEntity {
    expenses: Expense[] = [];
    mapYearBalance: Map<number, number> = new Map();
    balance = 0;

    get dateRange(): DateRange {
        const length = this.expenses.length;
        if (length > 0) {
            return {
                startDate: this.expenses[0].createdOn,
                endDate: this.expenses[length - 1].createdOn
            };
        }
    }

    get yearsSeries(): number[] {
        return Array.from(this.mapYearBalance.keys()).sort((a, b) => a - b);
    }
}
