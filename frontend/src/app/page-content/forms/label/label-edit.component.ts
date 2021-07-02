import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { switchMap } from 'rxjs/operators';

import { Label } from '../../../model';
import { LabelForm } from './label-form';
import { NotificationService } from '../../../services/notification.service';
import { Kind, AppMessage } from '../../../model/app-message';
import { RestService } from '../../../services/rest.service';
import { Account } from '../../../model';
import { AccountService } from '../../../services/account.service';

@Component({
    templateUrl: './label-form.component.html'
})
export class LabelEditComponent extends LabelForm implements OnInit {

    pageTitle = 'Update Label';

    labelId: number;

    model: Label;

    constructor(protected fb: FormBuilder,
        private route: ActivatedRoute,
        private rest: RestService,
        private ns: NotificationService,
        private accountService: AccountService) {
        super(fb);
    }

    ngOnInit() {
        this.route.paramMap.pipe(switchMap(params => {
            this.labelId = +params.get('id');
            return this.accountService.loadAccount();
        })).subscribe((account: Account) => {
            this.model = account.labels.find(label => this.labelId === label.id);
            this.forbiddenNames = account.labels.filter(label => this.labelId !== label.id).map(l => l.name);
            this.createForm();
            this.form.patchValue({ name: this.model.name });
            this.showForm = true;
        }, err => {
            this.ns.push(AppMessage.of(Kind.WEB_SERVICE_OFFLINE));
        });
    }

    onSubmit() {
        this.showForm = false;
        const newLabel = Object.assign({ id: this.labelId }, this.readFormData());
        this.ns.push(AppMessage.of(Kind.IN_PROGRESS));
        this.rest.updateLabel(newLabel).subscribe(() => {
            this.ns.push(AppMessage.of(Kind.LABEL_EDIT_OK));
            this.accountService.reload();
        },
            err => this.ns.push(AppMessage.of(Kind.UNEXPECTED_ERROR)));
    }

}
