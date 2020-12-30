import { RefPeriod } from '../../model';

export class Timespan {
    options: { value: RefPeriod, selected: boolean }[] = [
        {
            value: 'All time',
            selected: false
        }
    ];

    setOptions(yearsSeries: number[]) {
        const years = Array.from(yearsSeries).reverse();
        if (years.length > 0) {
            const currYear = new Date().getFullYear();
            years.map(year => ({
                value: year,
                selected: (year === currYear)
            })).forEach(option => this.options.push(option));
        } else {
            this.options[0].selected = true;
        }
    }
}
