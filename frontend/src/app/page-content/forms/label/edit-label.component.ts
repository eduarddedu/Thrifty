import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { zip } from 'rxjs';

import { Label } from '../../../model';
import { LabelForm } from './label-form';
import { NotificationService } from '../../../services/notification.service';
import { Kind, AppMessage } from '../../../model/app-message';
import { RestService } from '../../../services/rest.service';
import { Account } from '../../../model';
import { DataService } from '../../../services/data.service';

@Component({
    templateUrl: './label-form.component.html'
})
export class EditLabelComponent extends LabelForm implements OnInit {
    pageTitle = 'Update Label';
    accountId: number;
    label: Label;

    constructor(protected fb: FormBuilder,
        private route: ActivatedRoute,
        private rest: RestService,
        private ns: NotificationService,
        private accountService: DataService) {
        super(fb);
    }

    ngOnInit() {
        zip(this.route.paramMap, this.accountService.load()).subscribe(value => {
            const params: ParamMap = value[0];
            const account: Account = value[1];
            this.accountId = account.id;
            this.label = account.labels.find(l => l.id === +params.get('id'));
            this.forbiddenNames = account.labels.filter(l => l.id !== this.label.id).map(l => l.name);
            this.createForm();
            this.form.patchValue({ name: this.label.name, description: this.label.description });
            this.showForm = true;
        }, err => {
            this.ns.push(AppMessage.of(Kind.WEB_SERVICE_OFFLINE));
        });
    }

    onSubmit() {
        this.showForm = false;
        const label = Object.assign({ id: this.label.id, accountId: this.accountId }, this.readFormData());
        this.ns.push(AppMessage.of(Kind.IN_PROGRESS));
        this.rest.updateLabel(label).subscribe(() => {
            this.ns.push(AppMessage.of(Kind.LABEL_EDIT_OK));
            this.accountService.reload();
        },
            err => this.ns.push(AppMessage.of(Kind.UNEXPECTED_ERROR)));
    }

}
