import { Message } from '../../services/messages.service';
import { Chart } from 'angular-highcharts';


export class DetailsComponentParent {

    categoryId: number;

    activeSince: Date;

    pieChart: Chart;

    columnChart: Chart;

    selectedExpenseId: number;

    selectOptions: Array<'All time' | number>;

    mapYearBalance: Map<number, number>;

    hasCharts = false;

    showNotification = false;

    showModal = false;

    notificationMessage: Message;

    modalMessage: Message;

    dataReady = false;

    protected setSelectorOptions() {
        const options: Array<any> = ['All time'];
        const years = Array.from(this.mapYearBalance.keys());
        this.selectOptions = options.concat(years.reverse());
    }

    protected toMap(mapObject: {[key: number]: number}): Map<number, number> {
        const mapYearBalance = new Map();
        Object.keys(mapObject).forEach(key => mapYearBalance.set(key, mapObject[key]));
        return mapYearBalance;
    }
}
