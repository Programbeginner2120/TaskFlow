import { Component, HostBinding, computed, inject, input } from "@angular/core";
import { NgxEchartsDirective } from "ngx-echarts";
import { GaugeThreshold } from "../../../interfaces/charts.interface";
import { EChartsOption } from "echarts/types/dist/shared";
import { ThemeService } from "../../../services/theme.service";

@Component({
    selector: 'app-progress-gauge',
    imports: [NgxEchartsDirective],
    templateUrl: './progress-gauge-chart.component.html',
    styleUrls: ['./progress-gauge-chart.component.scss']
})
export class ProgressGaugeComponent {

    @HostBinding('style.height') get hostHeight() { return this.height(); }
    @HostBinding('style.width')  get hostWidth()  { return this.width(); }

    // input signals
    value = input.required<number>();

    title = input<string>('');
    unit = input<string>('%');
    min = input<number>(0);
    max = input<number>(100);
    height = input<string>('300px');
    width = input<string>('300px');
    thresholds = input<GaugeThreshold[]>([
        // { value: 0.3, color: '#67e0e3' }, // cyan   0–30%
        // { value: 0.7, color: '#37a2da' }, // blue  30–70%
        // { value: 1.0, color: '#fd666d' }, // red   70–100%
        { value: 1.0, color: '#cec1c1' }
    ]);
    // gradientStart = input<string>('#67e0e3'); // cyan
    // gradientEnd   = input<string>('#fd666d'); // red

    // Injected services
    private readonly themeService = inject(ThemeService);

    // Computed
    readonly theme = computed(() =>
        this.themeService.theme()
    );

    chartOptions = computed<EChartsOption>(() => {
        const colorStops: [number, string][] = this.thresholds().length
            ? this.thresholds().map(t => [t.value, t.color])
            : [[1, '#3b82f6' ]];

        return {
            title: this.title()
                ? { text: this.title(), left: 'center', top: 'top', textStyle: { color: this.theme() === 'dark' ? 'white' : 'black', } }
                : undefined,
            series: [
                {
                    type: 'gauge',
                    startAngle: 90,
                    endAngle: -270, // full circle
                    min: this.min(),
                    max: this.max(),
                    pointer: { show: false }, // no needle - progress style
                    progress: {
                        show: true,
                        overlap: false,
                        roundCap: true,
                        width: 18,
                        // itemStyle: {
                        //     // Linear gradient across the progress arc
                        //     color: {
                        //     type: 'linear',
                        //     x: 0, y: 0,   // gradient start (left)
                        //     x2: 1, y2: 0, // gradient end (right)
                        //     colorStops: [
                        //         { offset: 0,   color: this.gradientStart() },
                        //         { offset: 1,   color: this.gradientEnd() },
                        //     ],
                        //     },
                        // },
                    },
                    axisLine: {
                        lineStyle: {
                            width: 18,
                            color: colorStops
                        },
                    },
                    splitLine: { show: false },
                    axisTick: { show: false },
                    axisLabel: { show: false },
                    data: [
                        {
                            value: this.value(),
                            detail: {
                                // centered value label
                                offsetCenter: [0, 0],
                                fontSize: 28,
                                fontWeight: 'bold',
                                formatter: `{value}${this.unit()}`,
                                color: 'inherit'
                            },
                        },
                    ],
                },
            ],
        };
    });
}