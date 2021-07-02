import { ExpenseData, LabelData, CategoryData } from '.';

export interface AccountData {
    id: number;
    name: string;
    currency: string;
    labels: LabelData[];
    categories: CategoryData[];
    expenses: ExpenseData[];
}
