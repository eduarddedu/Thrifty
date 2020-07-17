import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Chart } from 'angular-highcharts';

import { Account, Category, Expense, Label } from '../model';
import { Utils } from '../util/utils';

/*
* ChartsService exposes methods which create the various charts used by the application.
*/

@Injectable()
export class ChartsService {

    private readonly months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

    constructor(private router: Router) { }

    public yearlyTotalSpending(mapYearBalance: Map<number, number>): Chart {
        const categories = Array.from(mapYearBalance.keys()).map(year => year.toString());
        const series = this.dataSeries('Total', Array.from(mapYearBalance.values()), '#555');
        return this.columnChart(categories, series, 'Total per year');
    }

    public monthlySpendingByLabelColumnChart(category: Category, targetYear: number) {
        const series = this.monthlySeries(category, targetYear);
        return this.columnChart(this.months, series, '');
    }

    public monthlySharePerCategory(account: Account, targetYear: number): Chart {
        return this.columnChart(this.months, this.monthlySpendingByCategorySeries(account.categories, targetYear), '');
    }

    public spendingByLabelPieChart(category: Category) {
        const series = [{
            name: 'Total',
            data: this.spendingByLabelDataPoints(category)
        }];
        return this.pieChart(series);
    }

    public getSharePerCategory(categories: Category[]) {
        const series = [{
            name: 'Total',
            data: this.spendingByCategoryDataPoints(categories),
            cursor: 'pointer'
        }];
        return this.pieChart(series);
    }

    private pieChart(series) {
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
    }

    private columnChart(categories: string[], series: { name: string, data: number[], color?: string }[], yAxisTitle: string): Chart {
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
    }

    private dataSeries(name: string, values: number[], color: string) {
        return [{
            name: name,
            data: values.map(v => Math.abs(v)),
            color: color
        }];
    }

    private spendingByLabelDataPoints(category: Category): { name: string, y: number }[] {
        const result = category.labels.map(label => ({
            name: label.name,
            y: Math.abs(this.totalPerLabel(category.expenses, label))
        }));
        const expensesWithoutLabel = category.expenses.filter(exp => !exp.labels || exp.labels.length === 0);
        if (expensesWithoutLabel.length > 0) {
            result.push({name: 'Other', y: Math.abs(Utils.sumExpenses(expensesWithoutLabel))});
        }
        return result;
    }

    private spendingByCategoryDataPoints(categories: Category[]): { name: string, y: number }[] {
        const navigate = function (url) {
            this.router.navigate(url);
        };
        return categories.map(cat => ({
            name: cat.name,
            y: Math.abs(cat.balance),
            events: {
                click: navigate.bind(this, ['category/' + cat.id])
            }
        }));
    }

    private totalPerLabel(expenses: Expense[], label: Label): number {
        return Utils.sumExpenses(expenses.filter(expense => this.hasLabel(expense, label)));
    }

    private hasLabel(expense: Expense, label: Label) {
        return expense.labels.map(l => l.id).includes(label.id);
    }

    private monthlySeries(category: Category, year: number): { name: string, data: number[] }[] {
        return category.labels.map(label => {
            return {
                name: label.name,
                data: this.perMonthBalances(category.expenses.filter(expense => this.hasLabel(expense, label)), year)
            };
        });
    }

    private perMonthBalances(expenses: Expense[], targetYear: number): number[] {
        const data = [];
        for (let i = 0; i < 12; i++) {
            const onOrAfterDate = new Date(targetYear, i);
            const beforeDate = new Date(targetYear, i + 1);
            const currentMonthExpenses = this.filter(expenses, onOrAfterDate, beforeDate);
            const sum = Utils.sumExpenses(currentMonthExpenses);
            data[i] = Math.abs(sum);
        }
        return data;
    }

    private filter(expenses: Expense[], onOrAfterDate: Date, beforeDate: Date): Expense[] {
        return expenses.filter(e => {
            const transDate: Date = Utils.localDateToJsDate(e.createdOn);
            return transDate >= onOrAfterDate && transDate < beforeDate;
        });
    }

    private monthlySpendingByCategorySeries(categories: Category[], targetYear: number): { name: string, data: number[] }[] {
        return categories.map(c => {
            return {
                name: c.name,
                data: this.perMonthBalances(c.expenses, targetYear)
            };
        });
    }
}
