import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { LabelFormParent } from './label-form-parent';
import { MessageService, Kind } from '../../services/messages.service';
import { RestService } from '../../services/rest.service';
import { Account } from '../../model';
import { AnalyticsService } from '../../services/analytics.service';


@Component({
    templateUrl: './label-form.component.html',
    styles: []
})
export class LabelCreateComponent extends LabelFormParent implements OnInit {

    pageTitle = 'Create Label';

    constructor(protected fb: FormBuilder,
        private rest: RestService,
        private messages: MessageService,
        private analytics: AnalyticsService) {
        super(fb);
    }

    ngOnInit() {
        this.analytics.loadAccount().subscribe((account: Account) => {
            this.forbiddenNames = account.labels.map(l => l.name);
            this.createForm();
            this.showForm = true;
        }, err => {
            this.showNotification = true;
            this.notificationMessage = this.messages.get(Kind.WEB_SERVICE_OFFLINE);
        });

    }

    onSubmit() {
        this.showForm = false;
        const newLabel = this.readFormData();
        this.showNotification = true;
        this.notificationMessage = this.messages.get(Kind.IN_PROGRESS);
        this.rest.createLabel(newLabel).subscribe(() => {
            this.notificationMessage = this.messages.get(Kind.LABEL_CREATE_OK);
            this.analytics.reload();
        },
            err => this.notificationMessage = this.messages.get(Kind.UNEXPECTED_ERROR));
    }
}
