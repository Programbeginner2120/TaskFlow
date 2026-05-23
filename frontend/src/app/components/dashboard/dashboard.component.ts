import { ChangeDetectionStrategy, Component, computed, signal } from "@angular/core";
import { MultiLineChartComponent } from "../charts/multi-line-chart/multi-line-chart.component";
import { BarSeriesData, DonutSlice, GaugeThreshold, LineSeriesData } from "../../interfaces/charts.interface";
import { BarChartComponent } from "../charts/bar-chart/bar-chart.component";
import { ProgressGaugeComponent } from "../charts/progress-gauge-chart/progress-gauge-chart.component";
import { DonutChartComponent } from "../charts/donut-chart/donut-chart.component";

@Component({
    selector: 'app-dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
    imports: [MultiLineChartComponent, BarChartComponent, ProgressGaugeComponent, DonutChartComponent],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DashboardComponent {

    // progress gauge info

    cpu = signal(43);

    // donut chart info

    regionData = signal<DonutSlice[]>([
        { name: 'North',  value: 1048 },
        { name: 'South',  value: 735  },
        { name: 'East',   value: 580  },
        { name: 'West',   value: 484  },
    ]);

    // bar chart info

    quarters = signal(['Q1', 'Q2', 'Q3', 'Q4']);

    salesSeries = signal<BarSeriesData[]>([
        { name: 'North', data: [320, 440, 390, 510] },
        { name: 'South', data: [220, 310, 280, 390] },
        { name: 'East',  data: [150, 200, 175, 260] },
    ]);

    // multi line chart info

    months = signal(['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun']);

    revenueSeries = signal<LineSeriesData[]>([
        { name: 'Product A', data: [120, 200, 150,  80,  70, 110] },
        { name: 'Product B', data: [220, 182, 191, 234, 290, 330] },
        { name: 'Product C', data: [150, 232, 201, 154, 190, 330] },
    ]);

}