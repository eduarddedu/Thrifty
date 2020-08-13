import { Chart } from 'angular-highcharts';

import { Account, Expense, Category, Label } from '../model';
import { Utils } from '../util/utils';
import { Router } from '@angular/router';

const MONTHS = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

const dataSeries = function (name: string, values: number[], color: string) {
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
            valuePrefix: '-'
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
            valuePrefix: '-'
        }
    });
};

const getAccountSpendingByCategoryDataPoints = function (categories: Category[], router: Router) {
    const navigate = function (url) {
        router.navigate(url);
    };
    return categories.map(category => ({
        name: category.name,
        y: Math.abs(category.balance),
        events: {
            click: navigate.bind(this, ['category/' + category.id])
        }
    }));
};

const getCategorySpendingByLabelDataPoints = function (category: Category, router: Router): { name: string, y: number }[] {
    const navigate = function (url) {
        router.navigate(url);
    };
    const getTotalByLabel = function (expenses: Expense[], label: Label): number {
        return Utils.addExpenseAmounts(expenses.filter(expense => hasLabel(expense, label)));
    };
    const points = category.labels.map((label: Label) => ({
        name: label.name,
        y: Math.abs(getTotalByLabel(category.expenses, label)),
        events: {
            click: navigate.bind(this, ['label/' + label.id])
        }
    }));
    const expensesWithoutLabel = category.expenses.filter(exp => !exp.labels || exp.labels.length === 0);
    if (expensesWithoutLabel.length > 0) {
        points.push({
            name: 'Other',
            y: Math.abs(Utils.addExpenseAmounts(expensesWithoutLabel)),
            events: null
        });
    }
    return points;
};

const getLabelSpendingByCategoryDataPoints = function (router: Router, label: Label): { name: string, y: number }[] {
    const navigate = function (url) {
        router.navigate(url);
    };
    const points = [];
    label.categories.forEach((category: Category) => {
        const total = Utils.addExpenseAmounts(label.expenses.filter(e => e.category.id === category.id));
        points.push({
            name: category.name,
            y: Math.abs(total),
            events: {
                click: navigate.bind(this, ['category/' + category.id])
            }
        });
    });
    return points;
};

const hasLabel = function (expense: Expense, label: Label) {
    return expense.labels.map(l => l.id).includes(label.id);
};

const getSpendingPerYearColumnChart = function (expenses: Expense[]) {
    const yearTotals = [];
    const years: number[] = Utils.getYearsSeries(expenses);
    years.forEach(year => {
        const yearTotal = Utils.addExpenseAmounts(expenses.filter(e => e.createdOn.year === year));
        yearTotals.push(yearTotal);
    });
    const yearsStr: string[] = years.map((year: number) => `${year}`);
    const series = dataSeries('Total', yearTotals, '#555');
    return getColumnChart(yearsStr, series, 'Total per year');
};

const getSpendingPerMonthColumnChart = function (year: number, expenses: Expense[]): Chart {
    const monthTotals = [];
    for (let m = 1; m < 13; m++) {
        const monthTotal = Utils.addExpenseAmounts(expenses.filter(e => e.createdOn.year === year && e.createdOn.month === m));
        monthTotals.push(monthTotal);
    }
    const series = dataSeries('Total', monthTotals, '#555');
    return getColumnChart(MONTHS, series, 'Total per month');
};

export const Charts = {

    getAccountSpendingPerYearColumnChart: function (account: Account) {
        return getSpendingPerYearColumnChart(account.expenses);
    },

    getAccountSpendingPerMonthColumnChart: function (year: number, account: Account) {
        return getSpendingPerMonthColumnChart(year, account.expenses);
    },

    getCategorySpendingPerYearColumnChart: function (category: Category) {
        return getSpendingPerYearColumnChart(category.expenses);
    },

    getCategorySpendingPerMonthColumnChart: function (year: number, category: Category) {
        return getSpendingPerMonthColumnChart(year, category.expenses);
    },

    getLabelSpendingPerYearColumnChart(label: Label) {
        return getSpendingPerYearColumnChart(label.expenses);
    },

    getLabelSpendingPerMonthColumnChart: function (year: number, label: Label) {
        return getSpendingPerMonthColumnChart(year, label.expenses);
    },

    getAccountSpendingPerCategoryPieChart(account: Account, router: Router) {
        const points = getAccountSpendingByCategoryDataPoints(account.categories, router);
        const series = [{
            name: 'Total',
            data: points,
            cursor: 'pointer'
        }];
        return getPieChart(series);
    },

    getCategorySpendingPerLabelPieChart(category: Category, router: Router) {
        const points = getCategorySpendingByLabelDataPoints(category, router);
        const series = [{
            name: 'Total',
            data: points,
            cursor: 'pointer'
        }];
        return getPieChart(series);
    },

    getLabelSpendingPerCategoryPieChart(label: Label, router: Router) {
        const points = getLabelSpendingByCategoryDataPoints(router, label);
        const series = [{
            name: 'Total',
            data: points,
            cursor: 'pointer'
        }];
        return getPieChart(series);
    }

};
