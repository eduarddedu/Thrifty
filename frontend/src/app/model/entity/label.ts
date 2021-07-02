import { LabelData } from './labelData';
import { Category } from '../entity/category';
import { ExpenseGroup } from './expenseGroup';

export class Label extends ExpenseGroup implements LabelData {
    id: number;
    name: string;
    categories: Category[] = [];
    constructor(data: LabelData) {
        super();
        this.id = data.id;
        this.name = data.name;
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
