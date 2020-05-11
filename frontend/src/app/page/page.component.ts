import { Component, AfterViewInit } from '@angular/core';

import { Utils } from '../util/utils';

@Component({
    templateUrl: './page.component.html'
})

export class PageComponent {

    onActivateRoute() {
        Utils.scrollPage();
    }
}
