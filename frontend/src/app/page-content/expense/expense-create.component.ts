import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { combineLatest } from 'rxjs';

import { RestService } from '../../services/rest.service';
import { MessageService, Kind } from '../../services/messages.service';
import { Expense, Account, Category, RadioOption } from '../../model';
import { ExpenseFormParent } from './expense-form-parent';


@Component({
    templateUrl: './expense-form.component.html',
})
export class ExpenseCreateComponent extends ExpenseFormParent implements OnInit {

    category: Category;

    pageTitle = 'New expense';

    constructor(
        protected fb: FormBuilder,
        private ms: MessageService, private router: Router, private route: ActivatedRoute, private rs: RestService) {
        super(fb);
    }

    ngOnInit() {
        combineLatest(this.rs.getAccount(), this.route.queryParams).subscribe(v => {
            const account = v[0];
            this.category = account.categories.find(c => c.id === +v[1].categoryId);
            this.setRadioOptionsLabel(account);
            this.setRadioOptionsCategory(account);
            this.expenseForm.patchValue({ date: { jsdate: new Date() } });
            this.showForm = true;
        }, err => {
            this.showNotification = true;
            this.notificationMessage = this.ms.get(Kind.WEB_SERVICE_OFFLINE);
        });
    }

    onSubmit() {
        this.showForm = false;
        this.showNotification = true;
        this.notificationMessage = this.ms.get(Kind.IN_PROGRESS);
        const expense: Expense = Object.assign(this.readFormData(), {
            labels: this.selectedLabels,
            category: this.selectedCategory
        });
        this.rs.createExpense(expense).subscribe(
            () => this.notificationMessage = this.ms.get(Kind.EXPENSE_CREATE_OK),
            err => this.notificationMessage = this.ms.get(Kind.UNEXPECTED_ERROR));
    }

    private setRadioOptionsLabel(account) {
        const resolveEntityLabels = (ac: Account) => {
            if (!this.category) {
                return ac.labels;
            } else {
                return this.category.labels;
            }
        };
        resolveEntityLabels(account).forEach(label => this.radioOptionsLabel.push({
            id: label.id,
            name: label.name,
            checked: false
        }));
    }

    private setRadioOptionsCategory(account: Account) {
        account.categories.forEach(category => {
            const option = {
                id: category.id,
                name: category.name,
                description: category.description,
                checked: category.id === this.category.id ? true : false
            };
            this.radioOptionsCategory.push(<RadioOption>option);
            if (option.checked) {
                this.selectedCategory = {id: category.id, name: category.name, description: category.description};
            }
        });
    }
}
