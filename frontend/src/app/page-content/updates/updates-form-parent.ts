import { Message } from '../../services/messages.service';
import { RadioOption } from '../../model';

export class UpdatesFormParent {

    showForm = false;

    showNotification = false;

    showModal = false;

    notificationMessage: Message;

    modalMessage: Message;

    dropdownOptions: Array<{ value: string, selected: boolean }> = [
        { value: 'New', selected: true },
        { value: 'Edit', selected: false },
        { value: 'Update', selected: false }
    ];

    formAction = this.dropdownOptions[0].value;

    radioOptions: RadioOption[] = [];

    checkedOption: RadioOption;

    setFormAction(index) {
        this.formAction = this.dropdownOptions[index].value;
    }

    goButtonEnabled() {
        if (this.formAction === 'New') {
            return !this.checkedOption;
        } else {
            return this.checkedOption;
        }
    }

    onClickOption(event: { options: RadioOption[] }) {
        for (const option of event.options) {
            if (option.checked) {
                this.checkedOption = option;
            }
        }
        this.goButtonEnabled();
    }
}
