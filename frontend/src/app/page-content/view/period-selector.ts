import { RefPeriod } from '../../model';

export class PeriodSelector {
    selectorOptions: { value: RefPeriod, selected: boolean }[] = [];

    setSelectOptions(yearsSeries: number[]) {
        this.selectorOptions = [];
        this.selectorOptions.push({
            value: 'All time',
            selected: false
        });
        const years = Array.from(yearsSeries).reverse();
        if (years.length > 0) {
            const maxYear = years[0];
            years.map(year => ({
                value: year,
                selected: (year === maxYear)
            })).forEach(option => this.selectorOptions.push(option));
        }
    }
}
