import { Chart } from 'angular-highcharts';

import { Account, Expense, Category, Label } from '../model';
import { Utils } from '../util/utils';

const MONTHS = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

const dataSeries = function (name: string, values: number[], color: string = '#555') {
    return [{
        name: name,
        data: values.map(v => Math.abs(v)),
        color: color
    }];
};

const getPieChart = function (series: any) {
    const options: any = {
        chart: {
            type: 'pie'
        },
        title: {
            text: ''
        },
        credits: {
            enabled: false
        },
        series: series,
        tooltip: {
            useHTML: true,
            headerFormat: '<small>{point.key}</small><table>',
            pointFormat: '<tr><td>&bull; Total: </td><td style="text-align:right"><b>{point.y}</b> lei</td></tr>' +
                '<tr><td>&bull; Percentage: </td><td style="text-align:right">{point.percentage:.2f}%</td></tr>',
            footerFormat: '</table>',
            valueDecimals: 2
        }
    };
    return new Chart(options);
};

const getColumnChart = function (categories: string[], series: { name: string, data: number[], color?: string }[], yAxisTitle: string) {
    return new Chart({
        chart: {
            type: 'column'
        },
        title: {
            text: ''
        },
        credits: {
            enabled: false
        },
        xAxis: {
            categories: categories,
            crosshair: true
        },
        yAxis: {
            min: 0,
            title: {
                text: yAxisTitle
            }
        },
        series: series,
        tooltip: {
            useHTML: true,
            headerFormat: '<small>{point.key}</small><table>',
            pointFormat: '<tr><td>&bull; Total:</td><td style="text-align:right"><b>{point.y}</b> lei</td></tr>',
            footerFormat: '</table>',
            valueDecimals: 2
        }
    });
};

const getAccountSpendingByCategoryDataPoints = function (account: Account, year?: number, onClick?: Function) {
    return account.categories
        .filter(category => !year || category.mapYearBalance.get(year))
        .map(category => {
            const balance = Math.abs(year ? category.mapYearBalance.get(year) : category.balance) / 100;
            const callback = onClick ? onClick.bind(onClick, 'category', category.id) : null;
            return {
                name: category.name,
                y: balance,
                events: {
                    click: callback
                }
            };
        });
};

const getCategorySpendingByLabelDataPoints = function (category: Category, year?: number, onClick?: Function) {
    const map: Map<string, object> = new Map();
    const expensesInPeriod = category.expenses.filter(e => (!year || e.createdOn.year === year));
    for (const e of expensesInPeriod) {
        const key = e.labels.length === 0 ? 'Unlabeled' :
            e.labels.map(l => l.name).sort((a, b) => a <= b ? -1 : 1).join('-');
        const dataPoint: any = map.get(key) || {
            name: key,
            y: 0,
            events: (e.labels.length === 0 ? null : {
                click: onClick.bind(onClick, 'label', e.labels[0].id)
            })
        };
        dataPoint.y += Math.abs(e.cents / 100);
        map.set(key, dataPoint);
    }
    const points: any = Array.from(map.values());
    return points.filter(point => point.y !== 0);
};

const getLabelSpendingByCategoryDataPoints = function (label: Label, year?: number, onClick?: Function): { name: string, y: number }[] {
    const points = label.categories.map(c => {
        const expenses: Expense[] = label.expenses
            .filter(e => e.category.id === c.id && (!year || e.createdOn.year === year));
        const callback = onClick ? onClick.bind(onClick, 'category', c.id) : null;
        return {
            name: c.name,
            y: Math.abs(Utils.sumExpenses(expenses)) / 100,
            events: {
                click: callback
            }
        };
    });
    return points;
};

const getSpendingPerYearColumnChart = function (expenses: Expense[]) {
    const yearTotals = [];
    const years: number[] = Utils.getYearsSeries(expenses);
    years.forEach(year => {
        const yearTotal = Utils.sumExpenses(expenses.filter(e => e.createdOn.year === year)) / 100;
        yearTotals.push(yearTotal);
    });
    const yearsStr: string[] = years.map((year: number) => `${year}`);
    const series = dataSeries('Year', yearTotals);
    return getColumnChart(yearsStr, series, 'Total spending');
};

const getSpendingPerMonthColumnChart = function (expenses: Expense[], year: number): Chart {
    const monthTotals = [];
    for (let m = 1; m < 13; m++) {
        const monthTotal = Utils.sumExpenses(expenses.filter(e => e.createdOn.year === year && e.createdOn.month === m)) / 100;
        monthTotals.push(monthTotal);
    }
    const series = dataSeries('Month', monthTotals);
    return getColumnChart(MONTHS, series, 'Total spending');
};

export const Charts = {

    getAccountSpendingPerYearColumnChart: function (account: Account) {
        return getSpendingPerYearColumnChart(account.expenses);
    },

    getAccountSpendingPerMonthColumnChart: function (account: Account, year: number) {
        return getSpendingPerMonthColumnChart(account.expenses, year);
    },

    getCategorySpendingPerYearColumnChart: function (category: Category) {
        return getSpendingPerYearColumnChart(category.expenses);
    },

    getCategorySpendingPerMonthColumnChart: function (category: Category, year: number) {
        return getSpendingPerMonthColumnChart(category.expenses, year);
    },

    getLabelSpendingPerYearColumnChart(label: Label) {
        return getSpendingPerYearColumnChart(label.expenses);
    },

    getLabelSpendingPerMonthColumnChart: function (label: Label, year: number) {
        return getSpendingPerMonthColumnChart(label.expenses, year);
    },

    getAccountSpendingPerCategoryPieChart(account: Account, year?: number, onClick?: Function) {
        const series = [{
            name: 'Total',
            data: getAccountSpendingByCategoryDataPoints(account, year, onClick),
            cursor: 'pointer'
        }];
        return getPieChart(series);
    },

    getCategorySpendingPerLabelPieChart(category: Category, year?: number, onClick?: Function) {
        const points = getCategorySpendingByLabelDataPoints(category, year, onClick);
        const series = [{
            name: 'Total',
            data: points,
            cursor: 'pointer'
        }];
        return getPieChart(series);
    },

    getLabelSpendingPerCategoryPieChart(label: Label, year?: number, onClick?: Function) {
        const points = getLabelSpendingByCategoryDataPoints(label, year, onClick);
        const series = [{
            name: 'Total',
            data: points,
            cursor: 'pointer'
        }];
        return getPieChart(series);
    }

};
