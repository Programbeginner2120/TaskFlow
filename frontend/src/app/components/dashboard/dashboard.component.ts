import { ChangeDetectionStrategy, Component, signal } from "@angular/core";
import { MultiLineChartComponent } from "../charts/multi-line-chart/multi-line-chart.component";
import { LineSeriesData } from "../../interfaces/charts.interface";

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
    imports: [MultiLineChartComponent],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DashboardComponent {
    // months = signal(['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun']);

    // revenueSeries = signal<LineSeriesData[]>([
    //     { name: 'Product A', data: [120, 200, 150,  80,  70, 110] },
    //     { name: 'Product B', data: [220, 182, 191, 234, 290, 330] },
    //     { name: 'Product C', data: [150, 232, 201, 154, 190, 330] },
    // ]);

    // // Example: reactively add a new data point to all series
    // addDataPoint() {
    //     const nextMonths = ['Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    //     const idx = this.months().length - 6; // offset into nextMonths

    //     this.months.update(m => [...m, nextMonths[idx]]);

    //     this.revenueSeries.update(series =>
    //     series.map(s => ({
    //         ...s,
    //         data: [...s.data, Math.floor(Math.random() * 300) + 100],
    //     }))
    //     );
    // }
}