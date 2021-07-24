import { LabelData } from './labelData';
import { Category } from './category';
import { ExpenseGroup } from './expenseGroup';

export class Label extends ExpenseGroup implements LabelData {
    id: number;
    accountId: number;
    name: string;
    description: string;
    categories: Category[] = [];
    constructor(data: LabelData) {
        super();
        this.id = data.id;
        this.name = data.name;
        this.description = data.description;
        this.accountId = data.accountId;
    }

    hasCategory(categoryId: number): boolean {
        for (const category of this.categories) {
            if (category.id === categoryId) {
                return true;
            }
        }
        return false;
    }
}
