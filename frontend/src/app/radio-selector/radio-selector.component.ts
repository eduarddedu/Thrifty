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
    private lastClickedOption: RadioOption;

    onClickOption(option: RadioOption) {
        if (this.singleSelectMode) {
            if (option === this.lastClickedOption) {
                this.lastClickedOption.checked = !this.lastClickedOption.checked;
            } else {
                option.checked = !option.checked;
                if (option.checked && this.lastClickedOption) {
                    this.lastClickedOption.checked = false;
                }
                this.lastClickedOption = option;
            }
        } else {
            option.checked = !option.checked;
        }
        this.exportState();
    }

    private exportState() {
        this.click$.emit({
            options: this.options,
            currentCheckedOption: this.lastClickedOption && this.lastClickedOption.checked && this.lastClickedOption
        });
    }
}
