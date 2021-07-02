import { ExpenseGroup } from './expenseGroup';
import { Label } from './label';
import { CategoryData } from './categoryData';

export class Category extends ExpenseGroup {
    id: number;
    name: string;
    description: string;
    labels: Label[] = [];
    constructor(data: CategoryData) {
        super();
        this.id = data.id;
        this.name = data.name;
        this.description = data.description;
    }

    hasLabel(labelId: number): boolean {
        for (const label of this.labels) {
            if (label.id === labelId) {
                return true;
            }
        }
        return false;
    }
}
