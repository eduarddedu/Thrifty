import { LabelData } from './labelData';
import { Category } from './category';
import { ExpenseGroupEntity } from './expenseGroupEntity';

export class Label extends ExpenseGroupEntity implements LabelData {
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
