import { Component, Input, Output, EventEmitter } from '@angular/core';

import { RadioOption } from '../model';

@Component({
    selector: 'app-radio-selector',
    templateUrl: './radio-selector.component.html',
    styleUrls: ['./radio-selector.component.css']
})
export class RadioSelectorComponent {

    @Input() options: RadioOption[];
    @Input() singleSelectMode = false;
    @Output() click$: EventEmitter<Object> = new EventEmitter();

    onClickOption(option: RadioOption) {
        if (this.singleSelectMode) {
            this.options.forEach(opt => {
                if (opt.id === option.id) {
                    opt.checked = true;
                } else {
                    opt.checked = false;
                }
            });
        } else {
            option.checked = !option.checked;
        }
        this.exportState();
    }

    private exportState() {
        this.click$.emit({
            options: this.options,
        });
    }
}
