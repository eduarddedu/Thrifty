import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';

import { RestService } from '../../services/rest.service';
import { MessageService, Kind } from '../../services/messages.service';
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
        private messages: MessageService,
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
            this.showNotification = true;
            this.notificationMessage = this.messages.get(Kind.WEB_SERVICE_OFFLINE);
        });
    }

    onSubmit() {
        this.showForm = false;
        this.showNotification = true;
        this.notificationMessage = this.messages.get(Kind.IN_PROGRESS);
        const expense: Expense = Object.assign(this.readFormData(), {
            labels: this.selectedLabels,
            category: this.selectedCategory
        });
        this.rest.createExpense(expense).subscribe(
            () => {
                this.notificationMessage = this.messages.get(Kind.EXPENSE_CREATE_OK);
                this.analytics.reload();
            },
            err => this.notificationMessage = this.messages.get(Kind.UNEXPECTED_ERROR));
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
