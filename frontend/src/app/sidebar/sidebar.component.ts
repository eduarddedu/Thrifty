import { Component, OnInit, AfterViewInit } from '@angular/core';

import { Account } from '../model';
import { AnalyticsService } from '../services/analytics.service';

@Component({
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html'
})
export class SidebarComponent implements OnInit, AfterViewInit {

    categories: { id: number, name: string }[];
    labels: { id: number, name: string }[];

    constructor(protected analytics: AnalyticsService) { }

    ngOnInit() {
        this.analytics.dataReady.subscribe((account: Account) => {
            if (account) {
                this.setNavigationLinks(account);
            }
        });
    }

    ngAfterViewInit() {
        $('#side-menu')['metisMenu']();
    }

    setNavigationLinks(account: Account) {
        this.categories = account.categories.map(c => ({ id: c.id, name: c.name }));
        this.labels = account.labels.map(l => ({ id: l.id, name: l.name }));
    }
}
