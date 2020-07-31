import { Component, OnInit, AfterViewInit } from '@angular/core';

import { Account } from '../model';
import { AnalyticsService } from '../services/analytics.service';

@Component({
    selector: 'app-sidebar',
    templateUrl: './sidebar.component.html'
})
export class SidebarComponent implements OnInit, AfterViewInit {

    links: {id: number, name: string}[];

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
        this.links = account.categories.map(c => ({id: c.id, name: c.name}));
    }
}
