import { ExpenseData } from './expenseData';
import { LocalDate } from './localDate';
import { Label } from './label';
import { Category } from './category';

export class Expense {
    id: number;
    createdOn: LocalDate;
    description: string;
    cents: number;
    labels: Label[] = [];
    category: Category;
    constructor(data: ExpenseData) {
        this.id = data.id;
        this.createdOn = LocalDate.parse(data.createdOn);
        this.description = data.description;
        this.cents = data.amount * 100;
    }
}
