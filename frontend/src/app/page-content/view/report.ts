import { RefPeriod, Account, Category, Label, Expense } from '../../model';

export class Report {
    protected options: { value: RefPeriod, selected: boolean }[];
    protected entity: Account | Category | Label;
    protected refPeriod: RefPeriod;
    protected expenses: Expense[];

    setPeriodOptions() {
        this.options = [
            {
                value: 'All time',
                selected: false
            }
        ];
        const years = Array.from(this.entity.yearsSeries).reverse();
        if (years.length > 0) {
            const currYear = new Date().getFullYear();
            years.map(year => ({
                value: year,
                selected: (year === currYear)
            })).forEach(option => this.options.push(option));

            if (!this.options.find(o => o.selected)) {
                this.options[this.options.length - 1].selected = true;
            }
        } else {
            this.options[0].selected = true;
        }
        this.refPeriod = this.options.find(o => o.selected).value;
    }

    setExpenses() {
        if (this.refPeriod === 'All time') {
            this.expenses = this.entity.expenses;
        } else {
            this.expenses = this.entity.expenses.filter(e => e.createdOn.year === this.refPeriod);
        }
    }

}
