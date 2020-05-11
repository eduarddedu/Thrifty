import { Message } from '../../services/messages.service';
import { RadioOption } from '../../model';

export class UpdatesFormParent {

    showForm = false;

    showNotification = false;

    showModal = false;

    notificationMessage: Message;

    modalMessage: Message;

    actions = ['New', 'Edit', 'Delete'];

    formAction = this.actions[0];

    radioOptions: RadioOption[] = [];

    checkedOption: RadioOption;

    setFormAction(index) {
        this.formAction = this.actions[index];
    }

    goButtonEnabled() {
        if (this.formAction === 'New') {
            return !this.checkedOption;
        } else {
            return this.checkedOption;
        }
    }

    onClickOption(selector) {
        this.checkedOption = selector.currentCheckedOption;
        this.goButtonEnabled();
    }
}
