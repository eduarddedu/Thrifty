import { CategoryData } from './categoryData';
import { LabelData } from './labelData';

export interface ExpenseData {
    id?: number;
    createdOn: string;
    amount: number;
    description: string;
    labels: LabelData[];
    category: CategoryData;
}
