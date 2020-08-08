import { Chart } from 'angular-highcharts';

import { Account, Expense, Category, Label, LocalDate, DateRange } from '../model';
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

const getSpendingByCategoryDataPoints = function (categories: Category[], router: Router): { name: string, y: number }[] {
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

const getSpendingByLabelDataPoints = function (category: Category): { name: string, y: number }[] {
    const result = category.labels.map((label: Label) => ({
        name: label.name,
        y: Math.abs(getTotalByLabel(category.expenses, label))
    }));
    const expensesWithoutLabel = category.expenses.filter(exp => !exp.labels || exp.labels.length === 0);
    if (expensesWithoutLabel.length > 0) {
        result.push({ name: 'Other', y: Math.abs(Utils.sumExpenses(expensesWithoutLabel)) });
    }
    return result;
};

const getTotalByLabel = function (expenses: Expense[], label: Label): number {
    return Utils.sumExpenses(expenses.filter(expense => hasLabel(expense, label)));
};

const hasLabel = function (expense: Expense, label: Label) {
    return expense.labels.map(l => l.id).includes(label.id);
};

const getSpendingPerYearColumnChart = function (expenses: Expense[]) {
    const mapYearTotal: Map<number, number> = new Map();
    expenses.forEach((exp: Expense) => {
        const year = exp.createdOn.year;
        let total = mapYearTotal.get(year) || 0;
        total += exp.amount;
        mapYearTotal.set(year, total);
    });
    const series = dataSeries('Total', Array.from(mapYearTotal.values()), '#555');
    const uniqueYears = Utils.getYearsSeries(expenses);
    return getColumnChart(uniqueYears.map((year: number) => `${year}`), series, 'Total per year');
};

const getSpendingPerMonthColumnChart = function (year: number, expenseSet: Expense[]): Chart {
    const expenses = expenseSet.filter(exp => exp.createdOn.year === year);
    const mapMonthTotal: Map<number, number> = new Map();
    for (let i = 0; i < 12; i++) {
        mapMonthTotal.set(i, 0);
    }
    expenses.forEach(exp => {
        const month = exp.createdOn.month - 1;
        let total = mapMonthTotal.get(month);
        total += exp.amount;
        mapMonthTotal.set(month, total);
    });
    const series = dataSeries('Total', Array.from(mapMonthTotal.values()), '#555');
    return getColumnChart(MONTHS, series, 'Total per month');
};

export const Charts = {

    getAccountSpendingPerYearColumnChart: function (account: Account) {
        return getSpendingPerYearColumnChart(account.expenses);
    },

    getCategorySpendingPerYearColumnChart: function (category: Category) {
        return getSpendingPerYearColumnChart(category.expenses);
    },

    getAccountSpendingPerMonthColumnChart: function (year: number, account: Account) {
        return getSpendingPerMonthColumnChart(year, account.expenses);
    },

    getCategorySpendingPerMonthColumnChart: function(year: number, category: Category) {
        return getSpendingPerMonthColumnChart(year, category.expenses);
    },

    getAccountSpendingPerCategoryPieChart(account: Account, router: Router) {
        const data = getSpendingByCategoryDataPoints(account.categories, router);
        const series = [{
            name: 'Total',
            data: data,
            cursor: 'pointer'
        }];
        return getPieChart(series);
    },

    getCategorySpendingPerLabelPieChart(category: Category) {
        const series = [{
            name: 'Total',
            data: getSpendingByLabelDataPoints(category)
        }];
        return getPieChart(series);
    }

};
