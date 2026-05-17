import { ChangeDetectionStrategy, Component, signal } from "@angular/core";
import { MultiLineChartComponent } from "../charts/multi-line-chart/multi-line-chart.component";
import { BarSeriesData, GaugeThreshold, LineSeriesData } from "../../interfaces/charts.interface";
import { BarChartComponent } from "../charts/bar-chart/bar-chart.component";
import { ProgressGaugeComponent } from "../charts/progress-gauge-chart/progress-gauge-chart.component";

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
    imports: [MultiLineChartComponent, BarChartComponent, ProgressGaugeComponent],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DashboardComponent {
    /* Mutli line chart */
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

    /* Bar chart */
    // quarters = signal(['Q1', 'Q2', 'Q3', 'Q4']);

    // salesSeries = signal<BarSeriesData[]>([
    //     { name: 'North', data: [320, 440, 390, 510] },
    //     { name: 'South', data: [220, 310, 280, 390] },
    //     { name: 'East',  data: [150, 200, 175, 260] },
    // ]);

    // // Reactively add a new group member
    // addSeries() {
    //     this.salesSeries.update(series => [
    //     ...series,
    //     {
    //         name: `Region ${series.length + 1}`,
    //         data: this.quarters().map(() => Math.floor(Math.random() * 400) + 100),
    //     },
    //     ]);
    // }

    /* Progress Gauges */

    // cpu  = signal(43);
    // temp = signal(67);

    // cpuThresholds: GaugeThreshold[] = [
    //     // { value: 0.5, color: '#22c55e' }, // green   0–50%
    //     // { value: 0.8, color: '#f59e0b' }, // amber  50–80%
    //     // { value: 1.0, color: '#ef4444' }, // red    80–100%
    //     { value: 1.0, color: '#cec1c1' }
    // ];

    // tempThresholds: GaugeThreshold[] = [
    //     // { value: 0.33, color: '#22c55e' },
    //     // { value: 0.66, color: '#f59e0b' },
    //     // { value: 1.0,  color: '#ef4444' },
    //     { value: 1.0, color: '#cec1c1' }
    // ];

    // randomize() {
    //     this.cpu.set(Math.floor(Math.random() * 100));
    //     this.temp.set(Math.floor(Math.random() * 120));
    // }
}