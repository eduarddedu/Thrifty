import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';

import { RestService } from '../../services/rest.service';
import { NotificationService } from '../../services/notification.service';
import { Kind, AppMessage } from '../../model/app-message';
import { Expense, Account, RadioOption } from '../../model';
import { ExpenseFormParent } from './expense-form-parent';
import { AnalyticsService } from '../../services/analytics.service';


@Component({
    templateUrl: './expense-form.component.html',
})
export class ExpenseCreateComponent extends ExpenseFormParent implements OnInit {
    pageTitle = 'Create Expense';
    submitFormButtonText = 'Save';
    account: Account;

    constructor(
        protected fb: FormBuilder,
        private ns: NotificationService,
        private rest: RestService,
        private analytics: AnalyticsService) {
        super(fb);
    }

    ngOnInit() {
        this.analytics.loadAccount().subscribe(v => {
            this.account = v;
            this.setRadioOptionsLabel();
            this.setRadioOptionsCategory();
            this.expenseForm.patchValue({ date: { jsdate: new Date() } });
            this.showForm = true;
        }, err => {
            this.ns.push(AppMessage.of(Kind.WEB_SERVICE_OFFLINE));
        });
    }

    onSubmit() {
        this.showForm = false;
        this.ns.push(AppMessage.of(Kind.IN_PROGRESS));
        const expense: Expense = Object.assign(this.readFormData(), {
            labels: this.selectedLabels,
            category: this.selectedCategory
        });
        this.rest.createExpense(expense).subscribe(
            () => {
                this.ns.push(AppMessage.of(Kind.EXPENSE_CREATE_OK));
                this.analytics.reload();
            },
            err => this.ns.push(AppMessage.of(Kind.UNEXPECTED_ERROR)));
    }

    private setRadioOptionsLabel() {
        this.account.labels.forEach(label => this.radioOptionsLabel.push({
            id: label.id,
            name: label.name,
            checked: false
        }));
    }

    private setRadioOptionsCategory() {
        this.account.categories.forEach(category => {
            const option = {
                id: category.id,
                name: category.name,
                description: category.description,
                checked: false
            };
            this.radioOptionsCategory.push(<RadioOption>option);
        });
    }
}
