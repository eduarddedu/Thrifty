import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { switchMap } from 'rxjs/operators';

import { Label } from '../../model';
import { LabelFormParent } from './label-form-parent';
import { MessageService, Kind } from '../../services/messages.service';
import { RestService } from '../../services/rest.service';
import { Account } from '../../model';
import { AnalyticsService } from '../../services/analytics.service';

@Component({
    templateUrl: './label-form.component.html'
})
export class LabelEditComponent extends LabelFormParent implements OnInit {

    pageTitle = 'Update Label';

    id: number;

    model: Label;

    constructor(protected fb: FormBuilder,
        private route: ActivatedRoute,
        private rest: RestService,
        private messages: MessageService,
        private analytics: AnalyticsService) {
        super(fb);
    }

    ngOnInit() {
        this.route.queryParams.pipe(switchMap(params => {
            this.id = +params.id;
            return this.analytics.loadAccount();
        })).subscribe((account: Account) => {
            this.model = account.labels.find(label => this.id === label.id);
            this.forbiddenNames = account.labels.filter(label => this.id !== label.id).map(l => l.name);
            this.createForm();
            this.labelForm.patchValue({ name: this.model.name });
            this.showForm = true;
        }, err => {
            this.showNotification = true;
            this.notificationMessage = this.messages.get(Kind.WEB_SERVICE_OFFLINE);
        });
    }

    onSubmit() {
        this.showForm = false;
        const newLabel = Object.assign({ id: this.id }, this.readFormData());
        this.showNotification = true;
        this.notificationMessage = this.messages.get(Kind.IN_PROGRESS);
        this.rest.updateLabel(newLabel).subscribe(() => {
            this.notificationMessage = this.messages.get(Kind.LABEL_EDIT_OK);
            this.analytics.reload();
        },
            err => this.notificationMessage = this.messages.get(Kind.UNEXPECTED_ERROR));
    }

}
