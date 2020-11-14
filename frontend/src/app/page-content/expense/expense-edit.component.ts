import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { switchMap } from 'rxjs/operators';

import { RestService } from '../../services/rest.service';
import { NotificationService } from '../../services/notification.service';
import { Kind, AppMessage } from '../../model/app-message';
import { Expense, Account, RadioOption } from '../../model';
import { ExpenseFormParent } from './expense-form-parent';
import { Utils } from '../../util/utils';
import { AnalyticsService } from '../../services/analytics.service';

@Component({
    templateUrl: './expense-form.component.html',
})

export class ExpenseEditComponent extends ExpenseFormParent implements OnInit {
    pageTitle = 'Update Expense';
    submitFormButtonText = 'Update';
    expenseId: number;
    expense: Expense;


    constructor(
        protected fb: FormBuilder,
        private ns: NotificationService,
        private route: ActivatedRoute,
        private rest: RestService,
        private analytics: AnalyticsService) {
        super(fb);
    }

    ngOnInit() {
        this.route.paramMap.pipe(switchMap(params => {
            this.expenseId = +params.get('id');
            return this.analytics.loadAccount();
        })).subscribe((account: Account) => {
            this.expense = account.expenses.find(ex => ex.id === this.expenseId);
            this.setFormWithModelValues();
            this.setRadioOptionsLabel(account);
            this.selectedLabels = [].concat(this.expense.labels);
            this.setRadioOptionsCategory(account);
            this.showForm = true;
        }, err => {
            this.ns.push(AppMessage.of(Kind.WEB_SERVICE_OFFLINE));
        });
    }

    onSubmit() {
        this.showForm = false;
        this.ns.push(AppMessage.of(Kind.IN_PROGRESS));
        const expense: Expense = Object.assign(this.readFormData(), {
            id: this.expense.id,
            labels: this.selectedLabels,
            category: this.selectedCategory
        });
        this.rest.updateExpense(expense).subscribe(
            () => {
                this.ns.push(AppMessage.of(Kind.EXPENSE_EDIT_OK));
                this.analytics.reload();
            },
            err => this.ns.push(AppMessage.of(Kind.UNEXPECTED_ERROR)));
    }

    private setFormWithModelValues() {
        this.expenseForm.patchValue({
            date: { jsdate: Utils.localDateToJsDate(this.expense.createdOn) },
            description: this.expense.description,
            amount: this.expense.amount
        });
    }

    private setRadioOptionsLabel(account: Account) {
        const map: Map<number, RadioOption> = new Map();
        account.labels.forEach(label => map.set(label.id, {
            id: label.id,
            name: label.name,
            checked: false
        }));
        this.expense.labels.forEach(label => map.set(label.id, {
            id: label.id,
            name: label.name,
            checked: true
        }));
        this.radioOptionsLabel = Array.from(map.values());
    }

    private setRadioOptionsCategory(account: Account) {
        account.categories.forEach(category => {
            const option = {
                id: category.id,
                name: category.name,
                description: category.description,
                checked: category.id === this.expense.category.id ? true : false
            };
            this.radioOptionsCategory.push(<RadioOption>option);
            if (option.checked) {
                this.selectedCategory = { id: category.id, name: category.name, description: category.description };
            }
        });
    }
}

