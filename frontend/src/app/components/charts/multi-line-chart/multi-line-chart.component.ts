import { ChangeDetectionStrategy, Component, computed, HostBinding, inject, input } from "@angular/core";
import { LineSeriesData } from "../../../interfaces/charts.interface";
import { EChartsOption } from "echarts/types/dist/shared";
import { NgxEchartsDirective } from "ngx-echarts";
import { ThemeService } from "../../../services/theme.service";

@Component({
    selector: 'app-multi-line-chart',
    templateUrl: './multi-line-chart.component.html',
    styleUrls: ['./multi-line-chart.component.scss'],
    imports: [NgxEchartsDirective],
    changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MultiLineChartComponent {

    @HostBinding('style.height') get hostHeight() { return this.height(); }
    @HostBinding('style.width')  get hostWidth()  { return this.width(); }
    
    // Input signals
    title = input<string>('');
    categories = input<string[]>([]);
    series = input<LineSeriesData[]>([]);
    height = input<string>('400px');
    width = input<string>('400px');

    // Injected services
    private readonly themeService = inject(ThemeService);

    // Computed
    readonly theme = computed(() =>
        this.themeService.theme()
    );

    chartOptions = computed<EChartsOption>(() => ({
        title: this.title() ? { text: this.title(), textStyle: { color: this.theme() === 'dark' ? 'white' : 'black', } } : undefined,
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: this.series().map(s => s.name)
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '15%',
            outerBoundsMode: 'same',
            outerBoundsContain: 'axisLabel'
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: this.categories()
        },
        yAxis: {
            type: 'value'
        },
        series: this.series().map(s => ({
            name: s.name,
            type: 'line',
            data: s.data,
            smooth: true
        })),
    }));

}