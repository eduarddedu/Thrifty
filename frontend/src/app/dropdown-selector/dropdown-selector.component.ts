import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-dropdown-selector',
  template: `
  <select (change)="onSelectOption($event.target.selectedIndex)">
    <option *ngFor="let option of options" [selected]="option.selected">
      {{ option.value }}
    </option>
  </select>`,
  styleUrls: ['./dropdown-selector.component.css']
})
export class DropdownSelectorComponent {

  @Input() options: Array<{value: any, selected: boolean}>;

  @Output() selectEvent: EventEmitter<number> = new EventEmitter();

  onSelectOption(elemIndex) {
    $('select').blur();
    $('option').blur();
    this.selectEvent.emit(elemIndex);
  }

}
