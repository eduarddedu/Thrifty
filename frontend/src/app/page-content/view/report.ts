import { RefPeriod } from '../../model';

export class Report {
    protected options: { value: RefPeriod, selected: boolean }[];

    setPeriodOptions(yearsSeries: number[] = []) {
        this.options = [
            {
                value: 'All time',
                selected: false
            }
        ];
        const years = Array.from(yearsSeries).reverse();
        if (years.length > 0) {
            const currYear = new Date().getFullYear();
            years.map(year => ({
                value: year,
                selected: (year === currYear)
            })).forEach(option => this.options.push(option));

            if (this.options.filter(o => o.selected).length === 0) {
                this.options[this.options.length - 1].selected = true;
            }
        } else {
            this.options[0].selected = true;
        }
    }

}
