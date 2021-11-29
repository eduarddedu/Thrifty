import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';

import { NotificationService } from '../../../services/notification.service';
import { Kind, AppMessage } from '../../../model/app-message';
import { RestService } from '../../../services/rest.service';
import { Account } from '../../../model';
import { CategoryForm } from './category-form';
import { DataService } from '../../../services/data.service';


@Component({
    templateUrl: './category-form.component.html',
})
export class CreateCategoryComponent extends CategoryForm implements OnInit {
    pageTitle = 'Create Category';
    account: Account;

    constructor(protected fb: FormBuilder,
        private rest: RestService,
        private ns: NotificationService,
        private accountService: DataService) {
        super(fb);
    }

    ngOnInit() {
        this.accountService.load().subscribe((account: Account) => {
            this.account = account;
            this.forbiddenNames = account.categories.map(c => c.name);
            this.createForm();
            this.showForm = true;
        }, err => {
            this.ns.push(AppMessage.of(Kind.WEB_SERVICE_OFFLINE));
        });
    }

    onSubmit() {
        this.showForm = false;
        this.ns.push(AppMessage.of(Kind.IN_PROGRESS));
        const category = Object.assign(this.readFormData(), { accountId: this.account.id });
        this.rest.createCategory(category).subscribe(() => {
            this.ns.push(AppMessage.of(Kind.CATEGORY_CREATE_OK));
            this.accountService.reload();
        },
            err => this.ns.push(AppMessage.of(Kind.UNEXPECTED_ERROR)));
    }
}
