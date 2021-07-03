import { ExpenseGroup } from './expenseGroup';
import { Expense } from './expense';
import { Category } from './category';
import { Label } from './label';
import { ExpenseData } from './expenseData';
import { LabelData } from './labelData';
import { CategoryData } from './categoryData';
import { AccountData } from './accountData';

export class Account extends ExpenseGroup {
    id: number;
    categories: Category[];
    labels: Label[];
    private mapIdLabel: Map<number, Label> = new Map();
    private mapIdCategory: Map<number, Category> = new Map();

    constructor(data: AccountData) {
        super();
        this.id = data.id;
        this.buildAccount(data.expenses, data.categories, data.labels);
    }

    private buildAccount(expenseDatas: ExpenseData[], categoryDatas: CategoryData[], labelDatas: LabelData[]) {
        this.mapCategories(categoryDatas);
        this.mapLabels(labelDatas);
        this.labels = Array.from(this.mapIdLabel.values());
        this.categories = Array.from(this.mapIdCategory.values());

        for (const eData of expenseDatas) {
            const expense: Expense = new Expense(Object.assign({accountId: this.id}, eData));
            const category: Category = this.mapIdCategory.get(eData.category.id);
            const labels: Label[] = eData.labels.map(labelData => this.mapIdLabel.get(labelData.id));

            this.addExpenseToEntity(this, expense);
            this.addExpenseToEntity(category, expense);

            for (const label of labels) {
                this.addExpenseToEntity(label, expense);
                if (!label.hasCategory(category.id)) {
                    label.categories.push(category);
                }
                if (!category.hasLabel(label.id)) {
                    category.labels.push(label);
                }
            }
            expense.category = category;
            expense.labels = labels;
        }
        this.sortExpensesByDate();
    }

    private sortExpensesByDate() {
        this.expenses = this.expenses.sort((a, b) => {
            const yearDiff = a.createdOn.year - b.createdOn.year;
            const monthDiff = a.createdOn.month - b.createdOn.month;
            const dayDiff = a.createdOn.day - b.createdOn.day;
            const result = yearDiff !== 0 ? yearDiff : monthDiff !== 0 ? monthDiff : dayDiff;
            return result * -1;
        });
    }

    private mapCategories(categories: CategoryData[]) {
        categories.forEach(d => this.mapIdCategory.set(d.id, new Category(Object.assign({accountId: this.id}, d))));
    }

    private mapLabels(labels: LabelData[]) {
        labels.forEach(d => this.mapIdLabel.set(d.id, new Label(Object.assign({accountId: this.id}, d))));
    }

    private addExpenseToEntity(entity: ExpenseGroup, expense: Expense) {
        entity.expenses.push(expense);
        entity.balance += expense.cents;
        const year = expense.createdOn.year;
        const yearBalance = (entity.mapYearBalance.get(year) || 0) + expense.cents;
        entity.mapYearBalance.set(year, yearBalance);
    }

    hasCategory(categoryId: number): boolean {
        for (const category of this.categories) {
            if (category.id === categoryId) {
                return true;
            }
        }
        return false;
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
