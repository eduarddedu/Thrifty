import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { LabelForm } from './label-form';
import { NotificationService } from '../../../services/notification.service';
import { Kind, AppMessage } from '../../../model/app-message';
import { RestService } from '../../../services/rest.service';
import { Account, LabelData } from '../../../model';
import { AccountService } from '../../../services/account.service';


@Component({
    templateUrl: './label-form.component.html',
    styles: []
})
export class CreateLabelComponent extends LabelForm implements OnInit {
    accountId: number;
    pageTitle = 'Create Label';

    constructor(protected fb: FormBuilder,
        private rest: RestService,
        private ns: NotificationService,
        private accountService: AccountService) {
        super(fb);
    }

    ngOnInit() {
        this.accountService.loadAccount().subscribe((account: Account) => {
            this.accountId = account.id;
            this.forbiddenNames = account.labels.map(l => l.name);
            this.createForm();
            this.showForm = true;
        }, err => {
            this.ns.push(AppMessage.of(Kind.WEB_SERVICE_OFFLINE));
        });

    }

    onSubmit() {
        this.showForm = false;
        const label: LabelData = this.readFormData();
        label.accountId = this.accountId;
        this.ns.push(AppMessage.of(Kind.IN_PROGRESS));
        this.rest.createLabel(label).subscribe(() => {
            this.ns.push(AppMessage.of(Kind.LABEL_CREATE_OK));
            this.accountService.reload();
        },
            err => this.ns.push(AppMessage.of(Kind.UNEXPECTED_ERROR)));
    }
}