import { Message } from '../../services/messages.service';


export class DetailsComponentParent {

    categoryId: number;

    activeSince: Date;

    selectedExpenseId: number;

    showNotification = false;

    showModal = false;

    notificationMessage: Message;

    modalMessage: Message;

    dataReady = false;
}
